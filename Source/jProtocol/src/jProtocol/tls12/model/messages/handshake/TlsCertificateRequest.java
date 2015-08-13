package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsCertificateRequest extends TlsHandshakeMessage {

	//TODO: Used for authenticated client -> Implement if necessary
	public TlsCertificateRequest() {
		
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate_request;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
