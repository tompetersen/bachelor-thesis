package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.values.TlsRsaEncryptedPreMasterSecret;

public class TlsClientKeyExchangeMessage_RSA extends TlsClientKeyExchangeMessage {

	/*
	 struct {
          public-key-encrypted PreMasterSecret pre_master_secret;
      } EncryptedPreMasterSecret;
	 */
	
	private TlsRsaEncryptedPreMasterSecret _encPreMasterSecret;

	public TlsClientKeyExchangeMessage_RSA(TlsRsaEncryptedPreMasterSecret rsaEncryptedPremasterSecret) {
		_encPreMasterSecret = rsaEncryptedPremasterSecret;
	}

	public TlsClientKeyExchangeMessage_RSA(byte[] unparsedContent) {
		super();
		// TODO Parsing
	}

	@Override
	public byte[] getBodyBytes() {
		//TODO: number of bytes of length field? 
		//http://stackoverflow.com/questions/11505547/how-calculate-size-of-rsa-cipher-text-using-key-size-clear-text-length
		return _encPreMasterSecret.getPreMasterSecret();
	}

	public TlsRsaEncryptedPreMasterSecret getRsaEncryptedPreMasterSecret() {
		return _encPreMasterSecret;
	}
}
