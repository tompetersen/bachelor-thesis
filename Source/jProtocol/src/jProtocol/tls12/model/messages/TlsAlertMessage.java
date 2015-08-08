package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.TlsContentType.ContentType;

public class TlsAlertMessage extends TlsMessage {

	@Override
	public ContentType getContentType() {
		return ContentType.Alert;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
