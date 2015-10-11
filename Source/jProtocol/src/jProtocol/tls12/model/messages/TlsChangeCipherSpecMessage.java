package jProtocol.tls12.model.messages;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsContentType;
import java.util.ArrayList;

public class TlsChangeCipherSpecMessage extends TlsMessage {

	/*
	 *  The ChangeCipherSpec message is sent by both the client and the 
	 *  server to notify the receiving party that subsequent records will be 
	 *  protected under the newly negotiated CipherSpec and keys.
	 *  
	 *  See chapter 7.1, p. 27 TLS 1.2
	 */
	/**
	 * Default constructor.
	 */
	public TlsChangeCipherSpecMessage() {};
	
	/**
	 * Creates a change cipher spec message by parsing sent bytes.
	 * 
	 * @param unparsedContent the sent bytes
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsChangeCipherSpecMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);

		if (unparsedContent.length != 1 || unparsedContent[0] != 1) {
			throw new TlsDecodeErrorException("Invalid change cipher spec body - Should be single byte with value 1!");
		}
	}

	@Override
	public TlsContentType getContentType() {
		return TlsContentType.ChangeCipherSpec;
	}

	@Override
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
		result.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/TLS12_ChangeCipherSpec.html"));
		
		return result;
	}
}
