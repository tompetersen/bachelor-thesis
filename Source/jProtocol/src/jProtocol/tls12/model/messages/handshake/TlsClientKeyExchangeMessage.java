package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType;

public abstract class TlsClientKeyExchangeMessage extends TlsHandshakeMessage {

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.client_key_exchange;
	}

}
