package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsConnectionState;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.states.alert.TlsDecodeErrorOccuredState;
import jProtocol.tls12.model.states.alert.TlsDecryptErrorOccuredState;
import jProtocol.tls12.model.states.alert.TlsReceivedBadRecordMessageState;
import jProtocol.tls12.model.states.alert.TlsReceivedUnexpectedMessageState;
import jProtocol.tls12.model.states.client.TlsInitialClientState;
import jProtocol.tls12.model.states.client.TlsReceivedServerHelloDoneState;
import jProtocol.tls12.model.states.client.TlsWaitingForChangeCipherSpecState_Client;
import jProtocol.tls12.model.states.client.TlsWaitingForFinishedState_Client;
import jProtocol.tls12.model.states.client.TlsWaitingForServerCertificateState;
import jProtocol.tls12.model.states.client.TlsWaitingForServerHelloDoneState;
import jProtocol.tls12.model.states.client.TlsWaitingForServerHelloState;
import jProtocol.tls12.model.states.client.TlsWaitingForServerKeyExchangeState;
import jProtocol.tls12.model.states.common.TlsConnectionEstablishedState;
import jProtocol.tls12.model.states.common.TlsReceivedCloseNotifyState;
import jProtocol.tls12.model.states.common.TlsWaitingForCloseNotifyState;
import jProtocol.tls12.model.states.server.TlsInitialServerState;
import jProtocol.tls12.model.states.server.TlsReceivedClientHelloState;
import jProtocol.tls12.model.states.server.TlsReceivedFinishedState;
import jProtocol.tls12.model.states.server.TlsServerWaitingForChangeCipherSpecState_Server;
import jProtocol.tls12.model.states.server.TlsWaitingForClientKeyExchangeState;
import jProtocol.tls12.model.states.server.TlsWaitingForFinishedState_Server;
import jProtocol.tls12.model.values.TlsApplicationData;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVerifyData;
import jProtocol.tls12.model.values.TlsVersion;
import java.util.List;

public class TlsStateMachine extends StateMachine<TlsCiphertext> {
	
	public enum TlsStateMachineEventType {
		connection_established,
		connection_error,
		received_data,
		connection_closed
	}
	
	public class TlsStateMachineEvent extends StateMachineEvent {
		private TlsStateMachineEventType _eventType;

		public TlsStateMachineEvent(TlsStateMachineEventType eventType) {
			super();
			_eventType = eventType;
		}

		public TlsStateMachineEventType getEventType() {
			return _eventType;
		}
	}
	
	public enum TlsStateType {
		//Server
		SERVER_INITIAL_STATE							(1),
		SERVER_RECEIVED_CLIENT_HELLO_STATE				(2),
		SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE	(3),
		SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE	(4),
		SERVER_IS_WAITING_FOR_FINISHED_STATE			(5),
		SERVER_RECEIVED_FINISHED_STATE					(6),
		
		//Client
		CLIENT_INITIAL_STATE							(51),
		CLIENT_IS_WAITING_FOR_SERVER_HELLO_STATE		(52),
		CLIENT_IS_WAITING_FOR_CERTIFICATE_STATE			(53),
		CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE	(54),
		CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE	(55),
		CLIENT_RECEIVED_SERVER_HELLO_DONE_STATE			(56),
		CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE	(57),
		CLIENT_IS_WAITING_FOR_FINISHED_STATE			(58),
		
		//Common
		CONNECTION_ESTABLISHED_STATE					(200),
		RECEIVED_CLOSE_NOTIFY_STATE						(400),
		WAITING_FOR_CLOSE_NOTIFY_STATE					(401),
		
		//Alert
		RECEIVED_UNEXPECTED_MESSAGE_STATE				(410),
		RECEIVED_BAD_RECORD_MESSAGE_STATE				(420),
		DECODE_ERROR_OCCURED_STATE						(450),
		DECRYPT_ERROR_OCCURED_STATE						(451);
		
		private int _type;
		
		private TlsStateType(int type) {
			_type = type;
		}

		public int getType() {
			return _type;
		}
	}
	
	private boolean _isServer;
	
	private TlsSecurityParameters _currentSecurityParameters;
	private TlsConnectionState _currentConnectionState;
	private TlsSecurityParameters _pendingSecurityParameters;
	private TlsConnectionState _pendingConnectionState;
	
	private TlsCipherSuiteRegistry _cipherSuiteRegistry;
	
	public TlsStateMachine(CommunicationChannel<TlsCiphertext> channel, TlsConnectionEnd entity) {
		super(channel);

		_cipherSuiteRegistry = new TlsCipherSuiteRegistry();
		
		_currentSecurityParameters = new TlsSecurityParameters(entity, _cipherSuiteRegistry.getNullCipherSuite());
		_currentConnectionState = new TlsConnectionState(_currentSecurityParameters);
		_pendingSecurityParameters = new TlsSecurityParameters(entity, _cipherSuiteRegistry.getNullCipherSuite());
		_pendingConnectionState = new TlsConnectionState(_pendingSecurityParameters);
		
		createStates();
		
		_isServer = (entity == TlsConnectionEnd.server);
		setTlsState(_isServer ? TlsStateType.SERVER_INITIAL_STATE : TlsStateType.CLIENT_INITIAL_STATE);
	}
	
