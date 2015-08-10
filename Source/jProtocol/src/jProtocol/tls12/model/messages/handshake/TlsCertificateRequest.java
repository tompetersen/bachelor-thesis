package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsCertificateRequest extends TlsHandshakeMessage {

	//TODO: Used for authenticated client -> Implement if necessary
	public TlsCertificateRequest() {
		
	}

	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.certificate_request;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
