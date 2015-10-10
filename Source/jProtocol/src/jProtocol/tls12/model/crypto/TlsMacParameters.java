package jProtocol.tls12.model.crypto;


public class TlsMacParameters {
	
	public byte[] macWriteKey;
	public long sequenceNumber;
	public byte contentType;
	public byte versionMajor;
	public byte versionMinor;
	public short length;
	public byte[] fragment;
	
	/**
	 * Creates a parameter object for MAC calculation consisting of needed values.
	 * 
	 * @param macWriteKey the MAC write key
	 * @param sequenceNumber the sewuence number
	 * @param contentType the content type of the message the MAC is computed for
	 * @param versionMajor the major TLS version
	 * @param versionMinor the minor TLS version
	 * @param length the message length
	 * @param fragment the fragment
	 */
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
