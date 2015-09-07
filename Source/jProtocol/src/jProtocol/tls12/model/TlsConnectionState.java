package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsRandom;

public class TlsConnectionState implements Cloneable {
	private TlsCipherSuite _cipherSuite;
	
	private byte[] _clientWriteMacKey;
	private byte[] _serverWriteMacKey;
	private byte[] _clientWriteEncyrptionKey;
	private byte[] _serverWriteEncyrptionKey;
	private byte[] _clientWriteIv;
	private byte[] _serverWriteIv;
	
	private long _sequenceNumber;

	public TlsConnectionState(TlsCipherSuite initialCipherSuite) {
		_cipherSuite = initialCipherSuite;
		
		_sequenceNumber = 0;
	}
	
	public long getSequenceNumber() {
		return _sequenceNumber;
	}
	
	public void increaseSequenceNumber() {
		_sequenceNumber++;
	}
	
	public void computeKeys(TlsRandom clientRandom, TlsRandom serverRandom, byte[] masterSecret) {
		/*
		 * Computation according to chapter 6.3 (p. 26) TLS 1.2
		 */
		int neededKeySize = 2 * _cipherSuite.getEncryptKeyLength() + 	//encryption keys
				2 * _cipherSuite.getMacKeyLength() + 					//mac keys
				2 * _cipherSuite.getFixedIvLength();					//aead implicit nonce
		
		byte[] keyBlock = TlsPseudoRandomFunction.prf(masterSecret, 
				"key expansion", 
				ByteHelper.concatenate(clientRandom.getBytes(), serverRandom.getBytes()), 
				neededKeySize);
		
		setKeys(keyBlock);
	}
	
	private void setKeys(byte[] keyBlock) {
		int pos = 0;
		
		byte macLength = _cipherSuite.getMacKeyLength();
		
		_clientWriteMacKey = new byte[macLength];
		System.arraycopy(keyBlock, pos, _clientWriteMacKey, 0, macLength);
		pos += macLength;
		
		_serverWriteMacKey = new byte[macLength];
		System.arraycopy(keyBlock, pos, _serverWriteMacKey, 0, macLength);
		pos += macLength;
		
		byte encKeyLength = _cipherSuite.getEncryptKeyLength();
		
		_clientWriteEncyrptionKey = new byte[encKeyLength]; 
		System.arraycopy(keyBlock, pos, _clientWriteEncyrptionKey, 0, encKeyLength);
		pos += encKeyLength;
		
		_serverWriteEncyrptionKey = new byte[encKeyLength]; 
		System.arraycopy(keyBlock, pos, _serverWriteEncyrptionKey, 0, encKeyLength);
		pos += encKeyLength;
		
		byte writeIvLength = _cipherSuite.getFixedIvLength();
		
		_clientWriteIv = new byte[writeIvLength]; 
		System.arraycopy(keyBlock, pos, _clientWriteIv, 0, writeIvLength);
		pos += writeIvLength;
		
		_serverWriteIv = new byte[writeIvLength]; 
		System.arraycopy(keyBlock, pos, _serverWriteIv, 0, writeIvLength);
		pos += writeIvLength;
	}
	
	public boolean hasComputedKeys() {
		return (_clientWriteEncyrptionKey != null);
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
	
	/**
	 * Sets the cipher suite.
	 * 
	 * @param cipherSuite the cipher suite. Must not be null.
	 */
	public void setCipherSuite(TlsCipherSuite cipherSuite) {
		if (cipherSuite == null) {
			throw new IllegalArgumentException("Ciphersuite must not be null!");
		}
		_cipherSuite = cipherSuite;
	}
	
	/**
	 * Returns the current cipher suite, which must have been set before. 
	 * 
	 * @return the current cipher suite
	 */
	public TlsCipherSuite getCipherSuite() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Ciphersuite must be set first!");
		}
		return _cipherSuite;
	}
	
	/*
	 
	public TlsBulkCipherAlgorithm getBulkCipherAlgorithm() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getBulkCipherAlgorithm();
	}
	
	public TlsCipherType getCipherType() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getCipherType();
	}
	
	public byte getEncKeyLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getEncryptKeyLength();
	}

	public byte getBlockLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getBlockLength();
	}

	public byte getFixedIvLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getFixedIvLength();
	}

	public byte getRecordIvLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getRecordIvLength();
	}

	public TlsMacAlgorithm getMacAlgorithm() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getMacAlgorithm();
	}

	public byte getMacLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getMacLength();
	}
	
	public byte getMacKeyLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getMacKeyLength();
	} 
	 
	 */
	
	public TlsKeyExchangeAlgorithm getKeyExchangeAlgorithm() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getKeyExchangeAlgorithm();
	}
	
	/**
	 * Transforms a TLSPlaintext to a TLSCiphertext. The Message will be MACed and 
	 * encrypted according to the used ciphersuite.
	 * 
	 * @param plaintext the TLSPlaintext
	 * @param parameters the encryption parameters used to encrypt
	 * 
	 * @return the TLSCiphertext
	 */
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext, TlsEncryptionParameters parameters) {
		return _cipherSuite.plaintextToCiphertext(plaintext, parameters);
	}
	
	/**
	 * Transforms a TLSCiphertext to a TLSPlaintext. The Message will be decrypted and 
	 * the MAC will be checked according to the used ciphersuite.
	 * 
	 * @param ciphertextBytes the TLSCiphertext bytes
	 * @param parameters the encryption parameters used to decrypt
	 * @param registry the cipher suite registry
	 * @param algorithm the used key exchange algorithm
	 * 
	 * @return the TlsPlaintext
	 * 
	 * @throws TlsBadRecordMacException if the message has an invalid MAC
	 * @throws TlsBadPaddingException if decryption of the messages fails because of invalid padding
	 * @throws TlsDecodeErrorException if the message itself can not be decoded properly
	 */
	public TlsPlaintext ciphertextToPlaintext(byte[] ciphertextBytes, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry, TlsKeyExchangeAlgorithm algorithm) 
			throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		return _cipherSuite.ciphertextToPlaintext(ciphertextBytes, parameters, registry, algorithm);
	}
	
	@Override
	public Object clone() { //throws CloneNotSupportedException {
        try {
			return super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new RuntimeException("Clone not supported!");
		}
    }
}
