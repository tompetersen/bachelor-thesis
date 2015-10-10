package jProtocol.tls12.model.crypto;

import java.security.SecureRandom;

public class TlsPseudoRandomNumberGenerator {
	
	private static SecureRandom _random = new SecureRandom();
	
	/**
	 * Computes a number of pseudo random bytes.
	 * 
	 * @param number the number of needed bytes
	 * 
	 * @return the random bytes
	 */
	public static byte[] nextBytes(int number) {
		byte[] result = new byte[number];
		_random.nextBytes(result);
		return result;
	}
	
	/**
	 * Returns the Secure random object.
	 * 
	 * @return the random object
	 */
	public static SecureRandom getRandom() {
		return _random;
	}
}
