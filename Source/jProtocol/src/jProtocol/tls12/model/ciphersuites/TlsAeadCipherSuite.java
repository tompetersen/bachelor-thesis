package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCipherSuite;
import jProtocol.tls12.model.TlsSecurityParameters.CipherType;
import jProtocol.tls12.model.TlsSecurityParameters.MacAlgorithm;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;

/*
 *  additional_data = seq_num + TLSCompressed.type + TLSCompressed.version + TLSCompressed.length;
 *  
 *  Nonce? explicit, implicit - how to compute?
 */
public abstract class TlsAeadCipherSuite extends TlsCipherSuite {

	public class TlsAeadEncryptionResult {
		//the plain fields
		public byte[] nonce;		
		public byte[] content;
		
		//the encrypted result
		public byte[] result;
	}
	
	public abstract TlsAeadEncryptionResult encrypt(byte[] key, byte[] nonce, byte[] plaintext, byte[] additionalData);
	
	public abstract byte[] decrypt(byte[] key, byte[] nonce, byte[] ciphertext, byte[] additionalData) throws TlsBadRecordMacException;

	@Override
	public CipherType getCipherType() {
		return CipherType.aead;
	}
	
	/*
	 * Implicit authentication of AEAD ciphers requires no explicit MAC.
	 */
	@Override
	public MacAlgorithm getMacAlgorithm() {
		return null;
	}

	@Override
	public byte getMacLength() {
		return 0;
	}

	@Override
	public byte getMacKeyLength() {
		return 0;
	}
	
}
