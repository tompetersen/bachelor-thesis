package jProtocol.tls12.model.values;

import java.util.Arrays;
import jProtocol.helper.ByteHelper;

public class TlsRandom {

	private byte[] _gmt;
	private byte[] _random;
	
	/**
	 * Creates a TlsRandom object with the current time set as gmtUnixTime.
	 * 
	 * @param random 28 random bytes
	 */
	public TlsRandom(byte[] random) {
		this((int) (System.currentTimeMillis() / 1000L), random);
	}
	
	/**
	 * Creates a TlsRandom object from time and random value.
	 * 
	 * @param gmtUnixTime the time in standard UNIX format (seconds since 1.1.1970). TLS does not require correctly set clocks.
	 * @param random 28 random bytes
	 */
	public TlsRandom(int gmtUnixTime, byte[] random) {
		if (random == null || random.length != 28) {
			throw new IllegalArgumentException("Random.random must be 28 bytes long!");
		}
		
		_gmt = ByteHelper.intToByteArray(gmtUnixTime);
		_random = random;
	}

	public byte[] getGmtUnixTime() {
		return _gmt;
	}

	public byte[] getRandom() {
		return _random;
	}
	
	public byte[] getBytes() {
		return ByteHelper.concatenate(_gmt, _random);
	}
	
	@Override
	public String toString() {
		return "TlsRandom [gmt=" + Arrays.toString(_gmt) + ", random=" + Arrays.toString(_random) + "]";
	}

}
