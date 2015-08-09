package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.values.TlsContentType.ContentType;

public interface TlsMessage {
	
	public ContentType getContentType();
	
	public byte[] getBytes();
	
}
