package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.values.TlsContentType;

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
		if (content == null) {
			throw new IllegalArgumentException("Alert description must not be null!");
		}
		_content = content;
	}
	
	@Override
	public TlsContentType getContentType() {
		return TlsContentType.ApplicationData;
	}

	@Override
	public byte[] getBytes() {
		return _content;
	}
}
