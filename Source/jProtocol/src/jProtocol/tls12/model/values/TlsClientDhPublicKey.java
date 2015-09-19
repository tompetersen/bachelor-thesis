package jProtocol.tls12.model.values;

public class TlsClientDhPublicKey {

	private byte[] _publicKey;
	
	public TlsClientDhPublicKey(byte[] publicKey) {
		_publicKey = publicKey;
	}

	public byte[] getPublicKey() {
		return _publicKey;
	}
}
