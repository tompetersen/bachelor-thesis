package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.TlsMessage;
import jProtocol.tls12.model.TlsContentType.ContentType;

public class TlsHandshakeMessage extends TlsMessage {

	@Override
	public ContentType getContentType() {
		return ContentType.Handshake;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
