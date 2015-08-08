package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsContentType;
import jProtocol.tls12.model.TlsMacParameters;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.TlsSecurityParameters.CipherType;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.fragments.TlsBlockFragment;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
		
		public TlsBlockEncryptionResult(byte[] iv, byte[] content, byte[] mac, byte[] padding, byte paddingLength, byte[] result) {
			super();
			this.iv = iv;
			this.content = content;
			this.mac = mac;
			this.padding = padding;
			this.paddingLength = paddingLength;
			this.result = result;
		}
	}
	
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext, TlsEncryptionParameters encParam) {
	//compute mac
		TlsMacParameters parameters = new TlsMacParameters(encParam.macWriteKey, 
				encParam.sequenceNumber, 
				TlsContentType.valueFromContentType(plaintext.getContentType()), 
				plaintext.getVersion().majorVersion, 
				plaintext.getVersion().minorVersion, 
				plaintext.getLength(), 
				plaintext.getFragment());
		byte[] mac = computeMac(parameters);
		
		assert mac.length == getMacLength() : "Different Mac lengths!";
		
	//create padding
		int lengthWithoutPadding = mac.length + plaintext.getLength() + 1; //+1 for paddingLength
		int paddingLength = getBlockLength() - (lengthWithoutPadding % getBlockLength());
		byte[] padding = new byte[paddingLength];
		Arrays.fill(padding, (byte)paddingLength);
		
		assert (lengthWithoutPadding + padding.length) % getBlockLength() == 0 : "Length is no multiple of block length after padding!";
		
	//create plaintext
		ByteBuffer b = ByteBuffer.allocate(plaintext.getLength() + padding.length + 1);
		b.put(plaintext.getFragment());
		b.put(mac);
		b.put(padding);
		b.put((byte)paddingLength);
	
	//build iv
		byte[] iv = TlsPseudoRandomNumberGenerator.nextBytes(getRecordIvLength());
		
	//encrypt
		byte[] encrypted = encrypt(encParam.encryptionWriteKey, b.array(), iv);
		
		TlsBlockEncryptionResult result = new TlsBlockEncryptionResult(iv, plaintext.getFragment(), mac, padding, (byte)paddingLength, encrypted);
		TlsCiphertext ciphertext = new TlsCiphertext(plaintext.getMessage(), plaintext.getVersion(), new TlsBlockFragment(result));
		
		return ciphertext;
	}
	
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext,  TlsEncryptionParameters parameters) throws TlsBadRecordMacException {
	//decrypt	
		byte[] fragmentBytes = ciphertext.getFragment().getBytes();
		byte[] encrypted = Arrays.copyOfRange(fragmentBytes, getRecordIvLength(), fragmentBytes.length);
		byte[] iv = Arrays.copyOfRange(fragmentBytes, 0, getRecordIvLength());
		byte[] decrypted = decrypt(parameters.encryptionWriteKey, encrypted, iv);
		
	//padding
		int paddingLength = decrypted[decrypted.length - 1];
		byte[] padding = Arrays.copyOfRange(decrypted, decrypted.length - paddingLength - 1, decrypted.length - 1);
		byte[] content = Arrays.copyOfRange(decrypted, getMacLength(), decrypted.length - paddingLength - 1);
		byte[] mac = Arrays.copyOfRange(decrypted, 0, getMacLength());

		for (int i = 0; i < padding.length; i++) {
			if (padding[i] != paddingLength) {
				//TODO: 
				throw new RuntimeException("PADDING CHECK FAILED!");
			}
		}
		
	//check mac
		TlsMacParameters macParams = new TlsMacParameters(parameters.macWriteKey, 
				parameters.sequenceNumber, 
				TlsContentType.valueFromContentType(ciphertext.getContentType()), 
				ciphertext.getVersion().majorVersion, 
				ciphertext.getVersion().minorVersion, 
				(short)content.length, 
				content);
		byte[] computedMac = computeMac(macParams);
		
		if (!Arrays.equals(mac, computedMac)) {
			throw new TlsBadRecordMacException();
		}
		
		//TODO: Ohje, wie geschummelt
		return new TlsPlaintext(ciphertext.getMessage(), ciphertext.getVersion());
	}
	
	public abstract byte[] computeMac(TlsMacParameters parameters);
	
	public abstract byte[] encrypt(byte[] key, byte[] message, byte[] iv);
	
	public abstract byte[] decrypt(byte[] key, byte[] iv, byte[] ciphertext);

	@Override
	public CipherType getCipherType() {
		return CipherType.block;
	}

	@Override
	public byte getFixedIvLength() {
		return 0;
	}

}
