package jProtocol.tls12.model.crypto;

import jProtocol.helper.ByteHelper;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TlsPseudoRandomFunction {
	
	/*
	 * See chapter 5 of TLS 1.2 specification.
	 * 
	 * In this section, we define one PRF, based on HMAC.  This PRF with the
	 SHA-256 hash function is used for all cipher suites defined in this
	 document and in TLS documents published prior to this document when
	 TLS 1.2 is negotiated.  New cipher suites MUST explicitly specify a
	 PRF and, in general, SHOULD use the TLS PRF with SHA-256 or a
	 stronger standard hash function.
	 */
	
	/**
	 * Computes random bytes according to the TLS 1.2PRF-SHA256 construction. 
	 * 
	 * @param secret the secret used for the computation
	 * @param label a computation describing label
	 * @param seed a seed for the computation
	 * @param neededBytes the number of bytes needed
	 * 
	 * @return neededBytes random Bytes
	 */
	public static byte[] prf(byte[] secret, String label, byte[] seed, int neededBytes) {
		if (label == null || secret == null || seed == null) {
			throw new IllegalArgumentException("Parameters for prf must not be null!");
		}
		
		return p_sha256(secret, 
				ByteHelper.concatenate(label.getBytes(StandardCharsets.US_ASCII), seed), 
						neededBytes);
	}

	private static byte[] p_sha256(byte[] secret, byte[] seed, int neededBytes) {
		byte[] result = new byte[neededBytes];
		int computedBytes = 0;
		byte[] A = seed;
		
		while (computedBytes < neededBytes) {
			A = hmac_sha256(secret, A);
			byte[] aAndSeed = ByteHelper.concatenate(A, seed);
			
			byte[] step = hmac_sha256(secret, aAndSeed);
			
			int bytesLeft = neededBytes - computedBytes;
			int bytesToCopy = Math.min(bytesLeft, step.length);
			System.arraycopy(step, 0, result, computedBytes, bytesToCopy);
			computedBytes += bytesToCopy;
		}
		
		return result;
	}
	
	private static byte[] hmac_sha256(byte[] key, byte[] data) {
		Mac sha256_HMAC;
		try {
			sha256_HMAC = Mac.getInstance("HmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("HmacSHA256 used in TLS 1.2 not found on this machine!");
		}
		
		SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
		try {
			sha256_HMAC.init(secret_key);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Invalid key used for HmacSHA256! Reason: " + e.getLocalizedMessage());
		}

		return sha256_HMAC.doFinal(data);
	}
}
