package jProtocol.tls12.model.ciphersuites.impl;

import jProtocol.tls12.model.TlsCipher;
import jProtocol.tls12.model.TlsMac;
import jProtocol.tls12.model.TlsMacParameters;
import jProtocol.tls12.model.TlsSecurityParameters.BulkCipherAlgorithm;
import jProtocol.tls12.model.TlsSecurityParameters.MacAlgorithm;
import jProtocol.tls12.model.ciphersuites.TlsBlockCipherSuite;

/*
 *  Cipher Suite definitions
 *  appendix C, p. 83-84
 */
public class TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA extends TlsBlockCipherSuite {

	private TlsMac _mac;
	private TlsCipher _cipher;
	
	public TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA() {
		_mac = new TlsMac(getMacAlgorithm());
		_cipher = new TlsCipher(getBulkCipherAlgorithm(), getCipherType());
	}
	
	@Override
	public byte[] computeMac(TlsMacParameters parameters) {
		return _mac.computeTlsMac(parameters);
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] iv,  byte[] plaintext) {
		return _cipher.encrypt(key, iv, plaintext);
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] iv, byte[] ciphertext) {
		return _cipher.decrypt(key, iv, ciphertext);
	}

	@Override
	public String getName() {
		return "TLS_RSA_WITH_AES_128_CBC_SHA";
	}

	@Override
	public short getCode() {
		return 0x002F;
	}

	@Override
	public BulkCipherAlgorithm getBulkCipherAlgorithm() {
		return BulkCipherAlgorithm.cipher_aes;
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
	public byte getRecordIvLength() {
		return (byte)16;
	}

	@Override
	public MacAlgorithm getMacAlgorithm() {
		return MacAlgorithm.mac_hmac_sha1;
	}

	@Override
	public byte getMacLength() {
		return (byte)20;
	}

	@Override
	public byte getMacKeyLength() {
		return (byte)20;
	}
}
