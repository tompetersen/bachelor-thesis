package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsMacAlgorithm;

/*
 * Information can be found in 
 * - appendix A.5. (cipher suite codes and stuff)
 * - appendix C (key and other sizes)
 */
public interface TlsCipherSuite {
	
	/**
	 * Transforms a TLSPlaintext to a TLSCiphertext. The Message will be MACed and 
	 * encrypted according to the used ciphersuite.
	 * 
	 * @param plaintext the TLSPlaintext
	 * @param parameters the encryption parameters used to encrypt
	 * 
	 * @return the TLSCiphertext
	 */
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext, TlsEncryptionParameters parameters);
	
	/**
	 * Transforms a TLSCiphertext to a TLSPlaintext. The Message will be decrypted and 
	 * the MAC will be checked according to the used ciphersuite.
	 * 
	 * @param ciphertext the TLSCiphertext
	 * @param parameters the encryption parameters used to decrypt
	 * @param registry the cipher suite registry
	 * @param algorithm the used key exchange algorithm
	 * 
	 * @return the TlsPlaintext
	 *
	 * @throws TlsBadRecordMacException if the message has an invalid MAC
	 * @throws TlsBadPaddingException if decryption of the messages fails beacuse of invalid padding
	 * @throws TlsDecodeErrorException if the message itself can not be decoded properly
	 */
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry, TlsKeyExchangeAlgorithm algorithm) 
			throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException;
	
	/**
	 * The name equal to the cipher suite name in TLS 1.2 specification.
	 * 
	 * @return the cipher suite name
	 */
	public String getName();
	
	/**
	 * The two byte code used in handshake messages.
	 * 
	 * @return the code
	 */
	public short getCode();
	
	/**
	 * The cipher type (stream, block or aead) of the cipher suite.
	 * 
	 * @return the cipher type
	 */
	public TlsCipherType getCipherType();
	
	/**
	 * The algorithm (null, rc4, 3des, aes) used for encryption.
	 * 
	 * @return the encryption algorithm
	 */
	public TlsBulkCipherAlgorithm getBulkCipherAlgorithm();
	
	/**
	 * The length of the encryption key used by the cipher suite.
	 * 
	 * @return the encryption key length in bytes
	 */
	public byte getEncryptKeyLength();
	
	/**
	 * The block length of the encryption algorithm.
	 * 
	 * @return the block length in bytes for block or AEAD cipher suites or 0 for stream cipher suites
	 */
	public byte getBlockLength();
	
	/**
	 * The fixed initialization vector length used by AEAD ciphers for implicit nonce.
	 * 
	 * @return the length in bytes for AEAD ciphers or 0 for block and stream cipher suites
	 */
	public byte getFixedIvLength();
	
	/**
	 * The record initialization vector length used by block ciphers.
	 * 
	 * @return the length in bytes for block ciphers or 0 for stream and AEAD cipher suites
	 */
	public byte getRecordIvLength();
	
	/**
	 * The MAC algorithm (null or hmac with md5, sha1, sha256, ...) used by the cipher suite.
	 * 
	 * @return the MAC algorithm
	 */
	public TlsMacAlgorithm getMacAlgorithm();
	
	/**
	 * The MAC length of the cipher suite.
	 * 
	 * @return the length of the MAC in bytes or 0 for AEAD cipher suites
	 */
	public byte getMacLength();
	
	/**
	 * The length of the MAC key needed by the cipher suite.
	 * 
	 * @return the MAC key length in bytes or 0 for AEAD cipher suites
	 */
	public byte getMacKeyLength();
	
	/**
	 * The key exchange algorithm (dhe_dss, dhe_rsa, dh_anon, rsa, dh_dss, dh_rsa) used in the cipher suite. 
	 * 
	 * @return the key exchange algorithm
	 */
	public TlsKeyExchangeAlgorithm getKeyExchangeAlgorithm();
}
