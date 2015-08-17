package jProtocol.tls12.model.values;


public class TlsVersion {
	
	private byte _minorVersion;
	private byte _majorVersion;

	public static TlsVersion getTls12Version() {
		return new TlsVersion((byte) 3,(byte) 3);
	}
	
	public TlsVersion(byte major, byte minor) {
		//TODO: version check!
		if (major != 3 || minor != 3) {
			throw new IllegalArgumentException("Unsupported TLS version [" + major + "," + minor + "]!" );
		}
		
		_majorVersion = major;
		_minorVersion = minor;
	}
	
	public byte getMinorVersion() {
		return _minorVersion;
	}

	public byte getMajorVersion() {
		return _majorVersion;
	}
	
	public byte[] getBytes() {
		byte[] result = {_majorVersion, _minorVersion};
		return result;
	}

	@Override
	public int hashCode() {
		return _majorVersion * 10 + _minorVersion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TlsVersion))
			return false;
		TlsVersion other = (TlsVersion) obj;
		if (_majorVersion != other._majorVersion)
			return false;
		if (_minorVersion != other._minorVersion)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TlsVersion[" + _minorVersion + ", " + _majorVersion + "]";
	}
}