package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCipherSuite;
import jProtocol.tls12.model.TlsSecurityParameters.CipherType;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;

public abstract class TlsBlockCipherSuite extends TlsCipherSuite {
	
	public class TlsBlockEncryptionResult {
		//the plain fields
		public byte[] iv;		
		public byte[] content;
		public byte[] mac;
		public byte[] padding;
		public byte paddingLength;
		
		//the encrypted result
		public byte[] result;
	}
	
	public abstract TlsBlockEncryptionResult encrypt(byte[] key, byte[] iv, byte[] plaintext);
	
	public abstract byte[] decrypt(byte[] key, byte[] iv, byte[] ciphertext) throws TlsBadRecordMacException;

	@Override
	public CipherType getCipherType() {
		return CipherType.block;
	}

	@Override
	public byte getFixedIvLength() {
		return 0;
	}

}
