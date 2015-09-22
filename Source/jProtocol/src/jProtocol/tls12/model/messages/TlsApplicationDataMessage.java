package jProtocol.tls12.model.messages;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsApplicationData;
import jProtocol.tls12.model.values.TlsContentType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/*
 *  Application data messages are carried by the record layer and are 
 *  fragmented, compressed, and encrypted based on the current connection 
 *  state. The messages are treated as transparent data to the record 
 *  layer.
 *  
 *  See chapter 10, p. 65 TLS 1.2
 */
public class TlsApplicationDataMessage extends TlsMessage {

	private TlsApplicationData _applicationData;
	
	public TlsApplicationDataMessage(TlsApplicationData applicationData) {
		if (applicationData == null) {
			throw new IllegalArgumentException("Application data must not be null!");
		}
		_applicationData = applicationData;
	}
	
	public TlsApplicationDataMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		
		_applicationData = new TlsApplicationData(unparsedContent);
	}
	
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
