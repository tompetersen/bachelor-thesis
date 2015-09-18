package jProtocol.tls12.model.ciphersuites;

public class TlsEncryptionParameters {

	private long _sequenceNumber;
	private byte[] _encryptionWriteKey;
	private byte[] _macWriteKey;
	private byte[] _writeIv;

	public TlsEncryptionParameters(long sequenceNumber, byte[] encryptionWriteKey, byte[] macWriteKey, byte[] writeIv) {
		_sequenceNumber = sequenceNumber;
		_encryptionWriteKey = encryptionWriteKey;
		_macWriteKey = macWriteKey;
		_writeIv = writeIv;
	}
	
	/**
	 * The current write or read sequence number.
	 * 
	 * @return the sequence number
	 */
	public long getSequenceNumber() {
		return _sequenceNumber;
	}

	/**
	 * The server or client write encryption key.
	 * 
	 * @return the write encryption key
	 */
	public byte[] getEncryptionWriteKey() {
		return _encryptionWriteKey;
	}

	/**
	 * The server or client write MAC key.
	 * 
	 * @return the write mac key
	 */
	public byte[] getMacWriteKey() {
		return _macWriteKey;
	}

	/**
	 * The server or client write IV used as implicit nonce for AEAD ciphers.
	 * 
	 * @return the write IV
	 */
	public byte[] getWriteIv() {
		return _writeIv;
	}
}
