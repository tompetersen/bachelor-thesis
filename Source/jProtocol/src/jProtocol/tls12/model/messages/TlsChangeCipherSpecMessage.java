package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsContentType;


/*
 *  The ChangeCipherSpec message is sent by both the client and the 
 *  server to notify the receiving party that subsequent records will be 
 *  protected under the newly negotiated CipherSpec and keys.
 *  
 *  See chapter 7.1, p. 27 TLS 1.2
 */
public class TlsChangeCipherSpecMessage extends TlsMessage {

	public TlsChangeCipherSpecMessage() {};
	
	public TlsChangeCipherSpecMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);

		if (unparsedContent.length != 1 || unparsedContent[0] != 1) {
			throw new TlsDecodeErrorException("Invalid change cipher spec body - Should be single byte with value 1!");
		}
	}

	public TlsContentType getContentType() {
		return TlsContentType.ChangeCipherSpec;
	}

	public byte[] getBytes() {
		byte[] messageBytes = {1};
		return messageBytes;
	}

	@Override
	public String toString() {
		return "";
	}
	
}
