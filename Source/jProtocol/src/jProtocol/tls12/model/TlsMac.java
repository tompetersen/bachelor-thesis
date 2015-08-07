package jProtocol.tls12.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jProtocol.tls12.model.TlsSecurityParameters.MacAlgorithm;

public class TlsMac {
	
	private String _macString;
	private Mac _mac;

	public TlsMac(MacAlgorithm algorithm) {
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

	public byte[] computeMac(byte[] key, byte[] message) {
		if (_macString != null) {
			SecretKeySpec secret_key = new SecretKeySpec(key, _macString);
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
