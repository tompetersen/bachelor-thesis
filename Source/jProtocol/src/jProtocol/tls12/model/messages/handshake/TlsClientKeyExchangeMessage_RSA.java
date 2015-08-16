package jProtocol.tls12.model.messages.handshake;

public class TlsClientKeyExchangeMessage_RSA extends TlsClientKeyExchangeMessage {

	/*
	 struct {
          public-key-encrypted PreMasterSecret pre_master_secret;
      } EncryptedPreMasterSecret;
	 */
	
	private byte[] _encPreMasterSecret;

	public TlsClientKeyExchangeMessage_RSA(byte[] rsaEncryptedPremasterSecret) {
		_encPreMasterSecret = rsaEncryptedPremasterSecret;
	}

	public TlsClientKeyExchangeMessage_RSA() {
		super();
		// TODO Parsing
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
