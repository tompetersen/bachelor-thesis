package jProtocol.tls12.model.values;

public class TlsVersion {
	
	private byte _minorVersion;
	private byte _majorVersion;

	public static TlsVersion getTls12Version() {
		return new TlsVersion((byte) 3,(byte) 3);
	}
	
	private TlsVersion(byte major, byte minor) {
		_majorVersion = major;
		_minorVersion = minor;
	}
	
	public byte getMinorVersion() {
		return _minorVersion;
	}

	public byte getMajorVersion() {
		return _majorVersion;
	}
}