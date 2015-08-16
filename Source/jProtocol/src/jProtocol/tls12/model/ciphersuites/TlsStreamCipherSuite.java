package jProtocol.tls12.model.ciphersuites;

import jProtocol.helper.ByteHelper;
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
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext, TlsEncryptionParameters parameters) {
		// TODO currently just used for TLS_NULL_WITH_NULL_NULL -> implement if necessary
		TlsStreamFragment fragment = new TlsStreamFragment(encrypt(parameters.getEncryptionWriteKey(), plaintext.getFragment())); 
		return new TlsCiphertext(plaintext.getMessage(),plaintext.getVersion(),fragment);
	}

	@Override
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry) throws TlsBadRecordMacException, TlsDecodeErrorException {
		byte[] ciphertextBytes = ciphertext.getBytes();
		if (ciphertextBytes.length <= TlsPlaintext.RECORD_HEADER_LENGTH) {
			throw new TlsDecodeErrorException("Ciphertext contains not enough information for record header and fragment!");
		}
		
		byte[] headerBytes = new byte[TlsPlaintext.RECORD_HEADER_LENGTH];
		System.arraycopy(ciphertextBytes, 0, headerBytes, 0, TlsPlaintext.RECORD_HEADER_LENGTH);
		byte[] fragmentBytes = new byte[ciphertextBytes.length - TlsPlaintext.RECORD_HEADER_LENGTH];
		System.arraycopy(ciphertextBytes, TlsPlaintext.RECORD_HEADER_LENGTH, fragmentBytes, 0, fragmentBytes.length);
		
		// TODO currently just used for TLS_NULL_WITH_NULL_NULL -> implement if necessary

	//decrypt
		byte[] decryptedFragment = decrypt(parameters.getEncryptionWriteKey(), fragmentBytes);
		
	//set correct length for fragment in plaintext
		int newLength = decryptedFragment.length;
		byte[] lengthBytes = ByteHelper.intToTwoByteArray(newLength);
		headerBytes[3] = lengthBytes[0];
		headerBytes[4] = lengthBytes[1];
			
		TlsPlaintext plaintext = new TlsPlaintext(ByteHelper.concatenate(headerBytes, decryptedFragment), registry);
		
		return plaintext;
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
