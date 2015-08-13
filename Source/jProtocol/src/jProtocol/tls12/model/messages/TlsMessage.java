package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.values.TlsContentType;

public interface TlsMessage {
	
	public TlsContentType getContentType();
	
	public byte[] getBytes();
	
}
