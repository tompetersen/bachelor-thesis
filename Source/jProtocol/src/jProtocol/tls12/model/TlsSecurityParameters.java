package jProtocol.tls12.model;

public class TlsSecurityParameters {
	
	public enum ConnectionEnd {
		server,
		client
	}
	
	public enum PrfAlgorithm {
		tls_prf_sha256
	}
	
	public enum CipherType {
		stream,
		block,
		aead
	}
	
	public enum BulkCipherAlgorithm {
		cipher_null,
		cipher_rc4,
		cipher_3des,
		cipher_aes		
	}
	
	public enum MacAlgorithm {
		mac_null,
		mac_hmac_md5,
		mac_hmac_sha1,
		mac_hmac_sha256,
		mac_hmac_sha384,
		mac_hmac_sha512
	}
	
	public enum CompressionMethod {
		compression_null
	}
	
	private ConnectionEnd _entity;
	private PrfAlgorithm _prfAlgorithm;
	private BulkCipherAlgorithm _bulkCipherAlgorithm;
	private CipherType _cipherType;
	private byte _encKeyLength;
	private byte _blockLength;
	private byte _fixedIvLength;
	private byte _recordIvLength;
	private MacAlgorithm _macAlgorithm;
	private byte _macLength;
	private byte _macKeyLength;
	//private CompressionMethod _compressionAlgorithm;
	private byte[] _masterSecret; //48
	private byte[] _clientRandom; //32
	private byte[] _serverRandom; //32
	
	public BulkCipherAlgorithm getBulkCipherAlgorithm() {
		return _bulkCipherAlgorithm;
	}
	
	public CipherType getCipherType() {
		return _cipherType;
	}
	
}
