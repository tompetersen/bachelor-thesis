package jProtocol.tls12.model.ciphersuites;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.fragments.TlsAeadFragment;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsMacAlgorithm;

/*
 *  additional_data = seq_num + TLSCompressed.type + TLSCompressed.version + TLSCompressed.length;
 *  
 *  Nonce? explicit, implicit - how to compute?
 */
public abstract class TlsAeadCipherSuite implements TlsCipherSuite {

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
	public TlsPlaintext ciphertextToPlaintext(byte[] ciphertextBytes, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry, TlsKeyExchangeAlgorithm algorithm) throws TlsBadRecordMacException {
	//TODO: same operations as in TlsBlockCipherSuite? Extract?
		
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
	
	public abstract byte[] encrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] plaintext);
	
	public abstract byte[] decrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] ciphertext) throws TlsBadRecordMacException;

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
