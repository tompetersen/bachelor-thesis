package jProtocol.tls12.model.crypto;

import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsCipherType;

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
public class TlsCipher {

	private String _cipherString;
	private String _algorithmString;
	private Cipher _cipher;
	private TlsCipherType _cipherType;

	public TlsCipher(TlsBulkCipherAlgorithm algorithm, TlsCipherType cipherType) {
		_cipherType = cipherType;
		switch (algorithm) {
		case cipher_null:
			_cipherString = null;
			_algorithmString = null;
			break;
		case cipher_aes:
			//Padding is handled in TlsBlockCipherSuite. SSL3Padding could be used as an alternative!
			_cipherString = "AES/CBC/NoPadding";//supports 128,192,256 bit keys
			_algorithmString = "AES";
			break;
		case cipher_3des:
			//Padding is handled in TlsBlockCipherSuite. SSL3Padding could be used as an alternative!
			_cipherString = "DESede/CBC/NoPadding";
			_algorithmString = "DESede";
			break;
		case cipher_rc4:
			_cipherString = "RC4";
			break;
		}
		
		try {
			if (_cipherString != null) {
				_cipher = Cipher.getInstance(_cipherString);
			} 
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(_cipherString + " algorithm used in TLS 1.2 not found on this machine!");
		}
	}

	public byte[] encrypt(byte[] key, byte[] iv, byte[] plaintext) {
		return performCipherOperation(key, iv, plaintext, Cipher.ENCRYPT_MODE);
	}
	
	public byte[] decrypt(byte[] key, byte[] iv, byte[] ciphertext) {
		return performCipherOperation(key, iv, ciphertext, Cipher.DECRYPT_MODE);
	}
	
	private byte[] performCipherOperation(byte[] key, byte[] iv, byte[] text, int opmode) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key,_algorithmString);
		
		//TODO: stream and aead ciphers?
		AlgorithmParameterSpec algSpec;
		switch (_cipherType) {
		case block:
			algSpec = new IvParameterSpec(iv);
			break;
		default:
			algSpec = null;
			break;
		}
		
		try {
			_cipher.init(opmode, secretKeySpec, algSpec);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Invalid key used for " + _cipherString + "! Reason: " + e.getLocalizedMessage());
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException("Invalid algorithm parameter for " + _cipherString + "! Reason: " + e.getLocalizedMessage());
		}
		
		byte[] result;
		try {
			result = _cipher.doFinal(text);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException("Illegal block size for " + _cipherString + "! Reason: " + e.getLocalizedMessage());
		} catch (BadPaddingException e) {
			throw new RuntimeException("Bad Padding for " + _cipherString + "! Reason: " + e.getLocalizedMessage());
		}
		
		return result;
	}
}
