package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.StateMachine;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
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
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.states.TlsStateMachineEvent.TlsStateMachineEventType;
import jProtocol.tls12.model.states.alert.TlsDecodeErrorOccuredState;
import jProtocol.tls12.model.states.alert.TlsDecryptErrorOccuredState;
import jProtocol.tls12.model.states.alert.TlsProtocolVersionErrorOccuredState;
import jProtocol.tls12.model.states.alert.TlsReceivedBadRecordMessageState;
import jProtocol.tls12.model.states.alert.TlsReceivedFatalAlertMessageState;
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
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsServerDhParams;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVerifyData;
import jProtocol.tls12.model.values.TlsVersion;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class TlsStateMachine extends StateMachine<TlsCiphertext> {

	private TlsStateType _currentStateType;
	protected TlsSecurityParameters _securityParameters;
	protected TlsConnectionState _currentReadConnectionState;
	protected TlsConnectionState _currentWriteConnectionState;
	protected TlsConnectionState _pendingConnectionState;
	private TlsCipherSuiteRegistry _cipherSuiteRegistry;
	private List<TlsApplicationDataMessage> _cachedApplicationDataMessages;

	/**
	 * Creates a state machine object. Registers states. Initial states must be
	 * set in subclasses.
	 * 
	 * @param cipherSuiteRegistry
	 */
	public TlsStateMachine(TlsCipherSuiteRegistry cipherSuiteRegistry) {
		_cipherSuiteRegistry = cipherSuiteRegistry;
		_securityParameters = new TlsSecurityParameters();

		TlsCipherSuite nullCipherSuite = _cipherSuiteRegistry.getNullCipherSuite();
		_currentReadConnectionState = new TlsConnectionState(nullCipherSuite);
		_currentWriteConnectionState = new TlsConnectionState(nullCipherSuite);
		_pendingConnectionState = new TlsConnectionState(nullCipherSuite);

		_cachedApplicationDataMessages = new ArrayList<>();

		createStates();
	}

	private void createStates() {
		// Server
		addState(TlsStateType.SERVER_INITIAL_STATE.getType(), new TlsInitialServerState(this));
		addState(TlsStateType.SERVER_RECEIVED_CLIENT_HELLO_STATE.getType(), new TlsReceivedClientHelloState(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE.getType(), new TlsWaitingForClientKeyExchangeState(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE.getType(), new TlsWaitingForChangeCipherSpecState_Server(this));
		addState(TlsStateType.SERVER_IS_WAITING_FOR_FINISHED_STATE.getType(), new TlsWaitingForFinishedState_Server(this));
		addState(TlsStateType.SERVER_RECEIVED_FINISHED_STATE.getType(), new TlsReceivedFinishedState(this));

		// Client
		addState(TlsStateType.CLIENT_INITIAL_STATE.getType(), new TlsInitialClientState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_STATE.getType(), new TlsWaitingForServerHelloState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_CERTIFICATE_STATE.getType(), new TlsWaitingForServerCertificateState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_KEY_EXCHANGE_STATE.getType(), new TlsWaitingForServerKeyExchangeState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE.getType(), new TlsWaitingForServerHelloDoneState(this));
		addState(TlsStateType.CLIENT_RECEIVED_SERVER_HELLO_DONE_STATE.getType(), new TlsReceivedServerHelloDoneState(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE.getType(), new TlsWaitingForChangeCipherSpecState_Client(this));
		addState(TlsStateType.CLIENT_IS_WAITING_FOR_FINISHED_STATE.getType(), new TlsWaitingForFinishedState_Client(this));

		// Common
		addState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType(), new TlsConnectionEstablishedState(this));
		addState(TlsStateType.RECEIVED_CLOSE_NOTIFY_STATE.getType(), new TlsReceivedCloseNotifyState(this));
		addState(TlsStateType.WAITING_FOR_CLOSE_NOTIFY_STATE.getType(), new TlsWaitingForCloseNotifyState(this));

		// Alert
		addState(TlsStateType.RECEIVED_UNEXPECTED_MESSAGE_STATE.getType(), new TlsReceivedUnexpectedMessageState(this));
		addState(TlsStateType.RECEIVED_BAD_RECORD_MESSAGE_STATE.getType(), new TlsReceivedBadRecordMessageState(this));
		addState(TlsStateType.DECODE_ERROR_OCCURED_STATE.getType(), new TlsDecodeErrorOccuredState(this));
		addState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE.getType(), new TlsDecryptErrorOccuredState(this));
		addState(TlsStateType.PROTOCOL_VERSION_ERROR_OCCURED_STATE.getType(), new TlsProtocolVersionErrorOccuredState(this));
		addState(TlsStateType.RECEIVED_FATAL_ALERT_MESSAGE_STATE.getType(), new TlsReceivedFatalAlertMessageState(this));
	}

	/**
	 * Sets the state current state of this state machine.
	 * 
	 * @param stateType the new state type
	 * @param sender the calling state of this method
	 */
	public void setTlsState(TlsStateType stateType, TlsState sender) {
		_currentStateType = stateType;
		MyLogger.info(getEntityName() + " sets state to " + stateType.toString());
		setState(stateType.getType(), sender);

		if (stateType == TlsStateType.CONNECTION_ESTABLISHED_STATE) {
			notifyObserversOfEvent(new TlsStateMachineEvent(TlsStateMachineEventType.connection_established));
		}
	}

	/**
	 * Convenience method to set the state without a calling state.
	 * 
	 * @param stateType the new state type
	 */
	protected void setTlsState(TlsStateType stateType) {
		_currentStateType = stateType;
		MyLogger.info(getEntityName() + " sets state to " + stateType.toString());
		setState(stateType.getType());
	}

	/**
	 * Returns the current state.
	 * 
	 * @return the current state
	 */
	public TlsStateType getCurrentTlsState() {
		return _currentStateType;
	}

	/**
	 * Transforms a TLSPlaintext to a TLSCiphertext. The Message will be MACed
	 * and
	 * encrypted according to the current ciphersuite.
	 * 
	 * @param plaintext the TLSPlaintext
	 * 
	 * @return the TLSCiphertext
	 */
	TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext) {
		return _currentWriteConnectionState.plaintextToCiphertext(plaintext, getEncryptionParameters(false));
	}

	/**
	 * Transforms a TLSCiphertext to a TLSPlaintext. The Message will be
	 * decrypted and
	 * the MAC will be checked according to the current ciphersuite.
	 * 
	 * @param ciphertextBytes the TLSCiphertext bytes
	 * 
	 * @return the TlsPlaintext
	 * @throws TlsDecodeErrorException
	 */
	TlsPlaintext ciphertextToPlaintext(byte[] ciphertextBytes) throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		TlsKeyExchangeAlgorithm algorithm = _pendingConnectionState.getKeyExchangeAlgorithm();
		return _currentReadConnectionState.ciphertextToPlaintext(ciphertextBytes, getEncryptionParameters(true), _cipherSuiteRegistry, algorithm);
	}

	/**
	 * Returns the encryption parameters for a state machine dependent on sending or receiving a message. 
	 * 
	 * @param isReceiving true when receiving a message, false when sending
	 * 
	 * @return the encryption parameters
	 */
	protected abstract TlsEncryptionParameters getEncryptionParameters(boolean isReceiving);

	/**
	 * Returns whether the current read cipher suite is TLS_NULL_WITH_NULL_NULL.
	 * 
	 * @return true, when it is NOT, false otherwise
	 */
	public boolean currentReadCipherSuiteIsNotTlsNull() {
		return (_currentReadConnectionState.getCipherSuite() != _cipherSuiteRegistry.getNullCipherSuite());
	}

	/**
	 * Returns whether the current write cipher suite is TLS_NULL_WITH_NULL_NULL.
	 * 
	 * @return true, when it is NOT, false otherwise
	 */
	public boolean currentWriteCipherSuiteIsNotTlsNull() {
		return (_currentWriteConnectionState.getCipherSuite() != _cipherSuiteRegistry.getNullCipherSuite());
	}

