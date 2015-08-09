package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.values.TlsContentType.ContentType;

/*
 *  Application data messages are carried by the record layer and are 
 *  fragmented, compressed, and encrypted based on the current connection 
 *  state. The messages are treated as transparent data to the record 
 *  layer.
 *  
 *  See chapter 10, p. 65 TLS 1.2
 */
public class TlsApplicationDataMessage implements TlsMessage {

	private byte[] _content;
	
	public TlsApplicationDataMessage(byte[] content) {
		_content = content;
	}
	
	@Override
	public ContentType getContentType() {
		return ContentType.ApplicationData;
	}

	@Override
	public byte[] getBytes() {
		return _content;
	}
	
}
