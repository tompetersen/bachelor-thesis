package jProtocol.tls12.model.values;

import java.util.HashMap;
import java.util.Map;

public class TlsHandshakeType {

	public enum HandshakeType {
		hello_request,
		client_hello,
		server_hello,
		certificate,
		server_key_exchange,
		certificate_request,
		server_hello_done,
		certificate_verify,
		client_key_exchange,
		finished
	}
	
	private static Map<HandshakeType, Byte> _types;
	
	static {
		_types = new HashMap<HandshakeType, Byte>();
		
		_types.put(HandshakeType.hello_request,			(byte)0);
		_types.put(HandshakeType.client_hello,			(byte)1);
		_types.put(HandshakeType.server_hello,			(byte)2);
		_types.put(HandshakeType.certificate,			(byte)11);
		_types.put(HandshakeType.server_key_exchange,	(byte)12);
		_types.put(HandshakeType.certificate_request,	(byte)13);
		_types.put(HandshakeType.server_hello_done,		(byte)14);
		_types.put(HandshakeType.certificate_verify,	(byte)15);
		_types.put(HandshakeType.client_key_exchange,	(byte)16);
		_types.put(HandshakeType.finished,				(byte)20);
	}
	
	public static HandshakeType handshakeTypeFromValue(byte b) {
		for (HandshakeType t : HandshakeType.values()) {
			if (b == _types.get(t)) {
				return t;
			}
		}
		throw new IllegalArgumentException("No HandshakeType for value " + b + "!");
	}
	
	public static byte valueFromHandshakeType(HandshakeType type) {
		if (type == null) {
			throw new IllegalArgumentException("HandshakeType must not be null!");
		}
		return _types.get(type);
	}
}
