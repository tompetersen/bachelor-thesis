package jProtocol.tls12.model.values;

public enum TlsContentType {
		ChangeCipherSpec	((byte)20),
		Alert				((byte)21),
		Handshake			((byte)22),
		ApplicationData		((byte)23);
	
	public static TlsContentType contentTypeFromValue(byte b) {
		for (TlsContentType t : TlsContentType.values()) {
			if (b == t.getValue()) {
				return t;
			}
		}
		throw new IllegalArgumentException("No ContentType for value " + b + "!");
	}
	
	private byte _value;
	
	private TlsContentType(byte value) {
		_value = value;
	}
	
	public byte getValue() {
		return _value;
	}
}
