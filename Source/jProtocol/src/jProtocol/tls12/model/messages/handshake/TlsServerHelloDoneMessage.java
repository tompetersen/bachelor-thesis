package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsServerHelloDoneMessage extends TlsHandshakeMessage {
	
	/*
	 *  struct { } ServerHelloDone;
	 */
	
	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.server_hello_done;
	}

	@Override
	public byte[] getBodyBytes() {
		byte[] result = new byte[0];
		return result;
	}

}
