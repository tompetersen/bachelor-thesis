package jProtocol.tls12.model.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Cipher Suite string according to:
 * http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher
 * 
 * Other useful material:
 * http://www.javamex.com/tutorials/cryptography/block_modes_java.shtml
 * 
 */
public class TlsAesCbcCipher {
	private Cipher _cipher;

	public TlsAesCbcCipher() {
		try {
			_cipher = Cipher.getInstance("AES/CBC/NoPadding");
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("[AES/CBC/NoPadding] algorithm used in TLS 1.2 not found on this machine!");
		}
	}

	public byte[] encrypt(byte[] key, byte[] iv, byte[] plaintext) {
		return performCipherOperation(key, iv, plaintext, Cipher.ENCRYPT_MODE);
	}
	
	public byte[] decrypt(byte[] key, byte[] iv, byte[] ciphertext) {
		return performCipherOperation(key, iv, ciphertext, Cipher.DECRYPT_MODE);
	}
	
	private byte[] performCipherOperation(byte[] key, byte[] iv, byte[] text, int opmode) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
		
		AlgorithmParameterSpec algSpec = new IvParameterSpec(iv);
		
		try {
			_cipher.init(opmode, secretKeySpec, algSpec);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Invalid key used for [AES/CBC/NoPadding]! Reason: " + e.getLocalizedMessage());
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException("Invalid algorithm parameter for [AES/CBC/NoPadding]! Reason: " + e.getLocalizedMessage());
		}
		
		byte[] result;
		try {
			result = _cipher.doFinal(text);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException("Illegal block size for [AES/CBC/NoPadding]! Reason: " + e.getLocalizedMessage());
		} catch (BadPaddingException e) {
			throw new RuntimeException("Bad Padding for [AES/CBC/NoPadding]! Reason: " + e.getLocalizedMessage());
		}
		
		return result;
	}
}
