package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsServerHelloDoneMessage extends TlsHandshakeMessage {
	
	/*
	 *  struct { } ServerHelloDone;
	 */
	
	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.server_hello_done;
	}

	@Override
	public byte[] getBodyBytes() {
		byte[] result = new byte[0];
		return result;
	}

}
