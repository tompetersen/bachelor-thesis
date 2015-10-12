package jProtocol.tls12.model.values;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;

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
	 * 
	 * @throws TlsDecodeErrorException if the value has no connected content type
	 */
	public static TlsContentType contentTypeFromValue(byte b) throws TlsDecodeErrorException {
		for (TlsContentType t : TlsContentType.values()) {
			if (b == t.getValue()) {
				return t;
			}
		}
		throw new TlsDecodeErrorException("No ContentType for value " + b + "!");
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
