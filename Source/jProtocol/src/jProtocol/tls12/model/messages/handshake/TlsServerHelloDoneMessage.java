package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.util.ArrayList;
import java.util.List;

public class TlsServerHelloDoneMessage extends TlsHandshakeMessage {
	
	/*
	 struct { } ServerHelloDone;
	 */
	
	/**
	 * Creates a server hello done message.
	 */
	public TlsServerHelloDoneMessage() {
		super();
	}
	
	/**
	 * Creates an server hello done message by parsing sent bytes.
	 * 
	 * @param unparsedContent the sent bytes
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsServerHelloDoneMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);

		if (unparsedContent.length > 0) {
			throw new TlsDecodeErrorException("Server hello done message should have no content!");
		}
	}
	
	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.server_hello_done;
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
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHelloDone.html");
	}
	
}