/*
 * Values
 */
	/**
	 * Returns the currently set TLS version.
	 * 
	 * @return the TLS version
	 */
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
		TlsVersion version = _securityParameters.hasVersion() ? _securityParameters.getVersion() : TlsVersion.getTls12Version();
		return version;
	}
	
	/**
	 * Sets the current TLS version.
	 * 
	 * @param version the TLS version
	 */
	public void setVersion(TlsVersion version) {
		if (isSupportedVersion(version)) {
			_securityParameters.setVersion(version);
		}
		else {
			//TODO: Used for different TLS versions -> Implement if necessary
			_securityParameters.setVersion(getHighestSupportedVersion());
		}
	}

	/**
	 * Returns whether the TLS version is supported. Currently just TLS 1.2 is supported.
	 * 
	 * @param version the TLS version
	 * 
	 * @return true, if the version is supported
	 */
	public boolean isSupportedVersion(TlsVersion version) {
		return version.equals(TlsVersion.getTls12Version());
	}

	/**
	 * Returns the highest supported TLS version (currently TLS 1.2).
	 * 
	 * @return the highest supported version
	 */
	public TlsVersion getHighestSupportedVersion() {
		return TlsVersion.getTls12Version();
	}

	/**
	 * Returns whether an abbreviated handshake can be performed for the session ID.
	 * 
	 * @param sessionId the session ID
	 * 
	 * @return true, if an abbreviated handshake can be performed for the session ID
	 */
	public boolean canPerformAbbreviatedHandshakeForSessionId(TlsSessionId sessionId) {
		// TODO: Used for abbreviated handshake -> implement if necessary
		return false;
	}

	/**
	 * Returns whether the negotiated cipher suite key exchange algorithm 
	 * needs a server key exchange message.
	 * 
	 * @return true, if a server key exchange message is necessary
	 */
	public boolean needsServerKeyExchangeMessage() {
		return (getPendingCipherSuite().getKeyExchangeAlgorithm() == TlsKeyExchangeAlgorithm.dhe_rsa);
	}

	/**
	 * Sets the TLS session ID. 
	 * 
	 * @param sessionId the session ID
	 */
	public void setSessionId(TlsSessionId sessionId) {
		_securityParameters.setSessionId(sessionId);
	}

	/**
	 * Returns the currently set session ID.
	 * 
	 * @return the session ID
	 */
	public TlsSessionId getSessionId() {
		return _securityParameters.getSessionId();
	}

	/**
	 * Increases the sequence number of the current write state. Should be 
	 * called after sending a message.
	 */
	public void increaseWriteSequenceNumber() {
		_currentWriteConnectionState.increaseSequenceNumber();
	}

	/**
	 * Increases the sequence number of the current read state. Should be 
	 * called after receiving a message.
	 */
	public void increaseReadSequenceNumber() {
		_currentReadConnectionState.increaseSequenceNumber();
	}

	/**
	 * Sets the client random value.
	 * 
	 * @param random the client random value
	 */
	public void setClientRandom(TlsRandom random) {
		_securityParameters.setClientRandom(random);
	}

	/**
	 * Sets the server random value.
	 * 
	 * @param random the server random value
	 */
	public void setServerRandom(TlsRandom random) {
		_securityParameters.setServerRandom(random);
	}

	/**
	 * Computes and sets the master secret from the pre master secret.
	 * 
	 * @param preMasterSecret the pre master secret
	 */
	public void computeMasterSecret(byte[] preMasterSecret) {
		_securityParameters.computeMasterSecret(preMasterSecret);
		_pendingConnectionState.computeKeys(_securityParameters.getClientRandom(), _securityParameters.getServerRandom(), _securityParameters.getMasterSecret());
	}

	/**
	 * Sets the cipher suite in the pending state by choosing the first (preferred) 
	 * cipher suite from a list. 
	 * 
	 * @param list the cipher suite list
	 */
	public void setPendingCipherSuiteFromList(List<TlsCipherSuite> list) {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Cipher suite list must not be null or empty!");
		}

		// first client cipher suite list entry is chosen as cipher suite
		setPendingCipherSuite(list.get(0));
	}

	/**
	 * Sets the cipher suite in the pending state.
	 * 
	 * @param cipherSuite the cipher suite
	 */
	public void setPendingCipherSuite(TlsCipherSuite cipherSuite) {
		_pendingConnectionState.setCipherSuite(cipherSuite);
	}

	/**
	 * Returns the pending cipher suite.
	 * 
	 * @return the pending cipher suite
	 */
	public TlsCipherSuite getPendingCipherSuite() {
		return _pendingConnectionState.getCipherSuite();
	}

	/**
	 * Returns all present cipher suite not including the TLS null cipher 
	 * suite (TLS_NULL_WITH_NULL_NULL).
	 * 
	 * @return the cipher suite list
	 */
	public List<TlsCipherSuite> allCipherSuites() {
		return _cipherSuiteRegistry.allCipherSuites();
	}

	/**
	 * Returns the server certificate list. 
	 * 
	 * @return the certificate list
	 */
	public abstract List<TlsCertificate> getCertificateList();

	/**
	 * Returns the clients public DH key.
	 * 
	 * @return the clients public DH key
	 */
	public abstract TlsClientDhPublicKey getClientDhPublicKey();

	/**
	 * Returns the servers DH parameters.
	 * 
	 * @return the servers DH parameters
	 */
	public abstract TlsServerDhParams getServerDhParams();

	/**
	 * Sets the pending state as current read state.
	 * Should be called after receiving the change cipher spec message.
	 */
	public void changeReadStateToPendingState() {
		_currentReadConnectionState = (TlsConnectionState) _pendingConnectionState.clone();
	}

	/**
	 * Sets the pending state as current write state.
	 * Should be called after sending the change cipher spec message.
	 */
	public void changeWriteStateToPendingState() {
		_currentWriteConnectionState = (TlsConnectionState) _pendingConnectionState.clone();
	}

