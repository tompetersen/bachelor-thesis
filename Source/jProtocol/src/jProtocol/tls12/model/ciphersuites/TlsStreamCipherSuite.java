package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.fragments.TlsStreamFragment;
import jProtocol.tls12.model.values.TlsCipherType;

public abstract class TlsStreamCipherSuite implements TlsCipherSuite {

	public class TlsStreamEncryptionResult {
		//the plain fields	
		public byte[] content;
		public byte[] mac;
		
		//the encrypted result
		public byte[] streamCiphered;
	}
	
	@Override
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext,
			TlsEncryptionParameters parameters) {
		// TODO currently just used for TLS_NULL_WITH_NULL_NULL -> implement if necessary
		TlsStreamFragment fragment = new TlsStreamFragment(encrypt(parameters.getEncryptionWriteKey(), plaintext.getFragment()))
		; 
		return new TlsCiphertext(plaintext.getMessage(),
				plaintext.getVersion(),
				fragment);
	}

	@Override
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry) throws TlsBadRecordMacException, TlsDecodeErrorException {
		// TODO currently just used for TLS_NULL_WITH_NULL_NULL -> implement if necessary

		byte[] content = decrypt(parameters.getEncryptionWriteKey(), ciphertext.getFragment().getBytes());
		return new TlsPlaintext(content, registry);
	}
	
	public abstract TlsStreamEncryptionResult encrypt(byte[] key, byte[] plaintext);
	
	public abstract byte[] decrypt(byte[] key, byte[] ciphertext) throws TlsBadRecordMacException;

	@Override
	public TlsCipherType getCipherType() {
		return TlsCipherType.stream;
	}

	/*
	 * No blocks or IV needed for stream ciphers.
	 */
	@Override
	public byte getBlockLength() {
		return 0;
	}

	@Override
	public byte getFixedIvLength() {
		return 0;
	}

	@Override
	public byte getRecordIvLength() {
		return 0;
	}
}
