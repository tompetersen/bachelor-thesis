package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
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

public class TlsClientHelloMessage extends TlsHandshakeMessage {

	/*
	 struct {
          ProtocolVersion client_version;
          Random random;
          SessionID session_id;
          CipherSuite cipher_suites<2..2^16-2>;
          CompressionMethod compression_methods<1..2^8-1>;
          select (extensions_present) {
              case false:
                  struct {};
              case true:
                  Extension extensions<0..2^16-1>;
          };
      } ClientHello;
	 */
	
	private static final int VERSION_LENGTH = 2;
	private static final int UNIX_TIME_LENGTH = 4;
	private static final int RANDOM_LENGTH = 28;
	private static final int SESSION_ID_LENGTH_FIELD_LENGTH = 1;
	private static final int CIPHER_SUITES_LENGTH_FIELD_LENGTH = 2;
	
	private TlsVersion _clientVersion;			//2
	private TlsRandom _clientRandom; 			//4 + 28
	private TlsSessionId _sessionId; 			//empty or 1..32 [Length: 1]
	private List<TlsCipherSuite> _cipherSuites; //1.. [Length: 2]

	// private byte[] _compressionMethod; 		//1.. [Length: 1]
	// private Extension[] _extensions;			//0.. [Length: 2]
	
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
	
	public TlsClientHelloMessage(byte[] unparsedContent, TlsCipherSuiteRegistry registry) throws TlsDecodeErrorException {
		super(unparsedContent);
		
		int parsedBytes = 0;
		
		//initial length check
		int unparsedLength = unparsedContent.length;
		if (unparsedLength < VERSION_LENGTH + UNIX_TIME_LENGTH + RANDOM_LENGTH + SESSION_ID_LENGTH_FIELD_LENGTH) { //version, random, sessionId length field
			throw new TlsDecodeErrorException("Invalid client hello message - contains not enough information!");
		}
		
		//parse version
		TlsVersion version = new TlsVersion(unparsedContent[0], unparsedContent[1]);
		parsedBytes += VERSION_LENGTH;
		
		//parse client random
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
		
		if (parsedBytes + sessionIdLength > unparsedLength + CIPHER_SUITES_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid client hello message - contains invalid session id length!");
		}
		
		byte[] sessionIdBytes = new byte[sessionIdLength];
		System.arraycopy(unparsedContent, parsedBytes, sessionIdBytes, 0, sessionIdLength);
		parsedBytes += sessionIdLength;
		
		TlsSessionId sessionId = new TlsSessionId(sessionIdBytes);
		
		//parse cipher suites
		byte[] cipherSuitesLengthBytes = {unparsedContent[parsedBytes], unparsedContent[parsedBytes + 1]};
		int cipherSuitesLength = ByteHelper.twoByteArrayToInt(cipherSuitesLengthBytes);
		parsedBytes += CIPHER_SUITES_LENGTH_FIELD_LENGTH;
		
		if (parsedBytes + cipherSuitesLength != unparsedLength) {
			throw new TlsDecodeErrorException("Invalid client hello message - contains invalid cipher suite length!");
		}
		
		List<TlsCipherSuite> cipherSuites = new ArrayList<>();
		for (int i = parsedBytes; i < parsedBytes + cipherSuitesLength; i+= 2) {
			byte[] csBytes = {unparsedContent[i], unparsedContent[i+1]}; 
			short csCode = (short)ByteHelper.twoByteArrayToInt(csBytes);
			cipherSuites.add(registry.cipherSuiteFromValue(csCode));
		}
		parsedBytes += cipherSuitesLength;
		
		//set values
		_clientVersion = version;
		_clientRandom = random;
		_sessionId = sessionId;
		_cipherSuites = cipherSuites;
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.client_hello;
	}

	@Override
	public byte[] getBodyBytes() {
		ByteBuffer b = ByteBuffer.allocate(VERSION_LENGTH + UNIX_TIME_LENGTH + RANDOM_LENGTH + SESSION_ID_LENGTH_FIELD_LENGTH + _sessionId.getLength() + CIPHER_SUITES_LENGTH_FIELD_LENGTH + 2 * _cipherSuites.size());
		
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
		b.put(ByteHelper.intToTwoByteArray(cslength));
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

	public String toDetailedString() {
		return "TlsClientHelloMessage [clientVersion=" + _clientVersion + ", clientRandom=" + _clientRandom + ", sessionId=" + _sessionId + ", cipherSuites=" + _cipherSuites.size() + " entries]";
	}

	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		KeyValueObject kvo = new KeyValueObject("ClientVersion", _clientVersion.toString());
		result.add(kvo);
		
		kvo = new KeyValueObject("ClientRandom", _clientRandom.toString());
		result.add(kvo);
		
		kvo = new KeyValueObject("SessionID", _sessionId.toString());
		result.add(kvo);
		
		List<KeyValueObject> cipherSuites = new ArrayList<>();
		for (TlsCipherSuite cs : _cipherSuites) {
			cipherSuites.add(new KeyValueObject("", cs.getName() + " [" + Short.toString(cs.getCode()) + "]"));
		}
		kvo = new KeyValueObject("CipherSuites", cipherSuites);
		result.add(kvo);
		
		return result;
	}
}
