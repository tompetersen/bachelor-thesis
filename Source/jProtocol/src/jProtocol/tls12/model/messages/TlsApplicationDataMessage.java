package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsApplicationData;
import jProtocol.tls12.model.values.TlsContentType;

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
}
