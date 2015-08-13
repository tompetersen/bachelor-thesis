package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

public class TlsFinishedMessage extends TlsHandshakeMessage {

	private byte[] _verifyData;
	
	public TlsFinishedMessage(byte[] verifiedData) {
		//TODO: length check
		_verifyData = verifiedData;
	}

	@Override
	public HandshakeType getHandshakeType() {
		return HandshakeType.finished;
	}

	@Override
	public byte[] getBodyBytes() {
		return _verifyData; //Fixed length VERIFY_DATA_LENGTH, therefore no length field needed
	}

	public byte[] getVerifyData() {
		return _verifyData;
	}

}
