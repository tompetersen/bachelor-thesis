package jProtocol.tls12.model.fragments;

import jProtocol.tls12.model.ciphersuites.TlsStreamCipherSuite.TlsStreamEncryptionResult;

public class TlsStreamFragment extends TlsFragment {

	private TlsStreamEncryptionResult _encryptionResult;
	
	public TlsStreamFragment(TlsStreamEncryptionResult encResult) {
		_encryptionResult = encResult;
	}

	@Override
	public byte[] getBytes() {
		return _encryptionResult.streamCiphered;
	}
	
	/**
	 * Returns the computed MAC for the fragment.
	 * 
	 * @return the MAC
	 */
	public byte[] getMac() {
		return _encryptionResult.mac;
	}

	@Override
	public int getLength() {
		return _encryptionResult.streamCiphered.length;
	}

	@Override
	public byte[] getContent() {
		return _encryptionResult.content;
	}
}
