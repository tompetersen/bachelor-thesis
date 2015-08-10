package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsFinishedMessage extends TlsHandshakeMessage {

	public byte[] _verifiedData;
	
	public TlsFinishedMessage(byte[] verifiedData) {
		_verifiedData = verifiedData;
	}

	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.finished;
	}

	@Override
	public byte[] getBodyBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}
