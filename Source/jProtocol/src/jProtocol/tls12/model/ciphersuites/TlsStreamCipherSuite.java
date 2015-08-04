package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCipherSuite;
import jProtocol.tls12.model.TlsSecurityParameters.CipherType;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;

public abstract class TlsStreamCipherSuite extends TlsCipherSuite {

	public class TlsStreamEncryptionResult {
		//the plain fields	
		public byte[] content;
		public byte[] mac;
		
		//the encrypted result
		public byte[] result;
	}
	
	public abstract TlsStreamEncryptionResult encrypt(byte[] key, byte[] plaintext);
	
	public abstract byte[] decrypt(byte[] key, byte[] ciphertext) throws TlsBadRecordMacException;

	@Override
	public CipherType getCipherType() {
		return CipherType.stream;
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
