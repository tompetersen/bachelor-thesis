package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsMacAlgorithm;

/*
 *  additional_data = seq_num + TLSCompressed.type + TLSCompressed.version + TLSCompressed.length;
 *  
 *  Nonce? explicit, implicit - how to compute?
 */
public abstract class TlsAeadCipherSuite implements TlsCipherSuite {

	public class TlsAeadEncryptionResult {
		//the plain fields
		public byte[] nonce;		
		public byte[] content;
		
		//the encrypted result
		public byte[] aeadCiphered;
	}
	
	@Override
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext,
			TlsEncryptionParameters parameters) {
		// TODO AEAD plaintextToCiphertext
		return null;
	}

	@Override
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext plaintext, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry) throws TlsBadRecordMacException {
		// TODO AEAD ciphertextToPlaintext
		return null;
	}
	
	public abstract TlsAeadEncryptionResult encrypt(byte[] key, byte[] nonce, byte[] plaintext, byte[] additionalData);
	
	public abstract byte[] decrypt(byte[] key, byte[] nonce, byte[] ciphertext, byte[] additionalData) throws TlsBadRecordMacException;

	@Override
	public TlsCipherType getCipherType() {
		return TlsCipherType.aead;
	}
	
	/*
	 * Implicit authentication of AEAD ciphers requires no explicit MAC.
	 */
	@Override
	public TlsMacAlgorithm getMacAlgorithm() {
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
