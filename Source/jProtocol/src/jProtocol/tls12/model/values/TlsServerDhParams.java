package jProtocol.tls12.model.values;

public class TlsServerDhParams {

	private byte[] _dh_p; 	// The prime modulus used for the Diffie-Hellman operation.
	private byte[] _dh_g; 	// The generator used for the Diffie-Hellman operation.
	private byte[] _dh_Ys;	// The server’s Diffie-Hellman public value (g^X mod p).
	
	public TlsServerDhParams(byte[] p, byte[] g, byte[] ys) {
		_dh_p = p;
		_dh_g = g;
		_dh_Ys = ys;
	}

	public byte[] getDh_p() {
		return _dh_p;
	}

	public byte[] getDh_g() {
		return _dh_g;
	}

	public byte[] getDh_Ys() {
		return _dh_Ys;
	}
}