/*
 * Verification data methods for finished message.
 */
	/**
	 * Adds a handshake message for inclusion in the verify data computation. 
	 * Finished and hello_request messages must not be included.
	 * 
	 * @param message the handshake message
	 */
	public void addHandshakeMessageForVerifyData(TlsHandshakeMessage message) {
		if (isValidVerifyMessage(message)) {
			_securityParameters.addHandshakeMessageForVerification(message);
		}
		else {
			throw new IllegalArgumentException("Messages in verification data must be handshake messages not equal to hello request or finished!");
		}
	}

	private boolean isValidVerifyMessage(TlsHandshakeMessage message) {
		TlsHandshakeType type = message.getHandshakeType();
		return type != TlsHandshakeType.finished && type != TlsHandshakeType.hello_request;
	}

	/**
	 * Returns whether the verify data received in the finished message is correct. 
	 * This means the same handshake messages were converted with the same master 
	 * secret and label.
	 * 
	 * @param verifyData the verify data
	 * 
	 * @return true, if the verify data computed locally is equal to the verify 
	 * 		data included in the finished message
	 */
	public abstract boolean isCorrectVerifyData(TlsVerifyData verifyData);

	/**
	 * Returns the verify data to include in the finished message
	 * 
	 * @return the verify data
	 */
	public abstract TlsVerifyData getVerifyDataToSend();

