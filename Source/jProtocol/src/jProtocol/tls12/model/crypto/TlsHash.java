package jProtocol.tls12.model.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TlsHash {

	private MessageDigest _digest; 
	
	public TlsHash() {
		try {
			_digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 not supported on this platform!");
		}
	}

	public byte[] hash(byte[] input) {
		_digest.update(input);
		return _digest.digest();
	}
}
