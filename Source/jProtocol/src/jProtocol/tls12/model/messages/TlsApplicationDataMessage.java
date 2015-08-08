package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.TlsContentType.ContentType;

public class TlsApplicationDataMessage extends TlsMessage {

	@Override
	public ContentType getContentType() {
		return ContentType.ApplicationData;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
