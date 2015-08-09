package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.crypto.TlsMacParameters;
import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.fragments.TlsBlockFragment;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsContentType;

import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class TlsBlockCipherSuite implements TlsCipherSuite {
	
	public class TlsBlockEncryptionResult {
		//the plain fields
		public byte[] iv;		
		public byte[] content;
		public byte[] mac;
		public byte[] padding;
		public byte paddingLength;
		
		//the encrypted result
		public byte[] blockCiphered;
		
		public TlsBlockEncryptionResult(byte[] iv, byte[] content, byte[] mac, byte[] padding, byte paddingLength, byte[] result) {
			super();
			this.iv = iv;
			this.content = content;
			this.mac = mac;
			this.padding = padding;
			this.paddingLength = paddingLength;
			this.blockCiphered = result;
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
		
	//create padding
		int lengthWithoutPadding = mac.length + plaintext.getLength() + 1; //+1 for paddingLength
		int paddingLength = getBlockLength() - (lengthWithoutPadding % getBlockLength());
		byte[] padding = new byte[paddingLength];
		Arrays.fill(padding, (byte)paddingLength);

	//create plaintext
		ByteBuffer b = ByteBuffer.allocate(plaintext.getLength() + mac.length + padding.length + 1);
		b.put(mac);
		b.put(plaintext.getFragment());
		b.put(padding);
		b.put((byte)paddingLength);
	
	//build iv
		byte[] iv = TlsPseudoRandomNumberGenerator.nextBytes(getRecordIvLength());
		
	//encrypt
		byte[] encrypted = encrypt(encParam.encryptionWriteKey, iv, b.array());
		
		TlsBlockEncryptionResult result = new TlsBlockEncryptionResult(iv, plaintext.getFragment(), mac, padding, (byte)paddingLength, encrypted);
		TlsCiphertext ciphertext = new TlsCiphertext(plaintext.getMessage(), plaintext.getVersion(), new TlsBlockFragment(result));
		
		return ciphertext;
	}
	
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext,  TlsEncryptionParameters parameters) throws TlsBadRecordMacException, TlsBadPaddingException {
	//decrypt	
		byte[] fragmentBytes = ciphertext.getFragment().getBytes();
		byte[] encrypted = Arrays.copyOfRange(fragmentBytes, getRecordIvLength(), fragmentBytes.length);
		byte[] iv = Arrays.copyOfRange(fragmentBytes, 0, getRecordIvLength());
		byte[] decrypted = decrypt(parameters.encryptionWriteKey, iv, encrypted);
		
	//padding
		int paddingLength = decrypted[decrypted.length - 1] & 0xFF; //interpret as unsigned byte
		if (paddingLength > (decrypted.length - getMacLength() - 1)) {
			throw new TlsBadPaddingException("Padding length invalid!");
		}
		byte[] padding = Arrays.copyOfRange(decrypted, decrypted.length - paddingLength - 1, decrypted.length - 1);
		byte[] content = Arrays.copyOfRange(decrypted, getMacLength(), decrypted.length - paddingLength - 1);
		byte[] mac = Arrays.copyOfRange(decrypted, 0, getMacLength());

		for (int i = 0; i < padding.length; i++) {
			if (padding[i] != paddingLength) {
				throw new TlsBadPaddingException("Padding contains incorrect values!");
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
			throw new TlsBadRecordMacException("Record check failed!");
		}
		
		//TODO: Ohje, wie geschummelt -> nicht mehr ganz so...
		return new TlsPlaintext(content, ciphertext.getVersion(), ciphertext.getContentType());
	}
	
	public abstract byte[] computeMac(TlsMacParameters parameters);
	
	public abstract byte[] encrypt(byte[] key, byte[] iv, byte[] message);
	
	public abstract byte[] decrypt(byte[] key, byte[] iv, byte[] ciphertext);

	@Override
	public TlsCipherType getCipherType() {
		return TlsCipherType.block;
	}

	@Override
	public byte getFixedIvLength() {
		return 0;
	}

}
