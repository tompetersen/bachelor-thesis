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

	/*
	 * //TODO: Used for compression-> Implement if necessary
	 * private enum PrfAlgorithm {
	 * tls_prf_sha256 //used in all TLS 1.2 cipher suites
	 * }
	 * 
	 * private enum CompressionMethod {
	 * compression_null
	 * }
	 * 
	 * private PrfAlgorithm _prfAlgorithm;
	 * private CompressionMethod _compressionAlgorithm;
	 */

	private TlsHash _hash;

	private TlsSessionId _sessionId;
	private TlsVersion _version;

	private byte[] _premastersecret;
	private byte[] _masterSecret; // 48
	private TlsRandom _clientRandom; // 32
	private TlsRandom _serverRandom; // 32

	private List<TlsHandshakeMessage> _currentHandshakeMessages;

	/**
	 * Creates a new security parameters object.
	 */
	public TlsSecurityParameters() {
		_hash = new TlsHash();

		_currentHandshakeMessages = new ArrayList<>();
	}

	/**
	 * Returns the session ID. Must have been set before.
	 * 
	 * @return the session ID
	 */
	public TlsSessionId getSessionId() {
		if (_sessionId == null) {
			throw new RuntimeException("SessionID must be set first!");
		}
		return _sessionId;
	}

	/**
	 * Sets the session ID.
	 * 
	 * @param sessionId the session ID. Must not be null.
	 */
	public void setSessionId(TlsSessionId sessionId) {
		if (sessionId == null) {
			throw new IllegalArgumentException("SessionID must be set!");
		}
		_sessionId = sessionId;
	}

	/**
	 * Returns whether a session ID has been set.
	 * 
	 * @return true, if a session ID has been set
	 */
	public boolean hasSessionId() {
		return (_sessionId != null);
	}

	/**
	 * Returns the used TLS version. Must have been set before.
	 * 
	 * @return the version
	 */
	public TlsVersion getVersion() {
		if (_version == null) {
			throw new RuntimeException("Version must be set first!");
		}
		return _version;
	}

	/**
	 * Returns whether a version has been set.
	 * 
	 * @return true, if a TLS version has been set
	 */
	public boolean hasVersion() {
		return (_version != null);
	}

	/**
	 * Sets the TLS version.
	 * 
	 * @param version the version. Must not be null.
	 */
	public void setVersion(TlsVersion version) {
		if (version == null) {
			throw new IllegalArgumentException("Version must be set!");
		}
		_version = version;
	}

	/*
	 * Random and mastersecret methods
	 */
	/**
	 * Sets the client random value.
	 * 
	 * @param clientRandom the client random. Must not be null.
	 */
	public void setClientRandom(TlsRandom clientRandom) {
		if (clientRandom == null) {
			throw new IllegalArgumentException("Invalid client random value! Must not be null!");
		}
		this._clientRandom = clientRandom;
	}

	/**
	 * Sets the server random value.
	 * 
	 * @param clientRandom the server random. Must not be null.
	 */
	public void setServerRandom(TlsRandom serverRandom) {
		if (serverRandom == null) {
			throw new IllegalArgumentException("Invalid server random value! Must not be null!");
		}
		this._serverRandom = serverRandom;
	}

	/**
	 * Returns whether a client random value has been set.
	 * 
	 * @return true, if a client random value has been set
	 */
	public boolean hasClientRandom() {
		return (_clientRandom != null);
	}

	/**
	 * Returns whether a server random value has been set.
	 * 
	 * @return true, if a server random value has been set
	 */
	public boolean hasServerRandom() {
		return (_serverRandom != null);
	}

	/**
	 * Returns the client random value. Must have been set before.
	 * 
	 * @return the client random
	 */
	public TlsRandom getClientRandom() {
		if (_clientRandom == null) {
			throw new RuntimeException("Client random must be set first!");
		}
		return _clientRandom;
	}

	/**
	 * Returns the server random value. Must have been set before.
	 * 
	 * @return the server random
	 */
	public TlsRandom getServerRandom() {
		if (_serverRandom == null) {
			throw new RuntimeException("Server random must be set first!");
		}
		return _serverRandom;
	}

	/**
	 * Returns whether the pre master secret has been set.
	 * 
	 * @return true, if the pre master secret has been set
	 */
	public boolean hasSetPreMasterSecret() {
		return (_premastersecret != null);
	}

	/**
	 * Returns the pre master secret. Must have been set before when computing
	 * the master secret.
	 * 
	 * @return the pre master secret
	 */
	public byte[] getPreMasterSecret() {
		if (_premastersecret == null) {
			throw new RuntimeException("Pre master secret must be set first!");
		}
		return _premastersecret;
	}

	/**
	 * Computes the master secret from pre master secret and random values.
	 * Server and client random values must be set.
	 * 
	 * @param premastersecret the pre master secret
	 */
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
		_masterSecret = TlsPseudoRandomFunction.prf(premastersecret, "master secret", ByteHelper.concatenate(_clientRandom.getBytes(), _serverRandom.getBytes()), 48);
	}

	/**
	 * Returns the master secret. Must have been set before.
	 * 
	 * @return the master secret
	 */
	public byte[] getMasterSecret() {
		if (_masterSecret == null) {
			throw new RuntimeException("Master secret must be computed first!");
		}
		return _masterSecret;
	}

	/**
	 * Returns whether the pre master secret has been set.
	 * 
	 * @return true, if the pre master secret has been set
	 */
	public boolean hasComputedMasterSecret() {
		return (_masterSecret != null);
	}

	/*
	 * Handshake verification messages
	 */
	/**
	 * Adds a handshake message to the ones which are verified in the finished
	 * messages.
	 * 
	 * @param message the message
	 */
	public void addHandshakeMessageForVerification(TlsHandshakeMessage message) {
		_currentHandshakeMessages.add(message);
	}

	/**
	 * Returns the verify data for the server finished message computed from the
	 * added handshake messages.
	 * 
	 * @return the server verify data
	 */
	public byte[] getFinishedVerifyDataForServer() {
		byte[] verifyData = TlsPseudoRandomFunction.prf(_masterSecret, "server finished", _hash.hash(getBytesFromHandshakeMessages()), VERIFY_DATA_LENGTH);

		return verifyData;
	}

	/**
	 * Returns the verify data for the client finished message computed from the
	 * added handshake messages.
	 * 
	 * @return the client verify data
	 */
	public byte[] getFinishedVerifyDataForClient() {
		byte[] verifyData = TlsPseudoRandomFunction.prf(_masterSecret, "client finished", _hash.hash(getBytesFromHandshakeMessages()), VERIFY_DATA_LENGTH);

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
