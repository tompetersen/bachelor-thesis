package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsHelloRequestMessage extends TlsHandshakeMessage {

	/*
	 *  struct { } HelloRequest;
	 */
	
	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.hello_request;
	}

	@Override
	public byte[] getBodyBytes() {
		byte[] result = new byte[0];
		return result;
	}

}
