package jProtocol.tls12.model.ciphersuites;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.crypto.TlsMacParameters;
import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.fragments.TlsBlockFragment;
import jProtocol.tls12.model.values.TlsCipherType;
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
		TlsMacParameters parameters = new TlsMacParameters(encParam.getMacWriteKey(), 
				encParam.getSequenceNumber(), 
				plaintext.getContentType().getValue(), 
				plaintext.getVersion().getMajorVersion(), 
				plaintext.getVersion().getMinorVersion(), 
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
		b.put(plaintext.getFragment());
		b.put(mac);
		b.put(padding);
		b.put((byte)paddingLength);
	
	//build iv
		byte[] iv = TlsPseudoRandomNumberGenerator.nextBytes(getRecordIvLength());
		
	//encrypt
		byte[] encrypted = encrypt(encParam.getEncryptionWriteKey(), iv, b.array());
		
		TlsBlockEncryptionResult result = new TlsBlockEncryptionResult(iv, plaintext.getFragment(), mac, padding, (byte)paddingLength, encrypted);
		TlsCiphertext ciphertext = new TlsCiphertext(plaintext.getMessage(), plaintext.getVersion(), new TlsBlockFragment(result));
		
		return ciphertext;
	}
	
	public TlsPlaintext ciphertextToPlaintext(TlsCiphertext ciphertext,  TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry) throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		byte[] ciphertextBytes = ciphertext.getBytes();
		if (ciphertextBytes.length <= TlsPlaintext.RECORD_HEADER_LENGTH) {
			throw new TlsDecodeErrorException("Ciphertext contains not enough information for record header and fragment!");
		}
		
		byte[] headerBytes = new byte[TlsPlaintext.RECORD_HEADER_LENGTH];
		System.arraycopy(ciphertextBytes, 0, headerBytes, 0, TlsPlaintext.RECORD_HEADER_LENGTH);
		byte[] fragmentBytes = new byte[ciphertextBytes.length - TlsPlaintext.RECORD_HEADER_LENGTH];
		System.arraycopy(ciphertextBytes, TlsPlaintext.RECORD_HEADER_LENGTH, fragmentBytes, 0, fragmentBytes.length);
		
	//check length field	
		byte[] recordLengthBytes = {headerBytes[3], headerBytes[4]};
		int recordLength = ByteHelper.twoByteArrayToInt(recordLengthBytes);
		
		if (recordLength != ciphertextBytes.length - TlsPlaintext.RECORD_HEADER_LENGTH) {
			throw new TlsDecodeErrorException("Message contains invalid fragment length!");
		}
		
	//decrypt
		byte[] encrypted = Arrays.copyOfRange(fragmentBytes, getRecordIvLength(), fragmentBytes.length);
		byte[] iv = Arrays.copyOfRange(fragmentBytes, 0, getRecordIvLength());
		byte[] decrypted = decrypt(parameters.getEncryptionWriteKey(), iv, encrypted);
		
	//padding
		int paddingLength = decrypted[decrypted.length - 1] & 0xFF; //interpret as unsigned byte
		if (paddingLength > (decrypted.length - getMacLength() - 1)) {
			throw new TlsBadPaddingException("Padding length invalid!");
		}
		byte[] padding = Arrays.copyOfRange(decrypted, decrypted.length - paddingLength - 1, decrypted.length - 1);
		byte[] mac = Arrays.copyOfRange(decrypted, decrypted.length - paddingLength - 1 - getMacLength(), decrypted.length - paddingLength - 1);
		byte[] decryptedFragment = Arrays.copyOfRange(decrypted, 0, decrypted.length - paddingLength - 1 - getMacLength());

		for (int i = 0; i < padding.length; i++) {
			if (padding[i] != paddingLength) {
				throw new TlsBadPaddingException("Padding contains incorrect values!");
			}
		}
		
	//check mac
		TlsMacParameters macParams = new TlsMacParameters(parameters.getMacWriteKey(), 
				parameters.getSequenceNumber(), 
				ciphertext.getContentType().getValue(), 
				ciphertext.getVersion().getMajorVersion(), 
				ciphertext.getVersion().getMinorVersion(), 
				(short)decryptedFragment.length, 
				decryptedFragment);
		byte[] computedMac = computeMac(macParams);
		
		if (!Arrays.equals(mac, computedMac)) {
			throw new TlsBadRecordMacException("Record check failed!");
		}
		
	//set correct length for fragment in plaintext
		int newLength = decryptedFragment.length;
		byte[] lengthBytes = ByteHelper.intToTwoByteArray(newLength);
		headerBytes[3] = lengthBytes[0];
		headerBytes[4] = lengthBytes[1];
		
		return new TlsPlaintext(ByteHelper.concatenate(headerBytes, decryptedFragment), registry);
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
