package jProtocol.tls12.model.ciphersuites.impl;

import jProtocol.tls12.model.ciphersuites.TlsAeadCipherSuite;
import jProtocol.tls12.model.crypto.TlsAesGcmCipher;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;

public class TlsCipherSuite_DHE_RSA_WITH_AES_128_GCM_SHA256 extends TlsAeadCipherSuite {

	private TlsAesGcmCipher _cipher;
	
	public TlsCipherSuite_DHE_RSA_WITH_AES_128_GCM_SHA256() {
		_cipher = new TlsAesGcmCipher(); 
	}

	@Override
	public String getName() {
		return "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256";
	}

	@Override
	public short getCode() {
		return 0x009E;
	}

	@Override
	public TlsBulkCipherAlgorithm getBulkCipherAlgorithm() {
		return TlsBulkCipherAlgorithm.cipher_aes;
	}

	@Override
	public byte getEncryptKeyLength() {
		return (byte)16;
	}

	@Override
	public byte getBlockLength() {
		return (byte)16;
	}

	@Override
	public byte getFixedIvLength() {
		/*	struct {
	             opaque salt[4];
	             opaque nonce_explicit[8];
	          } GCMNonce;
	          
			The salt is the "implicit" part of the nonce and is not sent in the
			packet.  Instead, the salt is generated as part of the handshake
			process: it is either the client_write_IV (when the client is
			sending) or the server_write_IV (when the server is sending).  The
			salt length (SecurityParameters.fixed_iv_length) is 4 octets.
		  */
		return (byte)4;
	}
	
	@Override
	public TlsKeyExchangeAlgorithm getKeyExchangeAlgorithm() {
		return TlsKeyExchangeAlgorithm.dhe_rsa;
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] plaintext) {
		return _cipher.encrypt(key, nonce, additionalData, plaintext);
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] ciphertext) throws TlsBadRecordMacException {
		return _cipher.decrypt(key, nonce, additionalData, ciphertext);
	}

}