	private void createStates() {
		//Server
		addState(TlsStateType.SERVER_INITIAL_STATE.getType(), 								new TlsInitialServerState(this));
		addState(TlsStateType.SERVER_RECEIVED_CLIENT_HELLO_STATE.getType(), 				new TlsReceivedClientHelloState(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE.getType(), 	new TlsWaitingForClientKeyExchangeState(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE.getType(), 	new TlsServerWaitingForChangeCipherSpecState_Server(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_FINISHED_STATE.getType(), 				new TlsWaitingForFinishedState_Server(this));
		addState(TlsStateType.SERVER_RECEIVED_FINISHED_STATE.getType(), 					new TlsReceivedFinishedState(this));

		//Client
		addState(TlsStateType.CLIENT_INITIAL_STATE.getType(), 								new TlsInitialClientState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_STATE.getType(), 			new TlsWaitingForServerHelloState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_CERTIFICATE_STATE.getType(), 			new TlsWaitingForServerCertificateState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE.getType(), 	new TlsWaitingForServerKeyExchangeState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE.getType(), 		new TlsWaitingForServerHelloDoneState(this));
		addState(TlsStateType.CLIENT_RECEIVED_SERVER_HELLO_DONE_STATE.getType(), 			new TlsReceivedServerHelloDoneState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE.getType(), 	new TlsWaitingForChangeCipherSpecState_Client(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_FINISHED_STATE.getType(), 				new TlsWaitingForFinishedState_Client(this));

		//Common
		addState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType(), 						new TlsConnectionEstablishedState(this));
		addState(TlsStateType.RECEIVED_CLOSE_NOTIFY_STATE.getType(), 						new TlsReceivedCloseNotifyState(this));
		addState(TlsStateType.WAITING_FOR_CLOSE_NOTIFY_STATE.getType(), 					new TlsWaitingForCloseNotifyState(this));

		//Alert
		addState(TlsStateType.RECEIVED_UNEXPECTED_MESSAGE_STATE.getType(), 					new TlsReceivedUnexpectedMessageState(this));
		addState(TlsStateType.RECEIVED_BAD_RECORD_MESSAGE_STATE.getType(), 					new TlsReceivedBadRecordMessageState(this));
		addState(TlsStateType.DECODE_ERROR_OCCURED_STATE.getType(), 						new TlsDecodeErrorOccuredState(this));
		addState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE.getType(), 						new TlsDecryptErrorOccuredState(this));
	}
	
	/**
	 * Transforms a TLSPlaintext to a TLSCiphertext. The Message will be MACed and 
	 * encrypted according to the current ciphersuite.
	 * 
	 * @param plaintext the TLSPlaintext
	 * 
	 * @return the TLSCiphertext
	 */
	TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext) {
		return _currentSecurityParameters.plaintextToCiphertext(plaintext, getEncryptionParameters());
	}
	
	/**
	 * Transforms a TLSCiphertext to a TLSPlaintext. The Message will be decrypted and 
	 * the MAC will be checked according to the current ciphersuite.
	 * 
	 * @param ciphertext the TLSCiphertext
	 * 
	 * @return the TlsPlaintext
	 * @throws TlsDecodeErrorException 
	 */
	TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext) throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {	
		return _currentSecurityParameters.ciphertextToPlaintext(ciphertext, getEncryptionParameters(), _cipherSuiteRegistry);
	}
	
	private TlsEncryptionParameters getEncryptionParameters() {
		byte[] encKey = null;
		byte[] macKey = null;
		byte[] iv = null;
		long seqNum = 0;
		
		if (_currentSecurityParameters.getCipherSuite() != _cipherSuiteRegistry.getNullCipherSuite()) {
			encKey = _isServer ? _currentConnectionState.getServerWriteEncryptionKey() : _currentConnectionState.getClientWriteEncryptionKey();
			macKey = _isServer ? _currentConnectionState.getServerWriteMacKey() : _currentConnectionState.getClientWriteMacKey();
			iv = _isServer ? _currentConnectionState.getServerWriteIv() : _currentConnectionState.getClientWriteIv();
			seqNum = _isServer ? _currentConnectionState.getServerSequenceNumber() : _currentConnectionState.getClientSequenceNumber();
		}
		
		return new TlsEncryptionParameters(seqNum, encKey, macKey, iv);
	}
	
	public TlsVersion getVersion() {
		/*
		 * Should return pending state version, but it's not available for client hello:
		 * 
		 * Appendix E.2
		 *  Earlier versions of the TLS specification were not fully clear on 
		 *  what the record layer version number (TLSPlaintext.version) should 
		 *  contain when sending ClientHello (i.e., before it is known which 
		 *  version of the protocol will be employed).  Thus, TLS servers 
		 *  compliant with this specification MUST accept any value {03,XX} as 
		 *  the record layer version number for ClientHello.
		 * 
		 */
		TlsVersion version = _pendingConnectionState.hasVersion() ? _pendingConnectionState.getVersion() : TlsVersion.getTls12Version();
		return version;
	}
	
