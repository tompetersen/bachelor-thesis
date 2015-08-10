package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public abstract class TlsClientKeyExchangeMessage extends TlsHandshakeMessage {

	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.client_key_exchange;
	}

}