/*
 * Cached application data
 */
	/**
	 * Returns all cached application data messages.
	 * 
	 * @return the cached application data messages
	 */
	public List<TlsApplicationDataMessage> getCachedApplicationDataMessages() {
		return _cachedApplicationDataMessages;
	}

	/**
	 * Adds an application data message to the cached messages. This is used for 
	 * application data messages which can be sent after the finished message before 
	 * the communication partner is in established state. 
	 * 
	 * @param message the message
	 */
	public void addCachedApplicationDataMessage(TlsApplicationDataMessage message) {
		_cachedApplicationDataMessages.add(message);
	}

	/**
	 * Clears the application data message cache.
	 */
	public void clearCachedApplicationDataMessage() {
		_cachedApplicationDataMessages.clear();
	}

/*
 * Public methods
 */
	/**
	 * Starts a connection to the server. Must be called in initial client state.
	 */
	public void openConnection() {
		if (isCurrentState(TlsStateType.CLIENT_INITIAL_STATE.getType())) {
			TlsInitialClientState state = (TlsInitialClientState) getCurrentState();
			state.openConnection();
		}
		else {
			throw new RuntimeException(getEntityName() + ": Open connection can only be called on a client in its initial state!");
		}
	}

	/**
	 * Closes a connection. Must be called in established state.
	 */
	public void closeConnection() {
		if (isCurrentState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType())) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState) getCurrentState();
			state.closeConnection();
		}
		else {
			throw new RuntimeException(getEntityName() + ": Close connection can only be called on an established connection!");
		}
	}

	/**
	 * Resets the connection.
	 */
	public void resetConnection() {
		// TODO: reset connection
	}

	/**
	 * Sends application data via an established connection.
	 * 
	 * @param data the application data
	 */
	public void sendData(TlsApplicationData data) {
		if (isCurrentState(TlsStateType.CONNECTION_ESTABLISHED_STATE.getType())) {
			TlsConnectionEstablishedState state = (TlsConnectionEstablishedState) getCurrentState();
			state.sendApplicationData(data);
		}
		else {
			throw new RuntimeException(getEntityName() + ": Connection must be established to send data!");
		}
	}

	/**
	 * Receives TLS data. 
	 * 
	 * @param data the data
	 */
	public void receivedData(TlsApplicationData data) {
		MyLogger.info(getEntityName() + " received Data: " + new String(data.getBytes(), StandardCharsets.US_ASCII));

		notifyObserversOfEvent(new TlsStateMachineEvent(TlsStateMachineEventType.received_data));
	}

	protected abstract String getEntityName();

