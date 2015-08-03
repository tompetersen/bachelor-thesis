package jProtocol.tls12.model;

import jProtocol.tls12.model.TlsContentType.ContentType;

public abstract class TlsMessage {
	
	public abstract ContentType getContentType();
	public abstract byte[] getBytes();
	
}
