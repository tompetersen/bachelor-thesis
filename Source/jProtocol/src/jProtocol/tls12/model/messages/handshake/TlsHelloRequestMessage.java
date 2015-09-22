package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.util.ArrayList;
import java.util.List;

public class TlsHelloRequestMessage extends TlsHandshakeMessage {

	/*
	 *  struct { } HelloRequest;
	 */
	
	public TlsHelloRequestMessage() {
		super();
	}

	public TlsHelloRequestMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);

		if (unparsedContent.length > 0) {
			throw new TlsDecodeErrorException("Hello request message should have no content!");
		}
	}
	
	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.hello_request;
	}


	@Override
	public byte[] getBodyBytes() {
		byte[] result = new byte[0];
		return result;
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		return new ArrayList<KeyValueObject>();
	}
	
	@Override
	public String getBodyHtmlInfo() {
		return "";
	}

}
