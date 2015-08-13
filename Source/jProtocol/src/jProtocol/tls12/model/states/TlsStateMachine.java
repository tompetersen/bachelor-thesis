package jProtocol.tls12.model.states;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsConnectionState;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.states.server.TlsInitialServerState;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;

import java.util.List;

public class TlsStateMachine extends StateMachine<TlsCiphertext> {

	public static final int INITIAL_SERVER_STATE = 1;
	public static final int RECEIVED_CLIENT_HELLO_STATE = 2;
	public static final int WAITING_FOR_CLIENT_KEY_EXCHANGE_STATE = 3;
	
	public static final int RECEIVED_UNEXPECTED_MESSAGE_STATE = 410;
	public static final int RECEIVED_BAD_RECORD_MESSAGE_STATE = 420;
	
	private boolean _isServer;
	private TlsSecurityParameters _securityParameters;
	private TlsConnectionState _connectionState;
	
	public TlsStateMachine(CommunicationChannel<TlsCiphertext> channel, TlsConnectionEnd entity) {
		super(channel);
		
		_isServer = (entity == TlsConnectionEnd.server);
		_securityParameters = new TlsSecurityParameters(entity);
		_connectionState = new TlsConnectionState(_securityParameters);
		
		createStates();
	}
	
	private void createStates() {
		//TODO: So oder anders?
		addState(INITIAL_SERVER_STATE, new TlsInitialServerState(this));
	}
	
	/**
	 * Transforms a TLSPlaintext to a TLSCiphertext. The Message will be MACed and 
	 * encrypted according to the current ciphersuite.
	 * 
	 * @param plaintext the TLSPlaintext
	 * 
	 * @return the TLSCiphertext
	 */
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext) {
		byte[] encKey = _isServer ? _connectionState.getServerWriteEncryptionKey() : _connectionState.getClientWriteEncryptionKey();
		byte[] macKey = _isServer ? _connectionState.getServerWriteMacKey() : _connectionState.getClientWriteMacKey();
		byte[] iv = _isServer ? _connectionState.getServerWriteIv() : _connectionState.getClientWriteIv();
		long seqNum = _isServer ? _connectionState.getServerSequenceNumber() : _connectionState.getClientSequenceNumber();
		
		TlsEncryptionParameters encryptionParameters = new TlsEncryptionParameters(seqNum, encKey, macKey, iv);
		
		return _securityParameters.plaintextToCiphertext(plaintext, encryptionParameters);
	}
	
	/**
	 * Transforms a TLSCiphertext to a TLSPlaintext. The Message will be decrypted and 
	 * the MAC will be checked according to the current ciphersuite.
	 * 
	 * @param ciphertext the TLSCiphertext
	 * 
	 * @return the TlsPlaintext
	 */
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext) 
			throws TlsBadRecordMacException, TlsBadPaddingException {
		byte[] encKey = _isServer ? _connectionState.getClientWriteEncryptionKey() : _connectionState.getServerWriteEncryptionKey();
		byte[] macKey = _isServer ? _connectionState.getClientWriteMacKey() : _connectionState.getServerWriteMacKey();
		byte[] iv = _isServer ? _connectionState.getClientWriteIv() : _connectionState.getServerWriteIv();
		long seqNum = _isServer ? _connectionState.getClientSequenceNumber() : _connectionState.getServerSequenceNumber();
		
		TlsEncryptionParameters encryptionParameters = new TlsEncryptionParameters(seqNum, encKey, macKey, iv);
		
		return _securityParameters.ciphertextToPlaintext(ciphertext, encryptionParameters);
	}
	
	public TlsVersion getVersion() {
		return _connectionState.getVersion();
	}
	
	public void setVersion(TlsVersion version) {
		if (isSupportedVersion(version)) {
			_connectionState.setVersion(version);
		}
		else {
			_connectionState.setVersion(getHighestSupportedVersion());
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
	
	public void setClientRandom(TlsRandom random) {
		_securityParameters.setClientRandom(random);
	}

	public void setServerRandom(TlsRandom random) {
		_securityParameters.setServerRandom(random);
	}
	
	public boolean canPerformAbbreviatedHandshakeForSessionId(TlsSessionId sessionId) {
		//TODO
		return false;
	}

	public void setSessionId(TlsSessionId sessionId) {
		_connectionState.setSessionId(sessionId);
	}
	
	public TlsSessionId getSessionId() {
		return _connectionState.getSessionId();
	}
	
	public void setCipherSuiteFromList(List<TlsCipherSuite> list) {
		if (list == null || list.isEmpty()) {
			throw new IllegalArgumentException("Cipher suite list must not be null or empty!");
		}
		//TODO: Choose Cipher suite
		_securityParameters.setCipherSuite(list.get(0));
	}
	
	public TlsCipherSuite getCipherSuite() {
		return _securityParameters.getCipherSuite();
	}
	
}
