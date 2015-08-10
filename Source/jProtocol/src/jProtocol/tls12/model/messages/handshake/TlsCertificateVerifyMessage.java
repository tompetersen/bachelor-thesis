package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsCertificateVerifyMessage extends TlsHandshakeMessage {

	//TODO: Used for authenticated client -> Implement if necessary
	public TlsCertificateVerifyMessage() {

	}

	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.certificate_verify;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
