package jProtocol.tls12.model.fragments;

import jProtocol.tls12.model.ciphersuites.TlsAeadCipherSuite.TlsAeadEncryptionResult;

public class TlsAeadFragment extends TlsFragment {

	private TlsAeadEncryptionResult _encryptionResult;
	
	public TlsAeadFragment(TlsAeadEncryptionResult encResult) {
		_encryptionResult = encResult;
	}

	@Override
	public byte[] getBytes() {
		return _encryptionResult.aeadCiphered;
	}
	
	public byte[] getNonceExplicit() {
		return _encryptionResult.nonce;
	}

	@Override
	public int getLength() {
		return _encryptionResult.aeadCiphered.length;
	}

	@Override
	public byte[] getContent() {
		return _encryptionResult.content;
	}

}
