package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsMacAlgorithm;

public class TlsSecurityParameters {
	
	public enum PrfAlgorithm {
		tls_prf_sha256
	}
	
	public enum CompressionMethod {
		compression_null
	}
	
	private TlsConnectionEnd _entity;
	
	//private PrfAlgorithm _prfAlgorithm;
	//private CompressionMethod _compressionAlgorithm;
	
	private byte[] _masterSecret; //48
	private byte[] _clientRandom; //32
	private byte[] _serverRandom; //32
	
	private TlsCipherSuite _cipherSuite;
	
	public TlsSecurityParameters(TlsConnectionEnd connectionEnd) {
		_entity = connectionEnd;
		//_prfAlgorithm = PrfAlgorithm.tls_prf_sha256; //used in all TLS 1.2 cipher suites
	}
	
	public void setCipherSuite(TlsCipherSuite cipherSuite) {
		if (cipherSuite == null) {
			throw new IllegalArgumentException("Ciphersuite must not be null!");
		}
		this._cipherSuite = cipherSuite;
	}
	
	public TlsBulkCipherAlgorithm getBulkCipherAlgorithm() {
		return _cipherSuite.getBulkCipherAlgorithm();
	}
	
	public TlsCipherType getCipherType() {
		return _cipherSuite.getCipherType();
	}
	
	public byte getEncKeyLength() {
		return _cipherSuite.getEncryptKeyLength();
	}

	public byte getBlockLength() {
		return _cipherSuite.getBlockLength();
	}

	public byte getFixedIvLength() {
		return _cipherSuite.getFixedIvLength();
	}

	public byte getRecordIvLength() {
		return _cipherSuite.getRecordIvLength();
	}

	public TlsMacAlgorithm getMacAlgorithm() {
		return _cipherSuite.getMacAlgorithm();
	}

	public byte getMacLength() {
		return _cipherSuite.getMacLength();
	}

	public byte getMacKeyLength() {
		return _cipherSuite.getMacKeyLength();
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
				ByteHelper.concatenate(_clientRandom, _serverRandom), 
				48);
	}

	public byte[] getMasterSecret() {
		if (_masterSecret == null) {
			throw new RuntimeException("Master secret must be computed first!");
		}
		return _masterSecret;
	}

	public byte[] getClientRandom() {
		if (_clientRandom == null) {
			throw new RuntimeException("Client random must be set first!");
		}
		return _clientRandom;
	}

	public byte[] getServerRandom() {
		if (_serverRandom == null) {
			throw new RuntimeException("Server random must be set first!");
		}
		return _serverRandom;
	}

	public TlsConnectionEnd getEntity() {
		return _entity;
	}
}
