package jProtocol.tls12.model.ciphersuites.impl;

import jProtocol.tls12.model.ciphersuites.TlsAeadCipherSuite;
import jProtocol.tls12.model.crypto.TlsAesGcmCipher;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;

public class TlsCipherSuite_RSA_WITH_AES_128_GCM_SHA256 extends TlsAeadCipherSuite {

	private TlsAesGcmCipher _cipher;
	
	public TlsCipherSuite_RSA_WITH_AES_128_GCM_SHA256() {
		_cipher = new TlsAesGcmCipher(); 
	}

	@Override
	public String getName() {
		return "TLS_RSA_WITH_AES_128_GCM_SHA256";
	}

	@Override
	public short getCode() {
		return 0x009C;
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
		return (byte)4;
	}

	@Override
	public TlsKeyExchangeAlgorithm getKeyExchangeAlgorithm() {
		return TlsKeyExchangeAlgorithm.rsa;
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] plaintext) {
		return _cipher.encrypt(key, nonce, additionalData, plaintext);
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] nonce, byte[] additionalData, byte[] ciphertext) throws TlsBadRecordMacException {
		return _cipher.decrypt(key, nonce, additionalData, ciphertext);
	}

	@Override
	public int getAuthenticationTagLength() {
		return TlsAesGcmCipher.GCM_TAG_LENGTH;
	}

}
