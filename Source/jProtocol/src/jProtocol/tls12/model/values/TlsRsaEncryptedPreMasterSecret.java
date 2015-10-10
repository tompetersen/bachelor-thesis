package jProtocol.tls12.model.values;

public class TlsRsaEncryptedPreMasterSecret {

	private byte[] _premastersecret;
	
	/**
	 * Creates a rsa encrypted premaster secret object.
	 * 
	 * @param premastersecret the premaster secret bytes
	 */
	public TlsRsaEncryptedPreMasterSecret(byte[] premastersecret) {
		_premastersecret = premastersecret;
	}
	
	/**
	 * Returns the premastersecret bytes
	 * 
	 * @return the bytes
	 */
	public byte[] getEncryptedPreMasterSecret() {
		return _premastersecret;
	}

}
