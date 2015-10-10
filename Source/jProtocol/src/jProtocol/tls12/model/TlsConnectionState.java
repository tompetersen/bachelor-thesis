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

	/**
	 * Creates a connection state object with an initial cipher suite object.
	 *  
	 * @param initialCipherSuite the initial cipher suite
	 */
	public TlsConnectionState(TlsCipherSuite initialCipherSuite) {
		_cipherSuite = initialCipherSuite;
		
		_sequenceNumber = 0;
	}
	
	/**
	 * Returns the current sequence number.
	 * 
	 * @return the sequence number
	 */
	public long getSequenceNumber() {
		return _sequenceNumber;
	}
	
	/**
	 * Increases the current sequence number. Should be called after receiving/sending a message.
	 */
	public void increaseSequenceNumber() {
		_sequenceNumber++;
	}
	
	/**
	 * Computes encryption, MAC and AEAD keys depending on the current cipher suite.
	 * 
	 * @param clientRandom the client random value
	 * @param serverRandom the server random value
	 * @param masterSecret the master secret
	 */
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
	
	/**
	 * Returns whether the keys have been computed.
	 * 
	 * @return true, if the keys have been computed
	 */
	public boolean hasComputedKeys() {
		return (_clientWriteEncyrptionKey != null);
	}
	
	/**
	 * Returns the client write MAC key. Must have been computed.
	 * The length is dependent on the current cipher suite.
	 * 
	 * @return the client write MAC key
	 */
	public byte[] getClientWriteMacKey() {
		if (_clientWriteMacKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _clientWriteMacKey;
	}

	/**
	 * Returns the server write MAC key. Must have been computed.
	 * The length is dependent on the current cipher suite.
	 * 
	 * @return the server write MAC key
	 */
	public byte[] getServerWriteMacKey() {
		if (_serverWriteMacKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _serverWriteMacKey;
	}

	/**
	 * Returns the client write encryption key. Must have been computed.
	 * The length is dependent on the current cipher suite.
	 * 
	 * @return the client write encryption key
	 */
	public byte[] getClientWriteEncryptionKey() {
		if (_clientWriteEncyrptionKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _clientWriteEncyrptionKey;
	}

	/**
	 * Returns the server write encryption key. Must have been computed.
	 * The length is dependent on the current cipher suite.
	 * 
	 * @return the server write encryption key
	 */
	public byte[] getServerWriteEncryptionKey() {
		if (_serverWriteEncyrptionKey == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _serverWriteEncyrptionKey;
	}

	/**
	 * Returns the client write IV. Must have been computed.
	 * The length is dependent on the current cipher suite.
	 * 
	 * @return the client write IV
	 */
	public byte[] getClientWriteIv() {
		if (_clientWriteIv == null) {
			throw new RuntimeException("Key must be computed first!");
		}
		return _clientWriteIv;
	}

	/**
	 * Returns the server write IV. Must have been computed.
	 * The length is dependent on the current cipher suite.
	 * 
	 * @return the server write IV
	 */
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
	
	/**
	 * Returns the used key exchange algorithm for the current connection. 
	 * The cipher suite must have been set before.
	 * 
	 * @return the key exchange algorithm
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
