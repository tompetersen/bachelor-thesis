package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsServerHelloDoneMessage extends TlsHandshakeMessage {
	
	/*
	 *  struct { } ServerHelloDone;
	 */
	public TlsServerHelloDoneMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO Parsing
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

}
