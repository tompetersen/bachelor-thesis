package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsVerifyData;

public class TlsFinishedMessage extends TlsHandshakeMessage {

	private TlsVerifyData _verifyData;
	
	public TlsFinishedMessage(TlsVerifyData verifyData) {
		//TODO: length check
		_verifyData = verifyData;
	}

	public TlsFinishedMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO  Parsing
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.finished;
	}

	@Override
	public byte[] getBodyBytes() {
		return _verifyData.getBytes(); //Fixed length VERIFY_DATA_LENGTH, therefore no length field needed
	}

	public TlsVerifyData getVerifyData() {
		return _verifyData;
	}

}
