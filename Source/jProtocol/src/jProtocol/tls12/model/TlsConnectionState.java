package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsHash;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;
import java.util.List;
import java.util.ArrayList;

public class TlsConnectionState {

	private static final int VERIFY_DATA_LENGTH = 12;
	
	private TlsHash _hash;
	
	private byte[] _clientWriteMacKey;
	private byte[] _serverWriteMacKey;
	private byte[] _clientWriteEncyrptionKey;
	private byte[] _serverWriteEncyrptionKey;
	private byte[] _clientWriteIv;
	private byte[] _serverWriteIv;
	
	private long _clientSequenceNumber;
	private long _serverSequenceNumber;
	private TlsSessionId _sessionId;
	
	private TlsVersion _version;
	
	private List<TlsHandshakeMessage> _currentHandshakeMessages;
	
	public TlsConnectionState() {
		_hash = new TlsHash();
		
		_clientSequenceNumber = 0;
		_serverSequenceNumber = 0;
		_currentHandshakeMessages = new ArrayList<>();
	}
	
	public void computeKeys(TlsSecurityParameters securityParameters) {
		/*
		 * Computation according to chapter 6.3 (p. 26) TLS 1.2
		 */
		int neededKeySize = 2 * securityParameters.getEncKeyLength() + 	//encryption keys
				2 * securityParameters.getMacKeyLength() + 				//mac keys
				2 * securityParameters.getFixedIvLength();					//aead implicit nonce
		
		byte[] keyBlock = TlsPseudoRandomFunction.prf(securityParameters.getMasterSecret(), 
				"key expansion", 
				ByteHelper.concatenate(securityParameters.getClientRandom().getBytes(), securityParameters.getServerRandom().getBytes()), 
				neededKeySize);
		
		setKeys(keyBlock, securityParameters);
	}
	
	private void setKeys(byte[] keyBlock, TlsSecurityParameters securityParameters) {
		int pos = 0;
		
		byte macLength = securityParameters.getMacKeyLength();
		
		_clientWriteMacKey = new byte[macLength];
		System.arraycopy(keyBlock, pos, _clientWriteMacKey, 0, macLength);
		pos += macLength;
		
		_serverWriteMacKey = new byte[macLength];
		System.arraycopy(keyBlock, pos, _serverWriteMacKey, 0, macLength);
		pos += macLength;
		
		byte encKeyLength = securityParameters.getEncKeyLength();
		
		_clientWriteEncyrptionKey = new byte[encKeyLength]; 
		System.arraycopy(keyBlock, pos, _clientWriteEncyrptionKey, 0, encKeyLength);
		pos += encKeyLength;
		
		_serverWriteEncyrptionKey = new byte[encKeyLength]; 
		System.arraycopy(keyBlock, pos, _serverWriteEncyrptionKey, 0, encKeyLength);
		pos += encKeyLength;
		
		byte writeIvLength = securityParameters.getFixedIvLength();
		
		_clientWriteIv = new byte[writeIvLength]; 
		System.arraycopy(keyBlock, pos, _clientWriteIv, 0, writeIvLength);
		pos += writeIvLength;
		
		_serverWriteIv = new byte[writeIvLength]; 
		System.arraycopy(keyBlock, pos, _serverWriteIv, 0, writeIvLength);
		pos += writeIvLength;
	}
	
	public long getClientSequenceNumber() {
		return _clientSequenceNumber;
	}
	
	public void increaseClientSequenceNumber() {
		_clientSequenceNumber++;
	}
	
	public long getServerSequenceNumber() {
		return _serverSequenceNumber;
	}
	
	public void increaseServerSequenceNumber() {
		_serverSequenceNumber++;
	}

	public TlsSessionId getSessionId() {
		if (_sessionId == null) {
			throw new RuntimeException("SessionID must be set first!");
		}
		return _sessionId;
	}

	public void setSessionId(TlsSessionId sessionId) {
		if (sessionId == null) {
			throw new IllegalArgumentException("SessionID must be set!");
		}
		_sessionId = sessionId;
	}

	public TlsVersion getVersion() {
		if (_version == null) {
			//TODO: tls version
			return TlsVersion.getTls12Version();
			//throw new RuntimeException("Version must be set first!");
		}
		return _version;
	}
	
	public boolean hasVersion() {
		return (_version == null);
	}

	public void setVersion(TlsVersion version) {
		if (version == null) {
			throw new IllegalArgumentException("Version must be set!");
		}
		_version = version;
	}

	public byte[] getClientWriteMacKey() {
		if (_clientWriteMacKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _clientWriteMacKey;
	}

	public byte[] getServerWriteMacKey() {
		if (_serverWriteMacKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _serverWriteMacKey;
	}

	public byte[] getClientWriteEncryptionKey() {
		if (_clientWriteEncyrptionKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _clientWriteEncyrptionKey;
	}

	public byte[] getServerWriteEncryptionKey() {
		if (_serverWriteEncyrptionKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _serverWriteEncyrptionKey;
	}

	public byte[] getClientWriteIv() {
		if (_clientWriteIv == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _clientWriteIv;
	}

	public byte[] getServerWriteIv() {
		if (_serverWriteIv == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _serverWriteIv;
	}
	
	public void addHandshakeMessageBytes(TlsHandshakeMessage message) {
		_currentHandshakeMessages.add(message);
	}
	
	private byte[] getBytesFromHandshakeMessages() {
		byte[] result = new byte[0];
		
		for (TlsHandshakeMessage message : _currentHandshakeMessages) {
			result = ByteHelper.concatenate(result, message.getBytes());
		}
		
		return result;
	}
	
	public byte[] getFinishedVerifyDataForServer(byte[] masterSecret) {
		byte[] verifyData = TlsPseudoRandomFunction.prf(masterSecret,
				"server finished",
				_hash.hash(getBytesFromHandshakeMessages()), 
				VERIFY_DATA_LENGTH);
		
		return verifyData;
	}
	
	public byte[] getFinishedVerifyDataForClient(byte[] masterSecret) {
		byte[] verifyData = TlsPseudoRandomFunction.prf(masterSecret,
				"client finished",
				_hash.hash(getBytesFromHandshakeMessages()), 
				VERIFY_DATA_LENGTH);
		
		return verifyData;
	}
	
}
