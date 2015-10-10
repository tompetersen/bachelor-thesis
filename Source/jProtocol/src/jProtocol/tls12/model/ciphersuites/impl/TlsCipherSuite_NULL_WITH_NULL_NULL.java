package jProtocol.tls12.model.ciphersuites.impl;

import jProtocol.tls12.model.ciphersuites.TlsStreamCipherSuite;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsMacAlgorithm;


public class TlsCipherSuite_NULL_WITH_NULL_NULL extends TlsStreamCipherSuite {

	/*
	 * If the cipher suite is TLS_NULL_WITH_NULL_NULL, encryption consists of the identity operation 
	 * (i.e., the data is not encrypted, and the MAC size is zero, implying that no MAC is used).
	 * 
	 * 6.2.3.1. p.22
	 */
	
	@Override
	public String getName() {
		return "TLS_NULL_WITH_NULL_NULL";
	}

	@Override
	public short getCode() {
		return 0x0000;
	}

	@Override
	public TlsStreamEncryptionResult encrypt(byte[] key, byte[] plaintext) {
		TlsStreamEncryptionResult result = new TlsStreamEncryptionResult();
		result.content = plaintext;
		result.mac = new byte[0];
		result.streamCiphered = plaintext;
		
		return result;
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] ciphertext) throws TlsBadRecordMacException {
		return ciphertext;
	}

	@Override
	public TlsBulkCipherAlgorithm getBulkCipherAlgorithm() {
		return null;
	}

	@Override
	public byte getEncryptKeyLength() {
		return 0;
	}

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
	public TlsKeyExchangeAlgorithm getKeyExchangeAlgorithm() {
		return null;
	}
	
	
}
