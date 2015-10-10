package jProtocol.tls12.model.values;

import java.nio.ByteBuffer;

public class TlsServerDhParams {

	private byte[] _dh_p; 	// The prime modulus used for the Diffie-Hellman operation.
	private byte[] _dh_g; 	// The generator used for the Diffie-Hellman operation.
	private byte[] _dh_Ys;	// The server’s Diffie-Hellman public value (g^X mod p).
	
	/**
	 * Creates a server DH parameters object.
	 * 
	 * @param p the prime modulus used for the Diffie-Hellman operation
	 * @param g the generator used for the Diffie-Hellman operation
	 * @param ys the server’s Diffie-Hellman public value (g^X mod p)
	 */
	public TlsServerDhParams(byte[] p, byte[] g, byte[] ys) {
		_dh_p = p;
		_dh_g = g;
		_dh_Ys = ys;
	}

	/**
	 * Returns the prime modulus used for the Diffie-Hellman operation.
	 * 
	 * @return p
	 */
	public byte[] getDh_p() {
		return _dh_p;
	}

	/**
	 * Returns the generator used for the Diffie-Hellman operation.
	 * 
	 * @return g
	 */
	public byte[] getDh_g() {
		return _dh_g;
	}

	/**
	 * Returns the server’s Diffie-Hellman public value (g^X mod p).
	 * 
	 * @return Ys
	 */
	public byte[] getDh_Ys() {
		return _dh_Ys;
	}
	
	/**
	 * Returns a byte representation of the server DH parameters.
	 * 
	 * @return the bytes
	 */
	public byte[] getBytes() {
		ByteBuffer result = ByteBuffer.allocate(_dh_p.length + _dh_g.length + _dh_Ys.length);
		
		result.put(_dh_p);
		result.put(_dh_g);
		result.put(_dh_Ys);
		
		return result.array();
	}
}
