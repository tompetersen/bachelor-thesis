package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.TlsSecurityParameters.BulkCipherAlgorithm;
import jProtocol.tls12.model.TlsSecurityParameters.MacAlgorithm;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;

/*
 * If the cipher suite is TLS_NULL_WITH_NULL_NULL, encryption consists of the identity operation 
 * (i.e., the data is not encrypted, and the MAC size is zero, implying that no MAC is used).
 * 
 * 6.2.3.1. p.22
 */
public class TlsCipherSuite_Null extends TlsStreamCipherSuite {

	@Override
	public String getName() {
		return "TLS_NULL_WITH_NULL_NULL";
	}

	@Override
	public short getDefinitionValue() {
		return 0x0000;
	}

	@Override
	public TlsStreamEncryptionResult encrypt(byte[] key, byte[] plaintext) {
		TlsStreamEncryptionResult result = new TlsStreamEncryptionResult();
		result.content = plaintext;
		result.mac = null;
		result.result = plaintext;
		
		return result;
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] ciphertext) throws TlsBadRecordMacException {
		return ciphertext;
	}

	@Override
	public BulkCipherAlgorithm getBulkCipherAlgorithm() {
		return null;
	}

	@Override
	public byte getEncryptKeyLength() {
		return 0;
	}

	@Override
	public MacAlgorithm getMacAlgorithm() {
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

}
