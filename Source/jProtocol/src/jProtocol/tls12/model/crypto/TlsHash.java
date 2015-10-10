package jProtocol.tls12.model.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TlsHash {

	private MessageDigest _digest; 
	
	/**
	 * Creates a object offering hash calculations with SHA256.
	 */
	public TlsHash() {
		try {
			_digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 not supported on this platform!");
		}
	}

	/**
	 * Computes the hash value for the input bytes.
	 * 
	 * @param input the input
	 * 
	 * @return the hash
	 */
	public byte[] hash(byte[] input) {
		_digest.update(input);
		return _digest.digest();
	}
}
