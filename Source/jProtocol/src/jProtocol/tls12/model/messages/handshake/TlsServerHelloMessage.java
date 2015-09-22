package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TlsServerHelloMessage extends TlsHandshakeMessage {

	/*
	 struct {
          ProtocolVersion server_version;
          Random random;
          SessionID session_id;
          CipherSuite cipher_suite;
          CompressionMethod compression_method;
          select (extensions_present) {
              case false:
                  struct {};
              case true:
                  Extension extensions<0..2^16-1>;
          };
      } ServerHello;
	 */
	
	private static final int VERSION_LENGTH = 2;
	private static final int UNIX_TIME_LENGTH = 4;
	private static final int RANDOM_LENGTH = 28;
	private static final int SESSION_ID_LENGTH_FIELD_LENGTH = 1;
	private static final int CIPHER_SUITE_LENGTH = 2;
	
	private TlsVersion _serverVersion;		//2
	private TlsRandom _serverRandom; 		//4 + 28
	private TlsSessionId _sessionId; 		// empty or 1..32 [Length: 1]
	private TlsCipherSuite _cipherSuite; 	// 2

	// private byte _compressionMethod; 	//1
	// private Extension[] _extensions;		//0.. [Length: 2]
	
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
	
	public TlsServerHelloMessage(byte[] unparsedContent, TlsCipherSuiteRegistry registry) throws TlsDecodeErrorException {
		super(unparsedContent);

		int parsedBytes = 0;
		
		//initial length check
		int unparsedLength = unparsedContent.length;
		if (unparsedLength < VERSION_LENGTH + UNIX_TIME_LENGTH + RANDOM_LENGTH + SESSION_ID_LENGTH_FIELD_LENGTH) { //version, random, sessionId length field
			throw new TlsDecodeErrorException("Invalid server hello message - contains not enough information!");
		}
		
		//parse version
		TlsVersion version = new TlsVersion(unparsedContent[0], unparsedContent[1]);
		parsedBytes += VERSION_LENGTH;
		
		//parse server random
		byte[] unixtimeBytes = new byte[UNIX_TIME_LENGTH];
		byte[] randomBytes = new byte[RANDOM_LENGTH];
		System.arraycopy(unparsedContent, parsedBytes, unixtimeBytes, 0, UNIX_TIME_LENGTH);
		parsedBytes += UNIX_TIME_LENGTH;
		System.arraycopy(unparsedContent, parsedBytes, randomBytes, 0, RANDOM_LENGTH);
		parsedBytes += RANDOM_LENGTH;
		
		TlsRandom random = new TlsRandom(ByteHelper.byteArrayToInt(unixtimeBytes), randomBytes);
		
		//parse session id
		int sessionIdLength = unparsedContent[parsedBytes]; //just one byte
		parsedBytes += SESSION_ID_LENGTH_FIELD_LENGTH;
		
		if (parsedBytes + sessionIdLength > unparsedLength + CIPHER_SUITE_LENGTH) {
			throw new TlsDecodeErrorException("Invalid server hello message - contains invalid session id length!");
		}
		
		byte[] sessionIdBytes = new byte[sessionIdLength];
		System.arraycopy(unparsedContent, parsedBytes, sessionIdBytes, 0, sessionIdLength);
		parsedBytes += sessionIdLength;
		
		TlsSessionId sessionId = new TlsSessionId(sessionIdBytes);
		
		//parse cipher suite
		byte[] csBytes = {unparsedContent[parsedBytes], unparsedContent[parsedBytes + 1]}; 
		short csCode = (short)ByteHelper.twoByteArrayToInt(csBytes);
		parsedBytes += CIPHER_SUITE_LENGTH;
		
		TlsCipherSuite cipherSuite = registry.cipherSuiteFromValue(csCode);
		
		//set values
		_serverVersion = version;
		_serverRandom = random;
		_sessionId = sessionId;
		_cipherSuite = cipherSuite;
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

	public String toDetailedString() {
		return "TlsServerHelloMessage [serverVersion=" + _serverVersion + ", serverRandom=" + _serverRandom + ", sessionId=" + _sessionId + ", cipherSuite=" + _cipherSuite + "]";
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		//TODO: server hello html info
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		KeyValueObject kvo = new KeyValueObject("ServerVersion", _serverVersion.toString());
		result.add(kvo);
		
		kvo = new KeyValueObject("ServerRandom", _serverRandom.toString());
		result.add(kvo);
		
		kvo = new KeyValueObject("SessionID", _sessionId.toString());
		result.add(kvo);
		
		kvo = new KeyValueObject("CipherSuite", _cipherSuite.getName() + " [" + Short.toString(_cipherSuite.getCode()) + "]");
		result.add(kvo);
				
		return result;
	}
	
	@Override
	public String getBodyHtmlInfo() {
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHello.html");
	}
}
