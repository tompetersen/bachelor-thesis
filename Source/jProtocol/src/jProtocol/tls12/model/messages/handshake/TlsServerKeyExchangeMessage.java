package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsServerKeyExchangeMessage extends TlsHandshakeMessage {

	//TODO: Used for DHE -> Implement when necessary
	public TlsServerKeyExchangeMessage() {
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.server_key_exchange;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
