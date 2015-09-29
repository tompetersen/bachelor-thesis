package jProtocol.tls12.model.crypto;

import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class TlsRsaCipher {
	/*
	 * http://www.javamex.com/tutorials/cryptography/rsa_encryption.shtml
	 * 
	 * http://www.java2s.com/Tutorial/Java/
	 * 0490__Security/DiffieHellmanKeyAgreement.htm
	 * http://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/
	 * CryptoSpec.html#DH2Ex
	 */
	private static final int KEY_SIZE = 1024;
	// Key size -> max. data size
	// 512 -> 53
	// 1024 -> 117
	// 2048 -> 245

	private PrivateKey _privateKey;
	private PublicKey _publicKey;
	private Cipher _cipher;
	private Signature _signature;

	/**
	 * Creates a new RSA cipher object and generates a new key pair (use on
	 * server side).
	 */
	public TlsRsaCipher() {
		createCipher();
		createSignature();
		generateKeyPair();
	}

	/**
	 * Creates a new RSA cipher object with just a public key (use on client
	 * side).
	 * Only encrypt should be called.
	 * 
	 * @param encodedPublicKey 
	 */
	public TlsRsaCipher(byte[] encodedPublicKey) {
		createCipher();
		createSignature();
		
		try {
			createPublicKey(encodedPublicKey);
		}
		catch (InvalidKeySpecException e) {
			throw new IllegalArgumentException("Invalid encoded RSA public key!");
		}
	}

	private void createCipher() {
		try {
			_cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("Cipher for RSA not found on this machine!");
		}
	}
	
	private void createSignature() {
		try {
			_signature = Signature.getInstance("SHA1WithRSA");
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Signature for SHA1WithRSA not found on this machine!");
		}
	}

	private void generateKeyPair() {
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("KeyPairGenerator for RSA not found on this machine!");
		}

		keyGen.initialize(KEY_SIZE);
		KeyPair keypair = keyGen.genKeyPair();

		_privateKey = keypair.getPrivate();
		_publicKey = keypair.getPublic();
	}

	private void createPublicKey(byte[] publicKeyBytes) throws InvalidKeySpecException {
		KeyFactory fact;
		try {
			fact = KeyFactory.getInstance("RSA");
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("KeyFactory for RSA not found on this machine!");
		}

		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		_publicKey = fact.generatePublic(pubKeySpec);
	}

	/**
	 * Returns the stored public key in encoded format (probably X.509).
	 * 
	 * @return the encoded public key
	 */
	public byte[] getEncodedPublicKey() {
		return _publicKey.getEncoded();
	}

	/**
	 * RSA encrypts the data with the stored public key.
	 * 
	 * @param data the data
	 * 
	 * @return the encrypted data
	 * 
	 * @throws TlsAsymmetricOperationException if the RSA encryption fails
	 */
	public byte[] encrypt(byte[] data) throws TlsAsymmetricOperationException {
		byte[] encryptedData = null;

		try {
			_cipher.init(Cipher.ENCRYPT_MODE, _publicKey);
			encryptedData = _cipher.doFinal(data);
		}
		catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new TlsAsymmetricOperationException("Public key encryption failed: " + e.getLocalizedMessage());
		}

		return encryptedData;
	}

	/**
	 * RSA decrypts the data with the stored private key.
	 * 
	 * @param encryptedData the encrypted data
	 * 
	 * @return the decrypted data
	 * 
	 * @throws TlsAsymmetricOperationException if the RSA decryption fails
	 */
	public byte[] decrypt(byte[] encryptedData) throws TlsAsymmetricOperationException {
		if (_privateKey == null) {
			throw new RuntimeException("Private Key in RSA cipher has not been set (TLS client mode).");
		}
		
		byte[] decryptedData;
		
		try {
			_cipher.init(Cipher.DECRYPT_MODE, _privateKey);
			decryptedData = _cipher.doFinal(encryptedData);
		}
		catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			throw new TlsAsymmetricOperationException("Private key decryption failed: " + e.getLocalizedMessage());
		}

		return decryptedData;
	}
	
	/* Signature:
	 * 
	 * http://www.java2s.com/Tutorial/Java/0490__Security/SimpleDigitalSignatureExample.htm 
	 */
	
	/**
	 * Computes a SHA1 RSA signature with the private key of this cipher.
	 * 
	 * @param dataToSign the data to be signed
	 * 
	 * @return the signature 
	 * 
	 * @throws TlsAsymmetricOperationException if an invalid key was provided or the signing process failed
	 */
	public byte[] signData(byte[] dataToSign) throws TlsAsymmetricOperationException {
		if (_privateKey == null) {
			throw new RuntimeException("Private Key in RSA cipher has not been set (TLS client mode).");
		}
		
		try {
		    _signature.initSign(_privateKey);
		    _signature.update(dataToSign);
	    
			return _signature.sign();
		}
		catch (InvalidKeyException | SignatureException e) {
			throw new TlsAsymmetricOperationException("Private key decryption failed: " + e.getLocalizedMessage());
		}
	}
	
	/**
	 * Checks a given signature for some data with the public key of this cipher.
	 * 
	 * @param data the data which was signed
	 * @param signature the computed signature
	 * 
	 * @return true if the signature can be verified, false otherwise
	 * 
	 * @throws TlsAsymmetricOperationException if an invalid key was provided or the verifying process failed
	 */
	public boolean checkSignature(byte[] data, byte[] signature) throws TlsAsymmetricOperationException {
		try {
			_signature.initVerify(_publicKey);
			_signature.update(data);
			return _signature.verify(signature);
		}
		catch (InvalidKeyException | SignatureException e) {
			throw new TlsAsymmetricOperationException("Private key decryption failed: " + e.getLocalizedMessage());
		}
	}
}
