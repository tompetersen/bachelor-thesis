package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsCertificateVerifyMessage extends TlsHandshakeMessage {

	//TODO: Used for authenticated client -> Implement if necessary
	public TlsCertificateVerifyMessage() {

	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate_verify;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
