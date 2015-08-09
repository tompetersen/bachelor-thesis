package jProtocol.tls12.model.crypto;


public class TlsMacParameters {
	public byte[] macWriteKey;
	public long sequenceNumber;
	public byte contentType;
	public byte versionMajor;
	public byte versionMinor;
	public short length;
	public byte[] fragment;
	
	public TlsMacParameters(byte[] macWriteKey, long sequenceNumber, byte contentType, byte versionMajor, byte versionMinor, short length, byte[] fragment) {
		this.macWriteKey = macWriteKey;
		this.sequenceNumber = sequenceNumber;
		this.contentType = contentType;
		this.versionMajor = versionMajor;
		this.versionMinor = versionMinor;
		this.length = length;
		this.fragment = fragment;
	}
}
