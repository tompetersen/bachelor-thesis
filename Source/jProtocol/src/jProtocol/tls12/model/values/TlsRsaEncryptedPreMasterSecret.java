package jProtocol.tls12.model.values;

public class TlsRsaEncryptedPreMasterSecret {

	private byte[] _premastersecret;
	
	public TlsRsaEncryptedPreMasterSecret(byte[] premastersecret) {
		_premastersecret = premastersecret;
	}
	
	public byte[] getPreMasterSecret() {
		return _premastersecret;
	}

}
