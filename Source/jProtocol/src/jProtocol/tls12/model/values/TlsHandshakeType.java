package jProtocol.tls12.model.values;

import java.util.HashMap;
import java.util.Map;

public enum TlsHandshakeType {
		hello_request,
		client_hello,
		server_hello,
		certificate,
		server_key_exchange,
		certificate_request,
		server_hello_done,
		certificate_verify,
		client_key_exchange,
		finished;
	
	private static Map<TlsHandshakeType, Byte> _types;
	
	static {
		_types = new HashMap<TlsHandshakeType, Byte>();
		
		_types.put(TlsHandshakeType.hello_request,			(byte)0);
		_types.put(TlsHandshakeType.client_hello,			(byte)1);
		_types.put(TlsHandshakeType.server_hello,			(byte)2);
		_types.put(TlsHandshakeType.certificate,			(byte)11);
		_types.put(TlsHandshakeType.server_key_exchange,	(byte)12);
		_types.put(TlsHandshakeType.certificate_request,	(byte)13);
		_types.put(TlsHandshakeType.server_hello_done,		(byte)14);
		_types.put(TlsHandshakeType.certificate_verify,	(byte)15);
		_types.put(TlsHandshakeType.client_key_exchange,	(byte)16);
		_types.put(TlsHandshakeType.finished,				(byte)20);
	}
	
	public static TlsHandshakeType handshakeTypeFromValue(byte b) {
		for (TlsHandshakeType t : TlsHandshakeType.values()) {
			if (b == _types.get(t)) {
				return t;
			}
		}
		throw new IllegalArgumentException("No HandshakeType for value " + b + "!");
	}
	
	public byte getValue() {
		return _types.get(this);
	}
}
