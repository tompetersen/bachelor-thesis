package jProtocol.tls12.model.crypto;

import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Cipher Suite string according to:
 * http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher
 * 
 * Other useful material:
 * http://www.javamex.com/tutorials/cryptography/block_modes_java.shtml
 * 
 */
public class TlsAesGcmCipher {
	
	/*
	 * RFC 5166
	 * An authentication tag with a length of 16 octets (128 bits) is used.
	 */
	public static final int GCM_TAG_LENGTH = 16; // in bytes
	
	private Cipher _cipher;

	public TlsAesGcmCipher() {
		try {
			_cipher = Cipher.getInstance("AES/GCM/NoPadding");
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("[AES/GCM/NoPadding] algorithm used in TLS 1.2 not found on this machine!");
		}
	}

	/**
	 * Encryption operation for AES 128 GCM.
	 * Note: An AEAD_AES_128_GCM ciphertext is exactly 16 octets longer than its corresponding plaintext.
	 * 
	 * @param key the encryption key
	 * @param nonce the nonce
	 * @param additionalData optional additional data
	 * @param plaintext the plaintext
	 * 
	 * @return the ciphertext
	 */
	public byte[] encrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] plaintext) {
		try {
			return performCipherOperation(key, nonce, additionalData, plaintext, Cipher.ENCRYPT_MODE);
		}
		catch (TlsBadRecordMacException e) {
			throw new RuntimeException("AEADBadTagException thrown in AES GCM encrypt mode. Wait... what?");
		}
	}
	
	public byte[] decrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] ciphertext) throws TlsBadRecordMacException {
		return performCipherOperation(key, nonce, additionalData, ciphertext, Cipher.DECRYPT_MODE);
	}
	
	private byte[] performCipherOperation(byte[] key, byte[] nonce, byte[] additionalData, byte[] text, int opmode) throws TlsBadRecordMacException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
		
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
		
		try {
			_cipher.init(opmode, secretKeySpec, spec);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Invalid key used for [AES/GCM/NoPadding]! Reason: " + e.getLocalizedMessage());
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException("Invalid algorithm parameter for [AES/GCM/NoPadding]! Reason: " + e.getLocalizedMessage());
		}
		
		_cipher.updateAAD(additionalData);
		
		byte[] result;
		try {
			result = _cipher.doFinal(text);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException("Illegal block size for [AES/GCM/NoPadding]! Reason: " + e.getLocalizedMessage());
		} catch (AEADBadTagException e) {
			throw new TlsBadRecordMacException("Bad authentication tag for [AES/GCM/NoPadding]!");
		} catch (BadPaddingException e) {
			throw new RuntimeException("Bad Padding for [AES/GCM/NoPadding]! Reason: " + e.getLocalizedMessage());
		}
		
		return result;
	}
}
