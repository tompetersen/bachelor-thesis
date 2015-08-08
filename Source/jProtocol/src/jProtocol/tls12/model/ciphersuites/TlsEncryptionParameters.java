package jProtocol.tls12.model.ciphersuites;

public class TlsEncryptionParameters {

	public long sequenceNumber;
	public byte[] encryptionWriteKey;
	public byte[] macWriteKey;
	public byte[] writeIv;

	public TlsEncryptionParameters(long sequenceNumber, byte[] encryptionWriteKey, byte[] macWriteKey, byte[] writeIv) {
		this.sequenceNumber = sequenceNumber;
		this.encryptionWriteKey = encryptionWriteKey;
		this.macWriteKey = macWriteKey;
		this.writeIv = writeIv;
	}
}
