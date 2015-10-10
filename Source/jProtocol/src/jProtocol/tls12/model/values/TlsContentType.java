package jProtocol.tls12.model.values;

/**
 * An enumeration of possible TLS content types.
 *  
 * @author Tom Petersen
 */
public enum TlsContentType {
	ChangeCipherSpec	((byte)20),
	Alert				((byte)21),
	Handshake			((byte)22),
	ApplicationData		((byte)23);
	
	/**
	 * Returns a content type for the describing value.
	 * 
	 * @param b the describing value
	 * 
	 * @return the content type
	 */
	public static TlsContentType contentTypeFromValue(byte b) {
		for (TlsContentType t : TlsContentType.values()) {
			if (b == t.getValue()) {
				return t;
			}
		}
		//TODO: Decode error
		throw new IllegalArgumentException("No ContentType for value " + b + "!");
	}
	
	private byte _value;
	
	private TlsContentType(byte value) {
		_value = value;
	}
	
	/**
	 * Returns the describing value of the content type.
	 * 
	 * @return the value
	 */
	public byte getValue() {
		return _value;
	}
}
