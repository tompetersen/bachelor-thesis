package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsMacAlgorithm;
import jProtocol.tls12.model.values.TlsRandom;

public class TlsSecurityParameters {
	
	public enum PrfAlgorithm {
		tls_prf_sha256 //used in all TLS 1.2 cipher suites
	}
	
	public enum CompressionMethod {
		compression_null
	}
	
	private TlsConnectionEnd _connectionEnd;
	private TlsCipherSuite _cipherSuite;
	
	//private PrfAlgorithm _prfAlgorithm;
	//private CompressionMethod _compressionAlgorithm;
	
	private byte[] _masterSecret; //48
	private TlsRandom _clientRandom; //32
	private TlsRandom _serverRandom; //32
	
	public TlsSecurityParameters(TlsConnectionEnd connectionEnd, TlsCipherSuite initialCipherSuite) {
		_connectionEnd = connectionEnd;
		_cipherSuite = initialCipherSuite;
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
	 * @param ciphertext the TLSCiphertext
	 * @param parameters the encryption parameters used to decrypt
	 * @param registry the cipher suite registry
	 * 
	 * @return the TlsPlaintext
	 * 
	 * @throws TlsBadRecordMacException when the message has an invalid MAC
	 * @throws TlsBadPaddingException when decryption of the messages fails beacuse of invalid padding
	 * @throws TlsDecodeErrorException when the message itself can not be decoded properly
	 */
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry) 
			throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		return _cipherSuite.ciphertextToPlaintext(ciphertext, parameters, registry);
	}
	
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
	
	public TlsKeyExchangeAlgorithm getKeyExchangeAlgorithm() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getKeyExchangeAlgorithm();
	}

	public byte getMacKeyLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getMacKeyLength();
	}
	
	public void setClientRandom(TlsRandom clientRandom) {
		if (clientRandom == null) {
			throw new IllegalArgumentException("Invalid client random value! Must not be null!");
		}
		this._clientRandom = clientRandom;
	}

	public void setServerRandom(TlsRandom serverRandom) {
		if (serverRandom == null) {
			throw new IllegalArgumentException("Invalid server random value! Must not be null!");
		}
		this._serverRandom = serverRandom;
	}

	public void computeMasterSecret(byte[] premastersecret) {
		if (_clientRandom == null || _serverRandom == null) {
			throw new RuntimeException("Client and server random values must be set before computing the master secret!");
		}
		if (premastersecret == null || premastersecret.length != 48) {
			throw new IllegalArgumentException("Invalid premastersecret! Must be 48 bytes long!");
		}
		
		/*
		 * According to chapter 8.1. (p.64) of TLS 1.2 specification
		 */
		_masterSecret = TlsPseudoRandomFunction.prf(premastersecret, 
				"master secret", 
				ByteHelper.concatenate(_clientRandom.getBytes(), _serverRandom.getBytes()), 
				48);
	}

	public byte[] getMasterSecret() {
		if (_masterSecret == null) {
			throw new RuntimeException("Master secret must be computed first!");
		}
		return _masterSecret;
	}

	public TlsRandom getClientRandom() {
		if (_clientRandom == null) {
			throw new RuntimeException("Client random must be set first!");
		}
		return _clientRandom;
	}

	public TlsRandom getServerRandom() {
		if (_serverRandom == null) {
			throw new RuntimeException("Server random must be set first!");
		}
		return _serverRandom;
	}

	public TlsConnectionEnd getEntity() {
		return _connectionEnd;
	}
}
