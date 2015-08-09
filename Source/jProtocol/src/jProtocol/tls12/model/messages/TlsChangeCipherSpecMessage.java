package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.values.TlsContentType.ContentType;


/*
 *  The ChangeCipherSpec message is sent by both the client and the 
 *  server to notify the receiving party that subsequent records will be 
 *  protected under the newly negotiated CipherSpec and keys.
 *  
 *  See chapter 7.1, p. 27 TLS 1.2
 */
public class TlsChangeCipherSpecMessage implements TlsMessage {

	@Override
	public ContentType getContentType() {
		return ContentType.ChangeCipherSpec;
	}

	@Override
	public byte[] getBytes() {
		byte[] messageBytes = {1};
		return messageBytes;
	}
	
}
