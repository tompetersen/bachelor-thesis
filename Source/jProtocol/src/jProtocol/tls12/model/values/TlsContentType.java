package jProtocol.tls12.model.values;

import java.util.HashMap;
import java.util.Map;

public enum TlsContentType {
		ChangeCipherSpec,
		Alert,
		Handshake,
		ApplicationData;
	
	private static Map<TlsContentType, Byte> _types;
	
	static {
		_types = new HashMap<TlsContentType, Byte>();
		
		_types.put(TlsContentType.ChangeCipherSpec, 	(byte)20);
		_types.put(TlsContentType.Alert, 				(byte)21);
		_types.put(TlsContentType.Handshake, 			(byte)22);
		_types.put(TlsContentType.ApplicationData, 	(byte)23);
	}
	
	public static TlsContentType contentTypeFromValue(byte b) {
		for (TlsContentType t : TlsContentType.values()) {
			if (b == _types.get(t)) {
				return t;
			}
		}
		throw new IllegalArgumentException("No ContentType for value " + b + "!");
	}
	
	public byte getValue() {
		return _types.get(this);
	}
}
