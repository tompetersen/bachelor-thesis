package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsHash;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;
import java.util.ArrayList;
import java.util.List;

public class TlsSecurityParameters {
	
	private static final int VERIFY_DATA_LENGTH = 12;
	
	public enum PrfAlgorithm {
		tls_prf_sha256 //used in all TLS 1.2 cipher suites
	}
	
	public enum CompressionMethod {
		compression_null
	}
	
	//private PrfAlgorithm _prfAlgorithm;
	//private CompressionMethod _compressionAlgorithm;
	
	private TlsHash _hash;

	private TlsSessionId _sessionId;
	private TlsVersion _version;
	
	private byte[] _premastersecret;
	private byte[] _masterSecret; //48
	private TlsRandom _clientRandom; //32
	private TlsRandom _serverRandom; //32

	private List<TlsHandshakeMessage> _currentHandshakeMessages;
	
	public TlsSecurityParameters() {
		_hash = new TlsHash();
		
		_currentHandshakeMessages = new ArrayList<>();
	}
	
	public TlsSessionId getSessionId() {
		if (_sessionId == null) {
			throw new RuntimeException("SessionID must be set first!");
		}
		return _sessionId;
	}

	public void setSessionId(TlsSessionId sessionId) {
		if (sessionId == null) {
			throw new IllegalArgumentException("SessionID must be set!");
		}
		_sessionId = sessionId;
	}
	
	public boolean hasSessionId() {
		return (_sessionId != null);
	}
	
	public TlsVersion getVersion() {
		if (_version == null) {
			throw new RuntimeException("Version must be set first!");
		}
		return _version;
	}
	
	public boolean hasVersion() {
		return (_version != null);
	}

	public void setVersion(TlsVersion version) {
		if (version == null) {
			throw new IllegalArgumentException("Version must be set!");
		}
		_version = version;
	}
	
/*
 * Random and mastersecret methods
 */
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
	
	public boolean hasClientRandom() {
		return (_clientRandom != null);
	}
	
	public boolean hasServerRandom() {
		return (_serverRandom != null);
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
	
	public boolean hasSetPreMasterSecret() {
		return (_premastersecret != null);
	}
	
	public byte[] getPreMasterSecret() {
		if (_premastersecret == null) {
			throw new RuntimeException("Pre master secret must be set first!");
		}
		return _premastersecret;
	}

	public void computeMasterSecret(byte[] premastersecret) {
		_premastersecret = premastersecret;
		
		if (_clientRandom == null || _serverRandom == null) {
			throw new RuntimeException("Client and server random values must be set before computing the master secret!");
		}
		if (premastersecret == null) {
			throw new IllegalArgumentException("Premastersecret must not be null!");
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
	
	public boolean hasComputedMasterSecret() {
		return (_masterSecret != null);
	}
	
/*
 * Handshake verification messages
 */
	public void addHandshakeMessageBytes(TlsHandshakeMessage message) {
		_currentHandshakeMessages.add(message);
	}

	public byte[] getFinishedVerifyDataForServer(byte[] masterSecret) {
		byte[] verifyData = TlsPseudoRandomFunction.prf(masterSecret,
				"server finished",
				_hash.hash(getBytesFromHandshakeMessages()), 
				VERIFY_DATA_LENGTH);
		
		return verifyData;
	}
	
	public byte[] getFinishedVerifyDataForClient(byte[] masterSecret) {
		byte[] verifyData = TlsPseudoRandomFunction.prf(masterSecret,
				"client finished",
				_hash.hash(getBytesFromHandshakeMessages()), 
				VERIFY_DATA_LENGTH);
		
		return verifyData;
	}
	
	private byte[] getBytesFromHandshakeMessages() {
		byte[] result = new byte[0];
		
		for (TlsHandshakeMessage message : _currentHandshakeMessages) {
			result = ByteHelper.concatenate(result, message.getBytes());
		}
		
		return result;
	}
}
