package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.values.TlsContentType.ContentType;

public abstract class TlsHandshakeMessage implements TlsMessage {

	@Override
	public ContentType getContentType() {
		return ContentType.Handshake;
	}
	
}
