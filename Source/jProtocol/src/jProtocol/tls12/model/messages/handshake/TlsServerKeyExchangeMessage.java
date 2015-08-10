package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsServerKeyExchangeMessage extends TlsHandshakeMessage {

	//TODO: Used for DHE -> Implement when necessary
	public TlsServerKeyExchangeMessage() {
	}

	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.server_key_exchange;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
