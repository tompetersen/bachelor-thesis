package jProtocol.tls12.model.values;

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

	/**
	 * Returns the unix time describing bytes.
	 * 
	 * @return the unix time bytes
	 */
	public byte[] getGmtUnixTime() {
		return _gmt;
	}
	
	/**
	 * Returns the random bytes (not including the time bytes). 
	 * 
	 * @return the random bytes
	 */
	public byte[] getRandom() {
		return _random;
	}
	
	/**
	 * Returns all bytes of this random object.
	 * 
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return ByteHelper.concatenate(_gmt, _random);
	}
	
	@Override
	public String toString() {
		return "0x " + ByteHelper.bytesToHexString(_gmt) + " " + ByteHelper.bytesToHexString(_random);
	}

}
