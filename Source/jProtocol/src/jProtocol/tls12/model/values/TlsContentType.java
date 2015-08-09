package jProtocol.tls12.model.values;

import java.util.HashMap;
import java.util.Map;

public class TlsContentType {
	
	public enum ContentType {
		ChangeCipherSpec,
		Alert,
		Handshake,
		ApplicationData		
	}
	
	private static Map<ContentType, Byte> _types;
	
	static {
		_types = new HashMap<ContentType, Byte>();
		
		_types.put(ContentType.ChangeCipherSpec, 	(byte)20);
		_types.put(ContentType.Alert, 				(byte)21);
		_types.put(ContentType.Handshake, 			(byte)22);
		_types.put(ContentType.ApplicationData, 	(byte)23);
	}
	
	public static ContentType contentTypeFromValue(byte b) {
		for (ContentType t : ContentType.values()) {
			if (b == _types.get(t)) {
				return t;
			}
		}
		throw new IllegalArgumentException("No ContentType for value " + b + "!");
	}
	
	public static byte valueFromContentType(ContentType type) {
		if (type == null) {
			throw new IllegalArgumentException("ContentType must not be null!");
		}
		return _types.get(type);
	}
}
