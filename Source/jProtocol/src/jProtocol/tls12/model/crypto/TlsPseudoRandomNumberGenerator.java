package jProtocol.tls12.model.crypto;

import java.security.SecureRandom;

public class TlsPseudoRandomNumberGenerator {
	
	private static SecureRandom _random = new SecureRandom();
	
	public static byte[] nextBytes(int number) {
		byte[] result = new byte[number];
		_random.nextBytes(result);
		return result;
	}
	
	public static SecureRandom getRandom() {
		return _random;
	}
}
