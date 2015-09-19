package jProtocol.tls12.model.ciphersuites;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.fragments.TlsAeadFragment;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsMacAlgorithm;
import java.util.Arrays;

/*
 *  additional_data = seq_num + TLSCompressed.type + TLSCompressed.version + TLSCompressed.length;
 *  
 *  Nonce? explicit, implicit - how to compute?
 */
public abstract class TlsAeadCipherSuite implements TlsCipherSuite {

	public static int NONCE_EXPLICIT_LENGTH = 8; //in bytes
	
	public class TlsAeadEncryptionResult {
		//the plain fields
		public byte[] nonce_explicit;		
		public byte[] content;
		
		//the encrypted result
		public byte[] aeadCiphered;
		
		public TlsAeadEncryptionResult(byte[] nonce_explicit, byte[] content, byte[] aeadCiphered) {
			super();
			
			this.nonce_explicit = nonce_explicit;
			this.content = content;
			this.aeadCiphered = aeadCiphered;
		}
	}
	
	@Override
	public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext, TlsEncryptionParameters parameters) {
	//nonce	
		/* struct {
         *    opaque salt[4];
         *    opaque nonce_explicit[8];
         * } GCMNonce;
		 * 
		 * The salt is the "implicit" part of the nonce and is not sent in the packet. Instead, the salt is 
		 * generated as part of the handshake process: it is either the client_write_IV (when the client is 
		 * sending) or the server_write_IV (when the server is sending). The salt length 
		 * (SecurityParameters.fixed_iv_length) is 4 octets.
		 */
		byte[] nonce_implicit = parameters.getWriteIv();
		
		/* The nonce_explicit is the "explicit" part of the nonce. It is chosen by the sender and is carried 
		 * in each TLS record in the GenericAEADCipher.nonce_explicit field. The nonce_explicit length 
		 * (SecurityParameters.record_iv_length) is 8 octets. [...]
		 * The nonce_explicit MAY be the 64-bit sequence number.
		 */
		byte[] nonce_explicit = ByteHelper.longToByteArray(parameters.getSequenceNumber());
		
		byte[] nonce = ByteHelper.concatenate(nonce_implicit, nonce_explicit);
	
	//additional data 	
		byte[] tmpAddDat = {plaintext.getContentType().getValue(), 
				plaintext.getVersion().getMajorVersion(), 
				plaintext.getVersion().getMinorVersion()};
		tmpAddDat = ByteHelper.concatenate(ByteHelper.longToByteArray(parameters.getSequenceNumber()), tmpAddDat);
		byte[] additionalData = ByteHelper.concatenate(tmpAddDat, ByteHelper.intToTwoByteArray(plaintext.getLength()));
		
	//encryption
		byte[] encrypted = encrypt(parameters.getEncryptionWriteKey(), nonce, additionalData, plaintext.getFragment());		
		TlsAeadEncryptionResult encResult = new TlsAeadEncryptionResult(nonce_explicit, plaintext.getFragment(), encrypted);
		TlsCiphertext result = new TlsCiphertext(plaintext.getMessage(), plaintext.getVersion(), new TlsAeadFragment(encResult));
		
		return result;
	}

	@Override
	public TlsPlaintext ciphertextToPlaintext(byte[] ciphertextBytes, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry, TlsKeyExchangeAlgorithm algorithm) throws TlsBadRecordMacException, TlsDecodeErrorException {
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
		byte[] encrypted = Arrays.copyOfRange(fragmentBytes, NONCE_EXPLICIT_LENGTH, fragmentBytes.length);
		
		byte[] nonce_explicit = Arrays.copyOfRange(fragmentBytes, 0, NONCE_EXPLICIT_LENGTH);
		byte[] nonce_implicit = parameters.getWriteIv();
		byte[] nonce = ByteHelper.concatenate(nonce_implicit, nonce_explicit);
		
		byte[] tmpAddDat = Arrays.copyOfRange(headerBytes, 0, 3); //contentType, version
		byte[] lengthBytes = {headerBytes[3], headerBytes[4]};
		short length = (short)ByteHelper.twoByteArrayToInt(lengthBytes);
		//To pass the correct length of the former TlsCompressed.fragment we have to subtract the authentication tag length.
		length = (short) (length - getAuthenticationTagLength() - NONCE_EXPLICIT_LENGTH);
		tmpAddDat = ByteHelper.concatenate(tmpAddDat, ByteHelper.intToTwoByteArray(length));
		byte[] additionalData = ByteHelper.concatenate(ByteHelper.longToByteArray(parameters.getSequenceNumber()), tmpAddDat);
		
		byte[] decryptedFragment = decrypt(parameters.getEncryptionWriteKey(), nonce, additionalData, encrypted);
	
	//set correct length for fragment in plaintext
		int newLength = decryptedFragment.length;
		lengthBytes = ByteHelper.intToTwoByteArray(newLength);
		headerBytes[3] = lengthBytes[0];
		headerBytes[4] = lengthBytes[1];
		
		return new TlsPlaintext(ByteHelper.concatenate(headerBytes, decryptedFragment), registry, algorithm);
	}
	
	public abstract byte[] encrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] plaintext);
	
	public abstract byte[] decrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] ciphertext) throws TlsBadRecordMacException;

	/**
	 * Returns the length of the cipher suite dependent authentication tag.
	 * 
	 * @return the authentication tag
	 */
	public abstract int getAuthenticationTagLength();
	
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
