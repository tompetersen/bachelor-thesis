package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.TlsContentType.ContentType;
import jProtocol.tls12.model.TlsMessage;

public class TlsChangeCipherSpecMessage extends TlsMessage {

	@Override
	public ContentType getContentType() {
		return ContentType.ChangeCipherSpec;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
