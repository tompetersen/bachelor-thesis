package jProtocol.tls12.model.values;

import java.nio.ByteBuffer;

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
	
	public byte[] getBytes() {
		ByteBuffer result = ByteBuffer.allocate(_dh_p.length + _dh_g.length + _dh_Ys.length);
		
		result.put(_dh_p);
		result.put(_dh_g);
		result.put(_dh_Ys);
		
		return result.array();
	}
}
