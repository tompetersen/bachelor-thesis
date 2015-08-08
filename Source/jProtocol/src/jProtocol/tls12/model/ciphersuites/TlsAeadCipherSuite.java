package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
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
	
	@Override
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext,
			TlsEncryptionParameters parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext plaintext,
			TlsEncryptionParameters parameters) throws TlsBadRecordMacException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public byte getRecordIvLength() {
		return 0;
	}
}
