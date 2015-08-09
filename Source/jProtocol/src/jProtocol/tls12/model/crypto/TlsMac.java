package jProtocol.tls12.model.crypto;

import jProtocol.tls12.model.values.TlsMacAlgorithm;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TlsMac {
	
	private String _macString;
	private Mac _mac;

	/**
	 * Creates a MAC object for TLS 1.2 specified MAC algorithms.
	 * 
	 * @param algorithm the algorithm
	 */
	public TlsMac(TlsMacAlgorithm algorithm) {
		switch (algorithm) {
		case mac_null:
			_macString = null;
			break;
		case mac_hmac_md5:
			_macString = "HmacMD5";	
			break;
		case mac_hmac_sha1:
			_macString = "HmacSHA1";
			break;
		case mac_hmac_sha256:
			_macString = "HmacSHA256";
			break;
		case mac_hmac_sha384:
			_macString = "HmacSHA384";
			break;
		case mac_hmac_sha512:
			_macString = "HmacSHA512";
			break;
		}
		
		try {
			if (_macString != null) {
				_mac = Mac.getInstance(_macString);
			} 
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(_macString + " algorithm used in TLS 1.2 not found on this machine!");
		}
	}

	/**
	 * Computes a MAC according to the TLS 1.2 specification.
	 * 
	 * @param macWriteKey the MAC write key	
	 * @param parameters the parameters necessary to compute the TLS MAC
	 * 
	 * @return the MAC
	 */
	public byte[] computeTlsMac(TlsMacParameters parameters) {
		ByteBuffer b = ByteBuffer.allocate(parameters.fragment.length + 13);
		
		b.putLong(parameters.sequenceNumber);	//8
		b.put(parameters.contentType);			//1
		b.put(parameters.versionMajor);			//1
		b.put(parameters.versionMinor);			//1
		b.putShort(parameters.length);			//2
		b.put(parameters.fragment);
		
		return computeMac(parameters.macWriteKey, b.array());
	}
	
	/**
	 * Computes a MAC for the message with the used algorithm.
	 * 
	 * @param secret the secret	used by the MAC function
	 * @param message the message
	 * 
	 * @return the MAC
	 */
	public byte[] computeMac(byte[] secret, byte[] message) {
		if (_macString != null) {
			SecretKeySpec secret_key = new SecretKeySpec(secret, _macString);
			try {
				_mac.init(secret_key);
			} catch (InvalidKeyException e) {
				throw new RuntimeException("Invalid key used for " + _macString + "! Reason: " + e.getLocalizedMessage());
			}
	
			return _mac.doFinal(message);
		}
		else {
			return null;
		}
	}
}
