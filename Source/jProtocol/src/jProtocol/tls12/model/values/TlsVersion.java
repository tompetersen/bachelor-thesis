package jProtocol.tls12.model.values;

public class TlsVersion {
	
	public byte minorVersion;
	public byte majorVersion;

	public TlsVersion(byte major, byte minor) {
		this.majorVersion = major;
		this.minorVersion = minor;
	}
}