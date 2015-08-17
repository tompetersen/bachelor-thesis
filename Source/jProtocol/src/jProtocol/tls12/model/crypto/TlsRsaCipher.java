package jProtocol.tls12.model.crypto;

import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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

	/**
	 * Creates a new rsa cipher object and generates a new key pair (use on
	 * server side).
	 */
	public TlsRsaCipher() {
		createCipher();
		generateKeyPair();
	}

	/**
	 * Creates a new rsa cipher object with just a public key (use on client
	 * side).
	 * Only encrypt should be called.
	 * 
	 * @param encodedPublicKey
	 */
	public TlsRsaCipher(byte[] encodedPublicKey) {
		createCipher();
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
	 * RSA-encrypts the data with the stored public key.
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
	 * RSA-decrypts the data with the stored private key.
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

	/*public void method() {
		// encode
		byte[] publicKey = keypair.getPublic().getEncoded();
		byte[] privateKey = keypair.getPrivate().getEncoded();

		String publicEncodingFormat = keypair.getPublic().getFormat();
		String privateEncodingFormat = keypair.getPrivate().getFormat();
		if (!publicEncodingFormat.equals("X.509")) {
			throw new RuntimeException("Public key encoding is unequal to X.509");
		}
		if (!privateEncodingFormat.equals("PKCS#8")) {
			throw new RuntimeException("Private key encoding is unequal to PKCS#8");
		}

		KeyFactory fact = KeyFactory.getInstance("RSA");

		// decode
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);
		PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKey);
		PublicKey pubKey = fact.generatePublic(pubKeySpec);
		PrivateKey privKey = fact.generatePrivate(privKeySpec);

		 RSAPublicKeySpec pub = fact.getKeySpec(pubKey,
		 RSAPublicKeySpec.class);
		 RSAPrivateKeySpec priv = fact.getKeySpec(keypair.getPrivate(),
		 RSAPrivateKeySpec.class);

		byte[] data = new byte[308]; // {3,1,4,1,5,9,2,6,5};

		// encrypt
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] cipherData = cipher.doFinal(data);

		// decrypt
		Cipher cipher2 = Cipher.getInstance("RSA");
		cipher2.init(Cipher.DECRYPT_MODE, privKey);
		byte[] decryptedData = cipher2.doFinal(cipherData);

		if (data.equals(decryptedData)) {
			throw new RuntimeException("Unequal input and decrypted output!");
		}
		else {
			MyLogger.info("Successfully decrypted!");
		}
	}*/

}
