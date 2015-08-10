package jProtocol.tls12.model.messages.handshake;

public class TlsClientKeyExchangeMessage_RSA extends TlsClientKeyExchangeMessage {

	private byte[] _encPreMasterSecret;

	public TlsClientKeyExchangeMessage_RSA(byte[] rsaEncryptedPremasterSecret) {
		_encPreMasterSecret = rsaEncryptedPremasterSecret;
	}

	@Override
	public byte[] getBodyBytes() {
		//TODO: number of bytes of length field? 
		//http://stackoverflow.com/questions/11505547/how-calculate-size-of-rsa-cipher-text-using-key-size-clear-text-length
		return _encPreMasterSecret;
	}

	public byte[] getRsaEncryptedPreMasterSecret() {
		return _encPreMasterSecret;
	}
}
