package jProtocol.tls12.model.messages;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsApplicationData;
import jProtocol.tls12.model.values.TlsContentType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class TlsApplicationDataMessage extends TlsMessage {

	/*
	 *  Application data messages are carried by the record layer and are 
	 *  fragmented, compressed, and encrypted based on the current connection 
	 *  state. The messages are treated as transparent data to the record 
	 *  layer.
	 *  
	 *  See chapter 10, p. 65 TLS 1.2
	 */
	
	private TlsApplicationData _applicationData;
	
	/**
	 * Creates an application data message.
	 * 
	 * @param applicationData the application data. Must not be null.
	 */
	public TlsApplicationDataMessage(TlsApplicationData applicationData) {
		if (applicationData == null) {
			throw new IllegalArgumentException("Application data must not be null!");
		}
		_applicationData = applicationData;
	}
	
	/**
	 * Creates an application data message by parsing sent bytes.
	 * 
	 * @param unparsedContent the sent bytes
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsApplicationDataMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		
		_applicationData = new TlsApplicationData(unparsedContent);
	}
	
	/**
	 * Returns the application data contained in this message.
	 * 
	 * @return the application data
	 */
	public TlsApplicationData getApplicationData() {
		return _applicationData;
	}

	@Override
	public TlsContentType getContentType() {
		return TlsContentType.ApplicationData;
	}

	@Override
	public byte[] getBytes() {
		return _applicationData.getBytes();
	}

	@Override
	public String toString() {
		return "DATA";
	}

	@Override
	public KeyValueObject getViewData() {
		ArrayList<KeyValueObject> children = new ArrayList<KeyValueObject>();
		
		children.add(new KeyValueObject("Value", new String(getBytes(), StandardCharsets.US_ASCII)));
		
		KeyValueObject result = new KeyValueObject("Content", children);
		result.setValue("TlsApplicationData");
		result.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/TLS12_ApplicationData.html"));
		
		return result;
	}
}
