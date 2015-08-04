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
	
	private TlsCipherSuite _cipherSuite;
	
	public BulkCipherAlgorithm getBulkCipherAlgorithm() {
		return _bulkCipherAlgorithm;
	}
	
	public CipherType getCipherType() {
		return _cipherType;
	}
	
	public byte getEncKeyLength() {
		return _encKeyLength;
	}

	public byte getBlockLength() {
		return _blockLength;
	}

	public byte getFixedIvLength() {
		return _fixedIvLength;
	}

	public byte getRecordIvLength() {
		return _recordIvLength;
	}

	public MacAlgorithm getMacAlgorithm() {
		return _macAlgorithm;
	}

	public byte getMacLength() {
		return _macLength;
	}

	public byte getMacKeyLength() {
		return _macKeyLength;
	}
	
	public void setClientRandom(byte[] clientRandom) {
		if (clientRandom == null || clientRandom.length != 32) {
			throw new IllegalArgumentException("Invalid client random value! Must be 32 bytes long!");
		}
		this._clientRandom = clientRandom;
	}

	public void setServerRandom(byte[] serverRandom) {
		if (serverRandom == null || serverRandom.length != 32) {
			throw new IllegalArgumentException("Invalid server random value! Must be 32 bytes long!");
		}
		this._serverRandom = serverRandom;
	}

	public void setCipherSuite(TlsCipherSuite cipherSuite) {
		if (cipherSuite == null) {
			throw new IllegalArgumentException("Ciphersuite must not be null!");
		}
		this._cipherSuite = cipherSuite;
	}

	public void computeMasterSecret(byte[] premastersecret) {
		if (_clientRandom == null || _serverRandom == null) {
			throw new RuntimeException("Client and server random values must be set before computing the master secret!");
		}
		if (premastersecret == null || premastersecret.length != 48) {
			throw new IllegalArgumentException("Invalid premastersecret! Must be 48 bytes long!");
		}
		
		/*
		 * According to chapter 8.1. (p.64) of TLS 1.2 specification
		 */
		_masterSecret = TlsPseudoRandomFunction.prf(premastersecret, 
				"master secret", 
				TlsPseudoRandomFunction.concatenate(_clientRandom, _serverRandom), 
				48);
		
		computeKeys();
	}
	
	private void computeKeys() {
		//TODO:
	}
	
}
