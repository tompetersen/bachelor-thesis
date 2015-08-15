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
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.states.client.TlsInitialClientState;
import jProtocol.tls12.model.states.common.TlsConnectionEstablishedState;
import jProtocol.tls12.model.states.server.TlsInitialServerState;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
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
	
	//Server
	public static final int SERVER_INITIAL_STATE = 1;
	public static final int SERVER_RECEIVED_CLIENT_HELLO_STATE = 2;
	public static final int SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE = 3;
	public static final int SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE = 4;
	public static final int SERVER_IS_WAITING_FOR_FINISHED_STATE = 5;
	public static final int SERVER_RECEIVED_FINISHED_STATE = 6;
	
	//Client
	public static final int CLIENT_INITIAL_STATE = 51;
	public static final int CLIENT_IS_WAITING_FOR_SERVER_HELLO_STATE = 52;
	public static final int CLIENT_IS_WAITING_FOR_CERTIFICATE_STATE = 53;
	public static final int CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE = 54;
	public static final int CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE = 55;
	public static final int CLIENT_RECEIVED_SERVER_HELLO_DONE_STATE = 56;
	public static final int CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE = 57;
	public static final int CLIENT_IS_WAITING_FOR_FINISHED_STATE = 58;
	
	//Common
	public static final int CONNECTION_ESTABLISHED_STATE = 200;
	public static final int RECEIVED_CLOSE_NOTIFY_STATE = 400;
	public static final int WAITING_FOR_CLOSE_NOTIFY_STATE = 401;
	
	//Alert
	public static final int RECEIVED_UNEXPECTED_MESSAGE_STATE = 410;
	public static final int RECEIVED_BAD_RECORD_MESSAGE_STATE = 420;
	public static final int DECRYPT_ERROR_OCCURED_STATE = 451;
	
	private boolean _isServer;
	
	private TlsSecurityParameters _currentSecurityParameters;
	private TlsConnectionState _currentConnectionState;
	private TlsSecurityParameters _pendingSecurityParameters;
	private TlsConnectionState _pendingConnectionState;
	
	private TlsCipherSuiteRegistry _cipherSuiteRegistry;
	
	public TlsStateMachine(CommunicationChannel<TlsCiphertext> channel, TlsConnectionEnd entity) {
		super(channel);
		
		_isServer = (entity == TlsConnectionEnd.server);
		
		_currentSecurityParameters = new TlsSecurityParameters(entity);
		_currentConnectionState = new TlsConnectionState(_currentSecurityParameters);
		_pendingSecurityParameters = new TlsSecurityParameters(entity);
		_pendingConnectionState = new TlsConnectionState(_pendingSecurityParameters);
		
		_cipherSuiteRegistry = new TlsCipherSuiteRegistry();
		
		createStates();
	}
	
	private void createStates() {
		//TODO: So oder anders?
		addState(SERVER_INITIAL_STATE, new TlsInitialServerState(this));
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
		byte[] encKey = _isServer ? _currentConnectionState.getServerWriteEncryptionKey() : _currentConnectionState.getClientWriteEncryptionKey();
		byte[] macKey = _isServer ? _currentConnectionState.getServerWriteMacKey() : _currentConnectionState.getClientWriteMacKey();
		byte[] iv = _isServer ? _currentConnectionState.getServerWriteIv() : _currentConnectionState.getClientWriteIv();
		long seqNum = _isServer ? _currentConnectionState.getServerSequenceNumber() : _currentConnectionState.getClientSequenceNumber();
		
		TlsEncryptionParameters encryptionParameters = new TlsEncryptionParameters(seqNum, encKey, macKey, iv);
		
		return _currentSecurityParameters.plaintextToCiphertext(plaintext, encryptionParameters);
	}
	
	/**
	 * Transforms a TLSCiphertext to a TLSPlaintext. The Message will be decrypted and 
	 * the MAC will be checked according to the current ciphersuite.
	 * 
	 * @param ciphertext the TLSCiphertext
	 * 
	 * @return the TlsPlaintext
	 */
	TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext) 
			throws TlsBadRecordMacException, TlsBadPaddingException {
		byte[] encKey = _isServer ? _currentConnectionState.getClientWriteEncryptionKey() : _currentConnectionState.getServerWriteEncryptionKey();
		byte[] macKey = _isServer ? _currentConnectionState.getClientWriteMacKey() : _currentConnectionState.getServerWriteMacKey();
		byte[] iv = _isServer ? _currentConnectionState.getClientWriteIv() : _currentConnectionState.getServerWriteIv();
		long seqNum = _isServer ? _currentConnectionState.getClientSequenceNumber() : _currentConnectionState.getServerSequenceNumber();
		
		TlsEncryptionParameters encryptionParameters = new TlsEncryptionParameters(seqNum, encKey, macKey, iv);
		
		return _currentSecurityParameters.ciphertextToPlaintext(ciphertext, encryptionParameters);
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
	
	public boolean isCorrectVerifyData(byte[] verifyData) {
		byte[] expected = _isServer ? _currentConnectionState.getFinishedVerifyDataForClient() : _currentConnectionState.getFinishedVerifyDataForServer();
		
		return expected.equals(verifyData);
	}
	
	public byte[] getVerifyDataToSend() {
		byte[] result = _isServer ? _currentConnectionState.getFinishedVerifyDataForServer() : _currentConnectionState.getFinishedVerifyDataForClient();
		
		return result;
	}
	
	/*
	 * Public methods
	 */
	
	public void openConnection() {
		if (isCurrentState(CLIENT_INITIAL_STATE)) {
			TlsInitialClientState state = (TlsInitialClientState)getCurrentState();
			state.openConnection();
		}
		else {
			throw new RuntimeException("Open connection can only be called on an client in its initial state!");
		}
	}
	
	public void closeConnection() {
		if (isCurrentState(CONNECTION_ESTABLISHED_STATE)) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState)getCurrentState();
			state.closeConnection();
		}
		else {
			throw new RuntimeException("Close connection can only be called on an established connection!");
		}
	}
	
	public void sendData(byte[] data) {
		if (isCurrentState(TlsStateMachine.CONNECTION_ESTABLISHED_STATE)) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState)getCurrentState();
			state.sendApplicationData(data);
		}
		else {
			throw new RuntimeException("Connection must be established to send data!");
		}
	}
	
	public void receivedData(byte[] data) {
		notifyStateMachineObservers(new TlsStateMachineEvent(TlsStateMachineEventType.received_data));
	}
}
