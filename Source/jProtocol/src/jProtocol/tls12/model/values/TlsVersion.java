package jProtocol.tls12.model.values;

public class TlsVersion {
	
	private byte _minorVersion;
	private byte _majorVersion;

	/**
	 * Creates a version object for TLS 1.2.
	 * 
	 * @return the TLS 1.2 version
	 */
	public static TlsVersion getTls12Version() {
		return new TlsVersion((byte) 3,(byte) 3);
	}
	
	/**
	 * Creates a (major, minor) version object.
	 * 
	 * @param major the major version part
	 * @param minor the minor version part
	 */
	public TlsVersion(byte major, byte minor) {
//		if (major != 3 || minor != 3) {
//			throw new IllegalArgumentException("Unsupported TLS version [" + major + "," + minor + "]!" );
//		}
		
		_majorVersion = major;
		_minorVersion = minor;
	}
	
	/**
	 * Returns the minor version part.
	 * 
	 * @return the minor version
	 */
	public byte getMinorVersion() {
		return _minorVersion;
	}

	/**
	 * Returns the major version part.
	 * 
	 * @return the major version
	 */
	public byte getMajorVersion() {
		return _majorVersion;
	}
	
	/**
	 * Returns the byte represantation of this version object.
	 * 
	 * @return the bytes
	 */
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
		String result = "[" + _minorVersion + "," + _majorVersion + "]";
		result += (this.equals(getTls12Version()) ? " TLS 1.2" : "");
		
		return result;
	}
}