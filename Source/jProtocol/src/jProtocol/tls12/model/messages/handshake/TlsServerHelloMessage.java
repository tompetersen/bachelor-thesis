package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.exceptions.TlsInvalidCipherSuiteException;
import jProtocol.tls12.model.values.TlsExtension;
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
	private static final int COMPRESSION_FIELD_LENGTH = 1;
	private static final int EXTENSIONS_LENGTH_FIELD_LENGTH = 2;

	private TlsVersion _serverVersion;		// 2
	private TlsRandom _serverRandom; 		// 4 + 28
	private TlsSessionId _sessionId; 		// empty or 1..32 [Length: 1]
	private TlsCipherSuite _cipherSuite; 	// 2

	private byte _compressionMethod; 		// 1
	private List<TlsExtension> _extensions;	// 0.. [Length: 2]

	/**
	 * Creates a server hello message.
	 * 
	 * @param serverVersion the server version. This is the version which will be used for this connection.
	 * @param serverRandom the server chosen random value
	 * @param sessionId the server chosen session ID
	 * @param cipherSuite the server chosen cipher suite
	 * @param extensions extensions which will be used in this connection
	 */
	public TlsServerHelloMessage(TlsVersion serverVersion, TlsRandom serverRandom, TlsSessionId sessionId, TlsCipherSuite cipherSuite, List<TlsExtension> extensions) {
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
		if (extensions == null) {
			throw new IllegalArgumentException("Extension list must be set!");
		}

		_serverVersion = serverVersion;
		_serverRandom = serverRandom;
		_sessionId = sessionId;
		_cipherSuite = cipherSuite;
		_compressionMethod = 0;
		_extensions = extensions;
	}

	/**
	 * Creates an server hello message by parsing sent bytes.
	 * 
	 * @param unparsedContent the sent bytes
	 * @param registry a cipher suite registry object to parse the contained
	 *            cipher suite code
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsServerHelloMessage(byte[] unparsedContent, TlsCipherSuiteRegistry registry) throws TlsDecodeErrorException {
		super(unparsedContent);

		int parsedBytes = 0;

		// initial length check
		int unparsedLength = unparsedContent.length;
		// version, random, sessionId length field
		if (unparsedLength < VERSION_LENGTH + UNIX_TIME_LENGTH + RANDOM_LENGTH + SESSION_ID_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid server hello message - contains not enough information!");
		}

		// parse version
		TlsVersion version = new TlsVersion(unparsedContent[0], unparsedContent[1]);
		parsedBytes += VERSION_LENGTH;

		// parse server random
		byte[] unixtimeBytes = new byte[UNIX_TIME_LENGTH];
		byte[] randomBytes = new byte[RANDOM_LENGTH];
		System.arraycopy(unparsedContent, parsedBytes, unixtimeBytes, 0, UNIX_TIME_LENGTH);
		parsedBytes += UNIX_TIME_LENGTH;
		System.arraycopy(unparsedContent, parsedBytes, randomBytes, 0, RANDOM_LENGTH);
		parsedBytes += RANDOM_LENGTH;

		TlsRandom random = new TlsRandom(ByteHelper.byteArrayToInt(unixtimeBytes), randomBytes);

		// parse session id
		int sessionIdLength = unparsedContent[parsedBytes]; // just one byte
		parsedBytes += SESSION_ID_LENGTH_FIELD_LENGTH;

		if (parsedBytes + sessionIdLength > unparsedLength + CIPHER_SUITE_LENGTH) {
			throw new TlsDecodeErrorException("Invalid server hello message - contains invalid session id length!");
		}

		byte[] sessionIdBytes = new byte[sessionIdLength];
		System.arraycopy(unparsedContent, parsedBytes, sessionIdBytes, 0, sessionIdLength);
		parsedBytes += sessionIdLength;

		TlsSessionId sessionId = new TlsSessionId(sessionIdBytes);

		// parse cipher suite
		byte[] csBytes = { unparsedContent[parsedBytes], unparsedContent[parsedBytes + 1] };
		short csCode = (short) ByteHelper.twoByteArrayToInt(csBytes);
		parsedBytes += CIPHER_SUITE_LENGTH;

		TlsCipherSuite cipherSuite;
		try {
			cipherSuite = registry.cipherSuiteFromCode(csCode);
		}
		catch (TlsInvalidCipherSuiteException e) {
			throw new TlsDecodeErrorException("CipherSuite for code [" + csCode + "] not found!");
		}

		// parse compression method
		if (parsedBytes + COMPRESSION_FIELD_LENGTH > unparsedLength + EXTENSIONS_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid server hello message - contains less bytes than necessary!");
		}
		byte compressionMethod = unparsedContent[parsedBytes];
		parsedBytes += COMPRESSION_FIELD_LENGTH;

		// parse extensions
		byte[] extensionsLengthBytes = { unparsedContent[parsedBytes], unparsedContent[parsedBytes + 1] };
		int extensionsLength = ByteHelper.twoByteArrayToInt(extensionsLengthBytes);
		parsedBytes += EXTENSIONS_LENGTH_FIELD_LENGTH;

		if (parsedBytes + extensionsLength != unparsedLength) {
			throw new TlsDecodeErrorException("Invalid server hello message - contains invalid extensions length!");
		}

		List<TlsExtension> extensions = new ArrayList<>();
		for (int i = parsedBytes; i < parsedBytes + extensionsLength;) {
			byte[] extensionTypeBytes = { unparsedContent[i], unparsedContent[i + 1] };
			short extensionType = (short) ByteHelper.twoByteArrayToInt(extensionTypeBytes);
			i += TlsExtension.EXTENSION_TYPE_FIELD_LENGTH;

			byte[] extensionLengthBytes = { unparsedContent[i], unparsedContent[i + 1] };
			int extensionLength = ByteHelper.twoByteArrayToInt(extensionLengthBytes);
			i += TlsExtension.EXTENSION_LENGTH_FIELD_LENGTH;

			byte[] extensionData = new byte[extensionLength];
			System.arraycopy(unparsedContent, i, extensionData, 0, extensionLength);
			i += extensionLength;

			TlsExtension extension = new TlsExtension(extensionType, extensionData);
			extensions.add(extension);
		}
		parsedBytes += extensionsLength;

		// set values
		_serverVersion = version;
		_serverRandom = random;
		_sessionId = sessionId;
		_cipherSuite = cipherSuite;
		_compressionMethod = compressionMethod;
		_extensions = extensions;
	}

	/**
	 * Returns the server version contained in this message. This is the version
	 * the protocol will use for this connection.
	 * 
	 * @return the server version
	 */
	public TlsVersion getServerVersion() {
		return _serverVersion;
	}

	/**
	 * Returns the server random value.
	 * 
	 * @return the server random
	 */
	public TlsRandom getServerRandom() {
		return _serverRandom;
	}

	/**
	 * Returns the session ID set by the server for this connection.
	 * 
	 * @return the session ID
	 */
	public TlsSessionId getSessionId() {
		return _sessionId;
	}

	/**
	 * Returns the server chosen cipher suite. It must have been contained in
	 * the client hello message and will be used for this connection.
	 * 
	 * @return the cipher suite
	 */
	public TlsCipherSuite getCipherSuite() {
		return _cipherSuite;
	}

	/**
	 * Returns the extensions contained in this message. They must have been
	 * present in the client hello message.
	 * 
	 * @return a list of extensions for the connection
	 */
	public List<TlsExtension> getExtensions() {
		return _extensions;
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.server_hello;
	}

	@Override
	public byte[] getBodyBytes() {
		int bufferLength = VERSION_LENGTH;
		bufferLength += UNIX_TIME_LENGTH;
		bufferLength += RANDOM_LENGTH;
		bufferLength += SESSION_ID_LENGTH_FIELD_LENGTH;
		bufferLength += _sessionId.getLength();
		bufferLength += CIPHER_SUITE_LENGTH;
		bufferLength += COMPRESSION_FIELD_LENGTH;

		bufferLength += EXTENSIONS_LENGTH_FIELD_LENGTH;
		short extensionListLength = 0;
		for (TlsExtension extension : _extensions) {
			extensionListLength += TlsExtension.EXTENSION_LENGTH_FIELD_LENGTH;
			extensionListLength += TlsExtension.EXTENSION_TYPE_FIELD_LENGTH;
			extensionListLength += extension.getExtensionData().length;
		}
		bufferLength += extensionListLength;

		ByteBuffer b = ByteBuffer.allocate(bufferLength);

		// version
		b.put(_serverVersion.getMajorVersion());
		b.put(_serverVersion.getMinorVersion());

		// random
		b.put(_serverRandom.getBytes());

		// sessionId
		b.put((byte) _sessionId.getLength());
		if (!_sessionId.isEmpty()) {
			b.put(_sessionId.getSessionId());
		}

		// cipherSuite
		b.putShort(_cipherSuite.getCode());

		// compression method
		b.put(_compressionMethod);

		// extension list
		b.putShort(extensionListLength);// 0 as length field
		for (TlsExtension extension : _extensions) {
			b.putShort(extension.getExtensionType());
			byte[] extensionData = extension.getExtensionData();
			b.putShort((short) extensionData.length);
			b.put(extensionData);
		}

		return b.array();
	}

	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();

		KeyValueObject kvo = new KeyValueObject("Server version", _serverVersion.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHello_Version.html"));
		result.add(kvo);

		kvo = new KeyValueObject("Server random", _serverRandom.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_Random.html"));
		result.add(kvo);

		kvo = new KeyValueObject("Session ID", _sessionId.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHello_SessionId.html"));
		result.add(kvo);

		kvo = new KeyValueObject("CipherSuite", _cipherSuite.getName() + " [0x" + ByteHelper.bytesToHexString(ByteHelper.intToTwoByteArray(_cipherSuite.getCode())) + "]");
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHello_CipherSuite.html"));
		result.add(kvo);

		kvo = new KeyValueObject("Compression method", Byte.toString(_compressionMethod));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHello_Compression.html"));
		result.add(kvo);

		List<KeyValueObject> extensions = new ArrayList<>();
		for (TlsExtension extension : _extensions) {
			extensions.add(new KeyValueObject(Short.toString(extension.getExtensionType()), "extension data"));
		}
		kvo = new KeyValueObject("Extension list", extensions);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHello_ExtensionList.html"));
		result.add(kvo);

		return result;
	}

	@Override
	public String getBodyHtmlInfo() {
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerHello.html");
	}
}
