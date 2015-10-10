package jProtocol.tls12.model.values;

public class TlsClientDhPublicKey {

	private byte[] _publicKey;
	
	/**
	 * Creates a client public key object.
	 * 
	 * @param publicKey the public key bytes
	 */
	public TlsClientDhPublicKey(byte[] publicKey) {
		_publicKey = publicKey;
	}

	/**
	 * Returns the included public key bytes.
	 * 
	 * @return the bytes
	 */
	public byte[] getPublicKey() {
		return _publicKey;
	}
}
