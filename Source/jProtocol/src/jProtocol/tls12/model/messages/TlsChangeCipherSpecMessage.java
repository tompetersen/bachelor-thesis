package jProtocol.tls12.model.messages;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsContentType;
import java.util.ArrayList;


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

	@Override
	public KeyValueObject getViewData() {
		ArrayList<KeyValueObject> children = new ArrayList<KeyValueObject>();
		
		children.add(new KeyValueObject("Value", "1"));
		
		KeyValueObject result = new KeyValueObject("Content", children);
		result.setValue("TlsChangeCipherSpec");
		result.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/TLS12_ChangeCipherSpec.html"));
		
		return result;
	}
}
