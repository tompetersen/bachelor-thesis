package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.StateMachine;
import jProtocol.Abstract.Model.Event;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsConnectionState;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.crypto.TlsRsaCipher;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
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
import jProtocol.tls12.model.states.server.TlsWaitingForChangeCipherSpecState_Server;
import jProtocol.tls12.model.states.server.TlsWaitingForClientKeyExchangeState;
import jProtocol.tls12.model.states.server.TlsWaitingForFinishedState_Server;
import jProtocol.tls12.model.values.TlsApplicationData;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVerifyData;
import jProtocol.tls12.model.values.TlsVersion;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TlsStateMachine extends StateMachine<TlsCiphertext> {
	
	public enum TlsStateMachineEventType {
		connection_established,
		connection_error,
		received_data,
		connection_closed
	}
	
	public class TlsStateMachineEvent extends Event {
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
	private TlsRsaCipher _rsaCipher;
	
	private List<TlsApplicationDataMessage> _cachedApplicationDataMessages;
	
	public TlsStateMachine(TlsConnectionEnd entity) {
		_cipherSuiteRegistry = new TlsCipherSuiteRegistry();
		
		_currentSecurityParameters = new TlsSecurityParameters(entity, _cipherSuiteRegistry.getNullCipherSuite());
		_currentConnectionState = new TlsConnectionState();
		_pendingSecurityParameters = new TlsSecurityParameters(entity, _cipherSuiteRegistry.getNullCipherSuite());
		_pendingConnectionState = new TlsConnectionState();
		
		_cachedApplicationDataMessages = new ArrayList<>();
		
		createStates();
		
		_isServer = (entity == TlsConnectionEnd.server);
		setTlsState(_isServer ? TlsStateType.SERVER_INITIAL_STATE : TlsStateType.CLIENT_INITIAL_STATE);
	}
	
	private void createStates() {
		//Server
		addState(TlsStateType.SERVER_INITIAL_STATE.getType(), 								new TlsInitialServerState(this));
		addState(TlsStateType.SERVER_RECEIVED_CLIENT_HELLO_STATE.getType(), 				new TlsReceivedClientHelloState(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE.getType(), 	new TlsWaitingForClientKeyExchangeState(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE.getType(), 	new TlsWaitingForChangeCipherSpecState_Server(this));
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
		return _currentSecurityParameters.plaintextToCiphertext(plaintext, getEncryptionParameters(false));
	}
	
	/**
	 * Transforms a TLSCiphertext to a TLSPlaintext. The Message will be decrypted and 
	 * the MAC will be checked according to the current ciphersuite.
	 * 
	 * @param ciphertextBytes the TLSCiphertext bytes
	 * 
	 * @return the TlsPlaintext
	 * @throws TlsDecodeErrorException 
	 */
	TlsPlaintext ciphertextToPlaintext(byte[] ciphertextBytes) throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {	
		TlsKeyExchangeAlgorithm algorithm = (_pendingSecurityParameters != null) ? _pendingSecurityParameters.getKeyExchangeAlgorithm() : null;
		return _currentSecurityParameters.ciphertextToPlaintext(ciphertextBytes, getEncryptionParameters(true), _cipherSuiteRegistry, algorithm);
	}
	
	private TlsEncryptionParameters getEncryptionParameters(boolean isReceiving) {
		byte[] encKey = null;
		byte[] macKey = null;
		byte[] iv = null;
		long seqNum = 0;
		
		boolean needsServerValue = (_isServer && !isReceiving) || (!_isServer && isReceiving); 
		
		if (_currentSecurityParameters.getCipherSuite() != _cipherSuiteRegistry.getNullCipherSuite()) {
			encKey = needsServerValue ? _currentConnectionState.getServerWriteEncryptionKey() : _currentConnectionState.getClientWriteEncryptionKey();
			macKey = needsServerValue ? _currentConnectionState.getServerWriteMacKey() : 		_currentConnectionState.getClientWriteMacKey();
			iv = 	 needsServerValue ? _currentConnectionState.getServerWriteIv() : 			_currentConnectionState.getClientWriteIv();
			seqNum = needsServerValue ? _currentConnectionState.getServerSequenceNumber() : 	_currentConnectionState.getClientSequenceNumber();
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
	
	public void computePendingMasterSecret(byte[] preMasterSecret) {
		_pendingSecurityParameters.computeMasterSecret(preMasterSecret);
		_pendingConnectionState.computeKeys(_pendingSecurityParameters);
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
	
	/**
	 * Sets the pending state (Security parameters and connection state) as current state.
	 * Should be called after receiving the change cipher spec message.
	 */
	public void changeToPendingState() {
		_currentConnectionState = _pendingConnectionState;
		_currentSecurityParameters = _pendingSecurityParameters;
	}
	
	public void addHandshakeMessageForVerifyData(TlsHandshakeMessage message) {
		if (isValidVerifyMessage(message)) {
			_pendingConnectionState.addHandshakeMessageBytes(message);
		}
		else {
			throw new IllegalArgumentException("Messages in verification data must be handshake messages not equal to hello request or finished!");
		}
	}
	
	private boolean isValidVerifyMessage(TlsHandshakeMessage message) {
		//TODO: maybe more checks, like already contains message, ...
		TlsHandshakeType type = message.getHandshakeType();
		return type != TlsHandshakeType.finished && type != TlsHandshakeType.hello_request;
	}
	
	public boolean isCorrectVerifyData(TlsVerifyData verifyData) {
		byte[] masterSecret = _currentSecurityParameters.getMasterSecret();
		
		byte[] expected = _isServer ? 	_currentConnectionState.getFinishedVerifyDataForClient(masterSecret) : 
										_currentConnectionState.getFinishedVerifyDataForServer(masterSecret);
		
		return Arrays.equals(expected, verifyData.getBytes());
	}
	
	public TlsVerifyData getVerifyDataToSend() {
		byte[] masterSecret = _currentSecurityParameters.getMasterSecret();
		
		byte[] bytes = _isServer ? 	_currentConnectionState.getFinishedVerifyDataForServer(masterSecret) : 
									_currentConnectionState.getFinishedVerifyDataForClient(masterSecret);
		
		return new TlsVerifyData(bytes);
	}
	
	public void setTlsState(TlsStateType stateType, TlsState sender) {
		setState(stateType.getType(), sender);
		
		if (stateType == TlsStateType.CONNECTION_ESTABLISHED_STATE) {
			notifyStateMachineObservers(new TlsStateMachineEvent(TlsStateMachineEventType.connection_established));
		}
	}
	
	private void setTlsState(TlsStateType stateType) {
		setState(stateType.getType());
	}
	
	public void resetConnection() {
		//TODO: reset connection
	}
	
	public TlsRsaCipher getRsaCipher() {
		if (_rsaCipher == null) {
			throw new RuntimeException("RSA Cipher must be set first!");
		}
		return _rsaCipher;
	}

	public void setRsaCipher(TlsRsaCipher rsaCipher) {
		_rsaCipher = rsaCipher;
	}

	public List<TlsApplicationDataMessage> getCachedApplicationDataMessages() {
		return _cachedApplicationDataMessages;
	}

	public void addCachedApplicationDataMessage(TlsApplicationDataMessage message) {
		_cachedApplicationDataMessages.add(message);
	}
	
	public void clearCachedApplicationDataMessage() {
		_cachedApplicationDataMessages.clear();
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
			throw new RuntimeException(getEntityName() + ": Open connection can only be called on a client in its initial state!");
		}
	}
	
	public void closeConnection() {
		if (isCurrentState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType())) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState)getCurrentState();
			state.closeConnection();
		}
		else {
			throw new RuntimeException(getEntityName() + ": Close connection can only be called on an established connection!");
		}
	}
	
	public void sendData(TlsApplicationData data) {
		if (isCurrentState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType())) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState)getCurrentState();
			state.sendApplicationData(data);
		}
		else {
			throw new RuntimeException(getEntityName() + ": Connection must be established to send data!");
		}
	}
	
	public void receivedData(TlsApplicationData data) {
		MyLogger.info(getEntityName() + " received Data: " + new String(data.getBytes(), StandardCharsets.US_ASCII));
		
		notifyStateMachineObservers(new TlsStateMachineEvent(TlsStateMachineEventType.received_data));
	}
	
	private String getEntityName() {
		return (_isServer ? "Server" : "Client");
	}
	
	public String getViewData() {
		StringBuilder result = new StringBuilder();
		result.append(getEntityName());
		result.append("\n");
		try {
			TlsCipherSuite suite = getPendingCipherSuite();
			result.append(suite.getName());
			result.append("\n");
		}
		catch (Exception e) {}
		
		return result.toString();
	}
}
