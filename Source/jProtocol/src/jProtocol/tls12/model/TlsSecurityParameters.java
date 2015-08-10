package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsMacAlgorithm;
import jProtocol.tls12.model.values.TlsRandom;

public class TlsSecurityParameters {
	
	public enum PrfAlgorithm {
		tls_prf_sha256 //used in all TLS 1.2 cipher suites
	}
	
	public enum CompressionMethod {
		compression_null
	}
	
	private TlsConnectionEnd _entity;
	private TlsCipherSuite _cipherSuite;
	
	//private PrfAlgorithm _prfAlgorithm;
	//private CompressionMethod _compressionAlgorithm;
	
	private byte[] _masterSecret; //48
	private TlsRandom _clientRandom; //32
	private TlsRandom _serverRandom; //32
	
	public TlsSecurityParameters(TlsConnectionEnd connectionEnd) {
		_entity = connectionEnd;
	}
	
	public void setCipherSuite(TlsCipherSuite cipherSuite) {
		if (cipherSuite == null) {
			throw new IllegalArgumentException("Ciphersuite must not be null!");
		}
		_cipherSuite = cipherSuite;
	}
	
	public TlsBulkCipherAlgorithm getBulkCipherAlgorithm() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getBulkCipherAlgorithm();
	}
	
	public TlsCipherType getCipherType() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getCipherType();
	}
	
	public byte getEncKeyLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getEncryptKeyLength();
	}

	public byte getBlockLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getBlockLength();
	}

	public byte getFixedIvLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getFixedIvLength();
	}

	public byte getRecordIvLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getRecordIvLength();
	}

	public TlsMacAlgorithm getMacAlgorithm() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getMacAlgorithm();
	}

	public byte getMacLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getMacLength();
	}

	public byte getMacKeyLength() {
		if (_cipherSuite == null) {
			throw new RuntimeException("Cipher Suite must be set first!");
		}
		return _cipherSuite.getMacKeyLength();
	}
	
	public void setClientRandom(TlsRandom clientRandom) {
		if (clientRandom == null) {
			throw new IllegalArgumentException("Invalid client random value! Must not be null!");
		}
		this._clientRandom = clientRandom;
	}

	public void setServerRandom(TlsRandom serverRandom) {
		if (serverRandom == null) {
			throw new IllegalArgumentException("Invalid server random value! Must not be null!");
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
				ByteHelper.concatenate(_clientRandom.getBytes(), _serverRandom.getBytes()), 
				48);
	}

	public byte[] getMasterSecret() {
		if (_masterSecret == null) {
			throw new RuntimeException("Master secret must be computed first!");
		}
		return _masterSecret;
	}

	public TlsRandom getClientRandom() {
		if (_clientRandom == null) {
			throw new RuntimeException("Client random must be set first!");
		}
		return _clientRandom;
	}

	public TlsRandom getServerRandom() {
		if (_serverRandom == null) {
			throw new RuntimeException("Server random must be set first!");
		}
		return _serverRandom;
	}

	public TlsConnectionEnd getEntity() {
		return _entity;
	}
}
