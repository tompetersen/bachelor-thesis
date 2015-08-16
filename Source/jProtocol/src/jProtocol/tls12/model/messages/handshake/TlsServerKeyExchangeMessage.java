package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsServerKeyExchangeMessage extends TlsHandshakeMessage {

	//TODO: Used for DHE -> Implement when necessary
	public TlsServerKeyExchangeMessage() {
	}

	public TlsServerKeyExchangeMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO Parsing
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
