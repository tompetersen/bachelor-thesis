package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;

public class TlsConnectionState {

	private TlsSecurityParameters _securityParameters;
	
	private byte[] _clientWriteMacKey;
	private byte[] _serverWriteMacKey;
	private byte[] _clientWriteEncyrptionKey;
	private byte[] _serverWriteEncyrptionKey;
	private byte[] _clientWriteIv;
	private byte[] _serverWriteIv;
	
	private long _sequenceNumber;
	private byte[] _sessionId;
	
	public TlsConnectionState(TlsSecurityParameters securityParameters) {
		_securityParameters = securityParameters;
		_sequenceNumber = 0;
	}
	
	public void computeKeys() {
		/*
		 * Computation according to chapter 6.3 (p. 26) TLS 1.2
		 */
		int neededKeySize = 2 * _securityParameters.getEncKeyLength() + 	//encryption keys
				2 * _securityParameters.getMacKeyLength() + 				//mac keys
				2 * _securityParameters.getFixedIvLength();					//aead implicit nonce
		
		byte[] keyBlock = TlsPseudoRandomFunction.prf(_securityParameters.getMasterSecret(), 
				"key expansion", 
				ByteHelper.concatenate(_securityParameters.getClientRandom(), _securityParameters.getServerRandom()), 
				neededKeySize);
		
		setKeys(keyBlock);
	}
	
	private void setKeys(byte[] keyBlock) {
		int pos = 0;
		
		byte macLength = _securityParameters.getMacKeyLength();
		
		_clientWriteMacKey = new byte[macLength];
		System.arraycopy(keyBlock, pos, _clientWriteMacKey, 0, macLength);
		pos += macLength;
		
		_serverWriteMacKey = new byte[macLength];
		System.arraycopy(keyBlock, pos, _serverWriteMacKey, 0, macLength);
		pos += macLength;
		
		byte encKeyLength = _securityParameters.getEncKeyLength();
		
		_clientWriteEncyrptionKey = new byte[encKeyLength]; 
		System.arraycopy(keyBlock, pos, _clientWriteEncyrptionKey, 0, encKeyLength);
		pos += encKeyLength;
		
		_serverWriteEncyrptionKey = new byte[encKeyLength]; 
		System.arraycopy(keyBlock, pos, _serverWriteEncyrptionKey, 0, encKeyLength);
		pos += encKeyLength;
		
		byte writeIvLength = _securityParameters.getFixedIvLength();
		
		_clientWriteIv = new byte[writeIvLength]; 
		System.arraycopy(keyBlock, pos, _clientWriteIv, 0, writeIvLength);
		pos += writeIvLength;
		
		_serverWriteIv = new byte[writeIvLength]; 
		System.arraycopy(keyBlock, pos, _serverWriteIv, 0, writeIvLength);
		pos += writeIvLength;
	}
	
	public long getSequenceNumber() {
		return _sequenceNumber;
	}
	
	public void increaseSequenceNumber() {
		_sequenceNumber ++;
	}

	public byte[] getSessionId() {
		return _sessionId;
	}

	public void setSessionId(byte[] sessionId) {
		_sessionId = sessionId;
	}

	public byte[] getClientWriteMacKey() {
		if (_clientWriteMacKey == null) {
			throw new RuntimeException("Key must be computed first");
		}
		return _clientWriteMacKey;
	}

	public byte[] getServerWriteMacKey() {
		if (_serverWriteMacKey == null) {
			throw new RuntimeException("Key must be computed first");
		}
		return _serverWriteMacKey;
	}

	public byte[] getClientWriteEncyrptionKey() {
		if (_clientWriteEncyrptionKey == null) {
			throw new RuntimeException("Key must be computed first");
		}
		return _clientWriteEncyrptionKey;
	}

	public byte[] getServerWriteEncyrptionKey() {
		if (_serverWriteEncyrptionKey == null) {
			throw new RuntimeException("Key must be computed first");
		}
		return _serverWriteEncyrptionKey;
	}

	public byte[] getClientWriteIv() {
		if (_clientWriteIv == null) {
			throw new RuntimeException("Key must be computed first");
		}
		return _clientWriteIv;
	}

	public byte[] getServerWriteIv() {
		if (_serverWriteIv == null) {
			throw new RuntimeException("Key must be computed first");
		}
		return _serverWriteIv;
	}
	
}
