package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;
import java.nio.ByteBuffer;

public class TlsServerHelloMessage extends TlsHandshakeMessage {

	private TlsVersion _serverVersion;	//2
	private TlsRandom _serverRandom; //4 + 28
	private TlsSessionId _sessionId; // empty or 1..32 [Length: 1]
	private TlsCipherSuite _cipherSuite; // 2

	// private byte _compressionMethod; //1
	// private Extension[] _extensions;	//0.. [Length: 2]
	
	public TlsServerHelloMessage(TlsVersion serverVersion, TlsRandom serverRandom, TlsSessionId sessionId, TlsCipherSuite cipherSuite) {
		if (serverVersion == null) {
			throw new IllegalArgumentException("Server version must be set!");
		}
		if (serverRandom == null) {
			throw new IllegalArgumentException("Server random must be set!");
		}
		if (sessionId == null) {
			throw new IllegalArgumentException("SessionID must be set!");
		}
		if (cipherSuite == null) {
			throw new IllegalArgumentException("CipherSuite must be set!");
		}
		
		_serverVersion = serverVersion;
		_serverRandom = serverRandom;
		_sessionId = sessionId;
		_cipherSuite = cipherSuite;
	}
	
	public TlsServerHelloMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO Parsing
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.server_hello;
	}

	@Override
	public byte[] getBodyBytes() {
		ByteBuffer b = ByteBuffer.allocate(2 + 4 + 28 + 1 + _sessionId.getLength() + 2);
		
		//version
		b.put(_serverVersion.getMajorVersion());
		b.put(_serverVersion.getMinorVersion());
		
		//random
		b.put(_serverRandom.getBytes());
		
		//sessionId
		b.put((byte)_sessionId.getLength());
		if (!_sessionId.isEmpty()) {
			b.put(_sessionId.getSessionId());
		}
		
		//cipherSuites
		b.putShort(_cipherSuite.getCode());
		
		return b.array();
	}
	
	public TlsVersion getServerVersion() {
		return _serverVersion;
	}

	public TlsRandom getServerRandom() {
		return _serverRandom;
	}

	public TlsSessionId getSessionId() {
		return _sessionId;
	}

	public TlsCipherSuite getCipherSuite() {
		return _cipherSuite;
	}

}
