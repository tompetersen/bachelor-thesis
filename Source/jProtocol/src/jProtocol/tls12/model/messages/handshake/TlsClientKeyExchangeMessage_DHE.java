package jProtocol.tls12.model.messages.handshake;

public class TlsClientKeyExchangeMessage_DHE extends TlsClientKeyExchangeMessage {

	private byte[] _yc;
	
	public TlsClientKeyExchangeMessage_DHE(byte[] dhClientPublicValue) {
		_yc = dhClientPublicValue;
	}

	@Override
	public byte[] getBodyBytes() {
		return _yc;
	}

	public byte[] getDiffieHellmenClientPublicValue() {
		return _yc;
	}
}
 