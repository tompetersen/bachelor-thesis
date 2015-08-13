package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;

import java.nio.ByteBuffer;
import java.util.List;

public class TlsClientHelloMessage extends TlsHandshakeMessage {

	private TlsVersion _clientVersion;	//2
	private TlsRandom _clientRandom; //4 + 28
	private TlsSessionId _sessionId; // empty or 1..32 [Length: 1]
	private List<TlsCipherSuite> _cipherSuites; // 1.. [Length: 2]

	// private byte[] _compressionMethod; //1.. [Length: 1]
	// private Extension[] _extensions;	//0.. [Length: 2]
	
	public TlsClientHelloMessage(TlsVersion clientVersion, TlsRandom clientRandom, TlsSessionId sessionId, List<TlsCipherSuite> cipherSuites) {
		if (clientVersion == null) {
			throw new IllegalArgumentException("Client version must be set!");
		}
		if (clientRandom == null) {
			throw new IllegalArgumentException("Client random must be set!");
		}
		if (sessionId == null) {
			throw new IllegalArgumentException("Cipher Suites must not be empty!");
		}
		if (cipherSuites == null || cipherSuites.size() < 1) {
			throw new IllegalArgumentException("Cipher Suites must not be empty!");
		}

		_clientVersion = clientVersion;
		_clientRandom = clientRandom;
		_sessionId = sessionId;
		_cipherSuites = cipherSuites;
	}
	
	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.client_hello;
	}

	@Override
	public byte[] getBodyBytes() {
		ByteBuffer b = ByteBuffer.allocate(2 + 4 + 28 + 1 + _sessionId.getLength() + 2 + 2 * _cipherSuites.size());
		
		//version
		b.put(_clientVersion.getMajorVersion());
		b.put(_clientVersion.getMinorVersion());
		
		//random
		b.put(_clientRandom.getBytes());
		
		//sessionId
		b.put((byte)_sessionId.getLength());
		if (!_sessionId.isEmpty()) {
			b.put(_sessionId.getSessionId());
		}
		
		//cipherSuites
		int cslength = 2 *  _cipherSuites.size(); // * 2 because number of bytes is used
		b.put((byte) ((cslength & 0xFF00) >> 8));
		b.put((byte) (cslength & 0xFF));
		for (TlsCipherSuite cs : _cipherSuites) {
			b.putShort(cs.getCode());
		}
		
		return b.array();
	}
	
	public TlsVersion getClientVersion() {
		return _clientVersion;
	}

	public TlsRandom getClientRandom() {
		return _clientRandom;
	}

	public TlsSessionId getSessionId() {
		return _sessionId;
	}

	public List<TlsCipherSuite> getCipherSuites() {
		return _cipherSuites;
	}


}
