package jProtocol.tls12.model.fragments;

import jProtocol.tls12.model.ciphersuites.TlsBlockCipherSuite.TlsBlockEncryptionResult;

public class TlsBlockFragment extends TlsFragment {

	private TlsBlockEncryptionResult _encryptionResult;
	
	public TlsBlockFragment(TlsBlockEncryptionResult encResult) {
		_encryptionResult = encResult;
	}

	@Override
	public byte[] getBytes() {
		return _encryptionResult.result;
	}
	
	/**
	 * Returns the initialization vector used for the encryption.
	 * 
	 * @return the iv
	 */
	public byte[] getIv() {
		return _encryptionResult.iv;
	}

	/**
	 * Returns the computed MAC for the fragment.
	 * 
	 * @return the MAC
	 */
	public byte[] getMac() {
		return _encryptionResult.mac;
	}

	/**
	 * Returns the computed padding for the fragment.
	 * 
	 * @return the padding
	 */
	public byte[] getPadding() {
		return _encryptionResult.padding;
	}

	/**
	 * Returns the used padding length for the fragment.
	 * 
	 * @return the padding length
	 */
	public byte getPaddingLength() {
		return _encryptionResult.paddingLength;
	}

	@Override
	public int getLength() {
		return _encryptionResult.result.length;
	}

	@Override
	public byte[] getContent() {
		return _encryptionResult.content;
	}
	
	

}