	public void setPendingVersion(TlsVersion version) {
		if (isSupportedVersion(version)) {
			_pendingConnectionState.setVersion(version);
		}
		else {
			_pendingConnectionState.setVersion(getHighestSupportedVersion());
		}
	}
	
	public boolean isSupportedVersion(TlsVersion version) {
		//TODO: version check?
		return version.equals(TlsVersion.getTls12Version());
	}
	
	public TlsVersion getHighestSupportedVersion() {
		//TODO: supportedVersions
		return TlsVersion.getTls12Version();
	}
	
	public void setPendingClientRandom(TlsRandom random) {
		_pendingSecurityParameters.setClientRandom(random);
	}

	public void setPendingServerRandom(TlsRandom random) {
		_pendingSecurityParameters.setServerRandom(random);
	}
	
	public boolean canPerformAbbreviatedHandshakeForSessionId(TlsSessionId sessionId) {
		//TODO: Abbreviated Handshakes
		return false;
	}

	public void setPendingSessionId(TlsSessionId sessionId) {
		_pendingConnectionState.setSessionId(sessionId);
	}
	
	public TlsSessionId getPendingSessionId() {
		return _pendingConnectionState.getSessionId();
	}
	
	public void setPendingCipherSuiteFromList(List<TlsCipherSuite> list) {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Cipher suite list must not be null or empty!");
		}
		//TODO: Choose Cipher suite
		setPendingCipherSuite(list.get(0));
	}
	
	public void setPendingCipherSuite(TlsCipherSuite cipherSuite) {
		_pendingSecurityParameters.setCipherSuite(cipherSuite);
	}
	
	public TlsCipherSuite getPendingCipherSuite() {
		return _pendingSecurityParameters.getCipherSuite();
	}
	
	public List<TlsCipherSuite> allCipherSuites() {
		return _cipherSuiteRegistry.allCipherSuites();
	}
	
	public void changeToPendingState() {
		_currentConnectionState = _pendingConnectionState;
		_currentSecurityParameters = _pendingSecurityParameters;
	}
	
	public void addHandshakeMessageForVerifyData(TlsMessage message) {
		if (isValidVerifyMessage(message)) {
			_pendingConnectionState.addHandshakeMessageBytes(message.getBytes());
		}
		else {
			throw new IllegalArgumentException("Messages in verification data must be handshake messages not equal to hello request or finished!");
		}
	}
	
	private boolean isValidVerifyMessage(TlsMessage message) {
		//TODO: maybe more checks, like already contains message, ...
		if (message.getContentType() == TlsContentType.Handshake){
			TlsHandshakeMessage m = (TlsHandshakeMessage)message;
			if (m.getHandshakeType() != TlsHandshakeType.finished && m.getHandshakeType() != TlsHandshakeType.hello_request) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCorrectVerifyData(TlsVerifyData verifyData) {
		byte[] expected = _isServer ? _currentConnectionState.getFinishedVerifyDataForClient() : _currentConnectionState.getFinishedVerifyDataForServer();
		
		return expected.equals(verifyData.getBytes());
	}
	
	public TlsVerifyData getVerifyDataToSend() {
		byte[] bytes = _isServer ? _currentConnectionState.getFinishedVerifyDataForServer() : _currentConnectionState.getFinishedVerifyDataForClient();
		
		return new TlsVerifyData(bytes);
	}
	
	public void setTlsState(TlsStateType stateType, TlsState sender) {
		setState(stateType.getType(), sender);
	}
	
	private void setTlsState(TlsStateType stateType) {
		setState(stateType.getType());
	}
	
	public void resetConnection() {
		//TODO: reset connection
	}
	
	/*
	 * Public methods
	 */
	public void openConnection() {
		if (isCurrentState(TlsStateType.CLIENT_INITIAL_STATE.getType())) {
			TlsInitialClientState state = (TlsInitialClientState)getCurrentState();
			state.openConnection();
		}
		else {
			throw new RuntimeException("Open connection can only be called on an client in its initial state!");
		}
	}
	
	public void closeConnection() {
		if (isCurrentState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType())) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState)getCurrentState();
			state.closeConnection();
		}
		else {
			throw new RuntimeException("Close connection can only be called on an established connection!");
		}
	}
	
	public void sendData(TlsApplicationData data) {
		if (isCurrentState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType())) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState)getCurrentState();
			state.sendApplicationData(data);
		}
		else {
			throw new RuntimeException("Connection must be established to send data!");
		}
	}
	
	public void receivedData(TlsApplicationData data) {
		notifyStateMachineObservers(new TlsStateMachineEvent(TlsStateMachineEventType.received_data));
	}
}