/*
 * View data
 */
	/**
	 * Returns the view data (list of key value objects) for displaying purposes.
	 * 
	 * @return the view data
	 */
	public List<KeyValueObject> getViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();

		String sessionId = _securityParameters.hasSessionId() ? "0x" + ByteHelper.bytesToHexString(_securityParameters.getSessionId().getSessionId()) : "";
		KeyValueObject kvo = new KeyValueObject("Session ID", sessionId);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_SessionId.html"));
		result.add(kvo);

		String clientRandom = _securityParameters.hasClientRandom() ? "0x" + ByteHelper.bytesToHexString(_securityParameters.getClientRandom().getBytes()) : "";
		kvo = new KeyValueObject("Client random", clientRandom);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_Random.html"));
		result.add(kvo);

		String serverRandom = _securityParameters.hasServerRandom() ? "0x" + ByteHelper.bytesToHexString(_securityParameters.getServerRandom().getBytes()) : "";
		kvo = new KeyValueObject("Server random", serverRandom);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_Random.html"));
		result.add(kvo);

		String preMasterSecret = _securityParameters.hasSetPreMasterSecret() ? "0x" + ByteHelper.bytesToHexString(_securityParameters.getPreMasterSecret()) : "";
		kvo = new KeyValueObject("Pre master secret", preMasterSecret);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_PreMasterSecret.html"));
		result.add(kvo);

		String masterSecret = _securityParameters.hasComputedMasterSecret() ? "0x" + ByteHelper.bytesToHexString(_securityParameters.getMasterSecret()) : "";
		kvo = new KeyValueObject("Master secret", masterSecret);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_MasterSecret.html"));
		result.add(kvo);

		result.add(objectForCertificateList(getCertificateList()));
		result.add(objectForDhValues(getServerDhParams(), getClientDhPublicKey()));

		result.add(objectForConnectionState(_currentReadConnectionState, "Current read state", "states/TLS12_CurrentState.html"));
		result.add(objectForConnectionState(_currentWriteConnectionState, "Current write state", "states/TLS12_CurrentState.html"));
		result.add(objectForConnectionState(_pendingConnectionState, "Pending state", "states/TLS12_PendingState.html"));

		return result;
	}

	private KeyValueObject objectForCertificateList(List<TlsCertificate> certList) {
		KeyValueObject result;
		if (certList != null) {
			ArrayList<KeyValueObject> listObjects = new ArrayList<>();
			for (TlsCertificate certificate : certList) {
				listObjects.add(new KeyValueObject("", certificate.getReadableCertificate()));
			}

			result = new KeyValueObject("Certificate", listObjects);
		}
		else {
			result = new KeyValueObject("Certificate", "");
		}

		result.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_Certificate.html"));
		return result;
	}

	private KeyValueObject objectForDhValues(TlsServerDhParams serverParams, TlsClientDhPublicKey clientPublicKey) {
		ArrayList<KeyValueObject> resultList = new ArrayList<>();

		boolean hasServerParams = (serverParams != null);
		boolean hasClientKey = (clientPublicKey != null);

		// TODO: maybe private key?
		if (hasServerParams) {
			resultList.add(new KeyValueObject("dh_p", hasServerParams ? "0x" + ByteHelper.bytesToHexString(serverParams.getDh_p()) : ""));
			resultList.add(new KeyValueObject("dh_g", hasServerParams ? "0x" + ByteHelper.bytesToHexString(serverParams.getDh_g()) : ""));
			resultList.add(new KeyValueObject("dh_Ys", hasServerParams ? "0x" + ByteHelper.bytesToHexString(serverParams.getDh_Ys()) : ""));
		}
		if (hasClientKey) {
			resultList.add(new KeyValueObject("dh_Yc", hasClientKey ? "0x" + ByteHelper.bytesToHexString(clientPublicKey.getPublicKey()) : ""));
		}

		KeyValueObject result = new KeyValueObject("DH parameters", resultList);
		result.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_DH.html"));
		return result;
	}

	private KeyValueObject objectForConnectionState(TlsConnectionState state, String title, String infoFileName) {
		ArrayList<KeyValueObject> listObjects = new ArrayList<>();

		KeyValueObject kvo = new KeyValueObject("Cipher suite", state.getCipherSuite().getName());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_CipherSuite.html"));
		listObjects.add(kvo);

		kvo = new KeyValueObject("Sequence number", Long.toHexString(state.getSequenceNumber()));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_SequenceNumber.html"));
		listObjects.add(kvo);

		boolean hasKeys = state.hasComputedKeys();
		kvo = new KeyValueObject("Client write encryption key", hasKeys ? ByteHelper.bytesToHexString(state.getClientWriteEncryptionKey()) : "");
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_WriteEncryptionKey.html"));
		listObjects.add(kvo);

		kvo = new KeyValueObject("Client write IV", hasKeys ? ByteHelper.bytesToHexString(state.getClientWriteIv()) : "");
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_WriteIv.html"));
		listObjects.add(kvo);

		kvo = new KeyValueObject("Client write MAC key", hasKeys ? ByteHelper.bytesToHexString(state.getClientWriteMacKey()) : "");
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_WriteMacKey.html"));
		listObjects.add(kvo);

		kvo = new KeyValueObject("Server write encryption key", hasKeys ? ByteHelper.bytesToHexString(state.getServerWriteEncryptionKey()) : "");
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_WriteEncryptionKey.html"));
		listObjects.add(kvo);

		kvo = new KeyValueObject("Server write IV", hasKeys ? ByteHelper.bytesToHexString(state.getServerWriteIv()) : "");
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_WriteIv.html"));
		listObjects.add(kvo);

		kvo = new KeyValueObject("Server write MAC key", hasKeys ? ByteHelper.bytesToHexString(state.getServerWriteMacKey()) : "");
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_WriteMacKey.html"));
		listObjects.add(kvo);

		KeyValueObject result = new KeyValueObject(title, listObjects);
		result.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName(infoFileName));

		return result;
	}
}
