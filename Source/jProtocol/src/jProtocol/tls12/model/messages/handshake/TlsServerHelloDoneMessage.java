package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.util.ArrayList;
import java.util.List;

public class TlsServerHelloDoneMessage extends TlsHandshakeMessage {
	
	/*
	 struct { } ServerHelloDone;
	 */
	
	public TlsServerHelloDoneMessage() {
		super();
	}
	
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
	
}
