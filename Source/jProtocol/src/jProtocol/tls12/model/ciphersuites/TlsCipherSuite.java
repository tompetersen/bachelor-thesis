package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.TlsSecurityParameters.BulkCipherAlgorithm;
import jProtocol.tls12.model.TlsSecurityParameters.CipherType;
import jProtocol.tls12.model.TlsSecurityParameters.MacAlgorithm;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_NULL_WITH_NULL_NULL;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;

import java.util.HashMap;
import java.util.Map;

/*
 * Information can be found in 
 * - appendix A.5. (cipher suite codes and stuff)
 * - appendix C (key and other sizes)
 */
public abstract class TlsCipherSuite {
	
	private static Map<Short, TlsCipherSuite> _supportedCipherSuites;
	
	static {
		_supportedCipherSuites = new HashMap<Short, TlsCipherSuite>();
		
		//TODO: Add cipher suites (maybe automagically)
		_supportedCipherSuites.put((short) 0, new TlsCipherSuite_NULL_WITH_NULL_NULL());
	}
	
	public static TlsCipherSuite cipherSuiteFromValue(short value) {
		TlsCipherSuite suite = _supportedCipherSuites.get(value);
		if (suite != null) {
			return suite;
		}
		else {
			throw new IllegalArgumentException("Cipher suite for value " + value + " not found!");
		}
	}
	
	public static short valueFromCipherSuite(TlsCipherSuite cipherSuite) {
		for (short s : _supportedCipherSuites.keySet()) {
			if (_supportedCipherSuites.get(s).getClass().equals(cipherSuite.getClass())) {
				return s;
			}
		}
		throw new IllegalArgumentException("Value for cipher suite " + cipherSuite.getName() + " not found!");
	}
	
	public abstract TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext, TlsEncryptionParameters parameters);
	
	public abstract TlsPlaintext ciphertextToPlaintext(TlsCiphertext plaintext, TlsEncryptionParameters parameters) throws TlsBadRecordMacException ;
	
	/**
	 * The name equal to the cipher suite name in TLS 1.2 specification.
	 * 
	 * @return the cipher suite name
	 */
	public abstract String getName();
	
	/**
	 * The two byte code used in handshake messages.
	 * 
	 * @return the code
	 */
	public abstract short getCode();
	
	/**
	 * The cipher type (stream, block or aead) of the cipher suite.
	 * 
	 * @return the cipher type
	 */
	public abstract CipherType getCipherType();
	
	/**
	 * The algorithm (null, rc4, 3des, aes) used for encryption.
	 * 
	 * @return the encryption algorithm
	 */
	public abstract BulkCipherAlgorithm getBulkCipherAlgorithm();
	
	/**
	 * The length of the encryption key used by the cipher suite.
	 * 
	 * @return the encryption key length in bytes
	 */
	public abstract byte getEncryptKeyLength();
	
	/**
	 * The block length of the encryption algorithm.
	 * 
	 * @return the block length in bytes for block or AEAD cipher suites or 0 for stream cipher suites
	 */
	public abstract byte getBlockLength();
	
	/**
	 * The fixed initialization vector length used by AEAD ciphers for implicit nonce.
	 * 
	 * @return the length in bytes for AEAD ciphers or 0 for block and stream cipher suites
	 */
	public abstract byte getFixedIvLength();
	
	/**
	 * The record initialization vector length used by block ciphers.
	 * 
	 * @return the length in bytes for block ciphers or 0 for stream and AEAD cipher suites
	 */
	public abstract byte getRecordIvLength();
	
	/**
	 * The MAC algorithm (null or hmac with md5, sha1, sha256, ...) used by the cipher suite.
	 * 
	 * @return the MAC algorithm
	 */
	public abstract MacAlgorithm getMacAlgorithm();
	
	/**
	 * The MAC length of the cipher suite.
	 * 
	 * @return the length of the MAC in bytes or 0 for AEAD cipher suites
	 */
	public abstract byte getMacLength();
	
	/**
	 * The length of the MAC key needed by the cipher suite.
	 * 
	 * @return the MAC key length in bytes or 0 for AEAD cipher suites
	 */
	public abstract byte getMacKeyLength();
}
