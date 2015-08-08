package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.TlsContentType;
import jProtocol.tls12.model.TlsContentType.ContentType;

public abstract class TlsMessage {
	
	public abstract ContentType getContentType();
	public abstract byte[] getBytes();
	
}
