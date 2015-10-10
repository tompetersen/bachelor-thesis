package jProtocol.tls12.model.values;

/**
 * An enumeration of possible TLS handshake types.
 *  
 * @author Tom Petersen
 */
public enum TlsHandshakeType {
	hello_request 			((byte)0),
	client_hello 			((byte)1),
	server_hello 			((byte)2),
	certificate 			((byte)11),
	server_key_exchange		((byte)12),
	certificate_request 	((byte)13),
	server_hello_done 		((byte)14),
	certificate_verify 		((byte)15),
	client_key_exchange 	((byte)16),
	finished 				((byte)20);
	
	/**
	 * Returns a handshake type from the describing value.
	 * 
	 * @param b the value
	 * 
	 * @return the handshake type
	 */
	public static TlsHandshakeType handshakeTypeFromValue(byte b) {
		for (TlsHandshakeType t : TlsHandshakeType.values()) {
			if (b == t.getValue()) {
				return t;
			}
		}
		//TODO: decode error
		throw new IllegalArgumentException("No HandshakeType for value " + b + "!");
	}
	
	private byte _value;
	
	private TlsHandshakeType(byte value) {
		_value = value;
	}
	
	/**
	 * The describing value of the handshake type.
	 * 
	 * @return the value
	 */
	public byte getValue() {
		return _value;
	}
}
