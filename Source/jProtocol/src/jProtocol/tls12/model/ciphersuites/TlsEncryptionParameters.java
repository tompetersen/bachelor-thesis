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

	public long getSequenceNumber() {
		return _sequenceNumber;
	}

	public byte[] getEncryptionWriteKey() {
		return _encryptionWriteKey;
	}

	public byte[] getMacWriteKey() {
		return _macWriteKey;
	}

	public byte[] getWriteIv() {
		return _writeIv;
	}
}
