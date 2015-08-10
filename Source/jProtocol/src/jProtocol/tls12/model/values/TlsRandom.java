package jProtocol.tls12.model.values;

import jProtocol.helper.ByteHelper;

public class TlsRandom {

	private byte[] _gmt;
	private byte[] _random;
	
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

}
