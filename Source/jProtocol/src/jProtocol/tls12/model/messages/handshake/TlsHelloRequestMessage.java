package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsHelloRequestMessage extends TlsHandshakeMessage {

	/*
	 *  struct { } HelloRequest;
	 */
	
	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.hello_request;
	}

	@Override
	public byte[] getBodyBytes() {
		byte[] result = new byte[0];
		return result;
	}

}
