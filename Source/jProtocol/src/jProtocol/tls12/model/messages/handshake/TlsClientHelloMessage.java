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
	private static final int COMPRESSION_LENGTH_FIELD_LENGTH = 1;
	private static final int EXTENSIONS_LENGTH_FIELD_LENGTH = 2;

	private TlsVersion _clientVersion;			// 2
	private TlsRandom _clientRandom; 			// 4 + 28
	private TlsSessionId _sessionId; 			// empty or 1..32 [Length: 1]
	private List<TlsCipherSuite> _cipherSuites; // 1.. [Length: 2]

	private byte _compressionMethod; 			// 1.. [Length: 1]
	private List<TlsExtension> _extensions;		// 0.. [Length: 2]

	/**
	 * Creates a client hello message.
	 * 
	 * @param clientVersion the highest TLS version the client supports
	 * @param clientRandom the client chosen random value
	 * @param sessionId the session ID. Should be empty for new connections and
	 *            can contain a session ID of an old connection to resume.
	 * @param cipherSuites a list of supported cipher suites sorted by preference
	 * @param extensionList a list of extensions, which should be used for this connection
	 */
	public TlsClientHelloMessage(TlsVersion clientVersion, TlsRandom clientRandom, TlsSessionId sessionId, List<TlsCipherSuite> cipherSuites, List<TlsExtension> extensionList) {
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
		if (extensionList == null) {
			throw new IllegalArgumentException("Extension list must be set!");
		}

		_clientVersion = clientVersion;
		_clientRandom = clientRandom;
		_sessionId = sessionId;
		_cipherSuites = cipherSuites;

		// TODO: Used for compression methods other than null compression ->
		// Implement if necessary
		_compressionMethod = 0; // Compression method null

		_extensions = extensionList;
	}

	/**
	 * Creates a client hello message by parsing sent bytes.
	 * 
	 * @param unparsedContent the sent bytes
	 * @param registry a cipher suite registry
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsClientHelloMessage(byte[] unparsedContent, TlsCipherSuiteRegistry registry) throws TlsDecodeErrorException {
		super(unparsedContent);

		int parsedBytes = 0;

		// initial length check
		int unparsedLength = unparsedContent.length;
		if (unparsedLength < VERSION_LENGTH + UNIX_TIME_LENGTH + RANDOM_LENGTH + SESSION_ID_LENGTH_FIELD_LENGTH + COMPRESSION_LENGTH_FIELD_LENGTH + EXTENSIONS_LENGTH_FIELD_LENGTH) { // version, random, sessionId, length field
			throw new TlsDecodeErrorException("Invalid client hello message - contains not enough information!");
		}

		// parse version
		TlsVersion version = new TlsVersion(unparsedContent[0], unparsedContent[1]);
		parsedBytes += VERSION_LENGTH;

		// parse client random
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

		if (parsedBytes + sessionIdLength > unparsedLength + CIPHER_SUITES_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid client hello message - contains invalid session id length!");
		}

		byte[] sessionIdBytes = new byte[sessionIdLength];
		System.arraycopy(unparsedContent, parsedBytes, sessionIdBytes, 0, sessionIdLength);
		parsedBytes += sessionIdLength;

		TlsSessionId sessionId = new TlsSessionId(sessionIdBytes);

		// parse cipher suites
		byte[] cipherSuitesLengthBytes = { unparsedContent[parsedBytes], unparsedContent[parsedBytes + 1] };
		int cipherSuitesLength = ByteHelper.twoByteArrayToInt(cipherSuitesLengthBytes);
		parsedBytes += CIPHER_SUITES_LENGTH_FIELD_LENGTH;

		if (parsedBytes + cipherSuitesLength > unparsedLength + COMPRESSION_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid client hello message - contains invalid cipher suite length!");
		}

		List<TlsCipherSuite> cipherSuites = new ArrayList<>();
		for (int i = parsedBytes; i < parsedBytes + cipherSuitesLength; i += 2) {
			byte[] csBytes = { unparsedContent[i], unparsedContent[i + 1] };
			short csCode = (short) ByteHelper.twoByteArrayToInt(csBytes);
			try {
				cipherSuites.add(registry.cipherSuiteFromCode(csCode));
			}
			catch (TlsInvalidCipherSuiteException e) {
				throw new TlsDecodeErrorException("CipherSuite for code [" + csCode + "] not found!");
			}
		}
		parsedBytes += cipherSuitesLength;

		// parse compression method
		byte compressionLength = unparsedContent[parsedBytes];
		if (parsedBytes + compressionLength > unparsedLength + EXTENSIONS_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid client hello message - contains invalid cipher suite length!");
		}
		// TODO: Used for compression methods other than null compression ->
		// Implement if necessary
		byte compressionMethod = unparsedContent[parsedBytes + 1];
		parsedBytes += COMPRESSION_LENGTH_FIELD_LENGTH + compressionLength;

		// parse extension list
		byte[] extensionsLengthBytes = { unparsedContent[parsedBytes], unparsedContent[parsedBytes + 1] };
		int extensionsLength = ByteHelper.twoByteArrayToInt(extensionsLengthBytes);
		parsedBytes += EXTENSIONS_LENGTH_FIELD_LENGTH;

		if (parsedBytes + extensionsLength != unparsedLength) {
			throw new TlsDecodeErrorException("Invalid client hello message - contains invalid cipher suite length!");
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
		_clientVersion = version;
		_clientRandom = random;
		_sessionId = sessionId;
		_cipherSuites = cipherSuites;
		_compressionMethod = compressionMethod;
		_extensions = extensions;
	}

	/**
	 * Returns a list of extensions the client want to use for this connection.
	 * 
	 * @return the extension list
	 */
	public List<TlsExtension> getExtensions() {
		return _extensions;
	}

	/**
	 * Returns the highest supported TLS version.
	 * 
	 * @return the version
	 */
	public TlsVersion getClientVersion() {
		return _clientVersion;
	}

	/**
	 * Returns the client random value.
	 * 
	 * @return the client random
	 */
	public TlsRandom getClientRandom() {
		return _clientRandom;
	}

	/**
	 * Returns the session ID. Can be empty for new sessions.
	 * 
	 * @return the session ID
	 */
	public TlsSessionId getSessionId() {
		return _sessionId;
	}

	/**
	 * Returns the list of client supported cipher suites.
	 * 
	 * @return the cipher suite list
	 */
	public List<TlsCipherSuite> getCipherSuites() {
		return _cipherSuites;
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.client_hello;
	}

	@Override
	public byte[] getBodyBytes() {
		int length = VERSION_LENGTH;
		length += UNIX_TIME_LENGTH;
		length += RANDOM_LENGTH;
		length += SESSION_ID_LENGTH_FIELD_LENGTH + _sessionId.getLength();
		length += CIPHER_SUITES_LENGTH_FIELD_LENGTH + 2 * _cipherSuites.size();
		length += COMPRESSION_LENGTH_FIELD_LENGTH + 1;

		length += EXTENSIONS_LENGTH_FIELD_LENGTH;
		short extensionListLength = 0;
		for (TlsExtension extension : _extensions) {
			extensionListLength += TlsExtension.EXTENSION_LENGTH_FIELD_LENGTH;
			extensionListLength += TlsExtension.EXTENSION_TYPE_FIELD_LENGTH;
			extensionListLength += extension.getExtensionData().length;
		}
		length += extensionListLength;

		ByteBuffer b = ByteBuffer.allocate(length);

		// version
		b.put(_clientVersion.getMajorVersion());
		b.put(_clientVersion.getMinorVersion());

		// random
		b.put(_clientRandom.getBytes());

		// sessionId
		b.put((byte) _sessionId.getLength());
		if (!_sessionId.isEmpty()) {
			b.put(_sessionId.getSessionId());
		}

		// cipherSuites
		int cslength = 2 * _cipherSuites.size(); // * 2 because number of bytes
													// is used
		b.put(ByteHelper.intToTwoByteArray(cslength));
		for (TlsCipherSuite cs : _cipherSuites) {
			b.putShort(cs.getCode());
		}

		// Compression
		// TODO: Used for compression methods other than null compression ->
		// Implement if necessary
		b.put((byte) 1);// compression length
		b.put(_compressionMethod);

		// Extensions
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
		ArrayList<KeyValueObject> resultList = new ArrayList<>();

		KeyValueObject kvo = new KeyValueObject("Client version", _clientVersion.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientHello_Version.html"));
		resultList.add(kvo);

		kvo = new KeyValueObject("Client random", _clientRandom.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("states/TLS12_Random.html"));
		resultList.add(kvo);

		kvo = new KeyValueObject("Session ID", _sessionId.toString());
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientHello_SessionId.html"));
		resultList.add(kvo);

		List<KeyValueObject> cipherSuites = new ArrayList<>();
		for (TlsCipherSuite cs : _cipherSuites) {
			cipherSuites.add(new KeyValueObject("", cs.getName() + " [0x" + ByteHelper.bytesToHexString(ByteHelper.intToTwoByteArray(cs.getCode())) + "]"));
		}
		kvo = new KeyValueObject("Cipher suite list", cipherSuites);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientHello_CipherSuite.html"));
		resultList.add(kvo);

		// compression
		// TODO: Used for compression methods other than null compression ->
		// Implement if necessary
		List<KeyValueObject> compressionMethods = new ArrayList<>();
		compressionMethods.add(new KeyValueObject("Compression method", Byte.toString(_compressionMethod)));
		kvo = new KeyValueObject("Compression method list", compressionMethods);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientHello_Compression.html"));
		resultList.add(kvo);

		// extension list
		List<KeyValueObject> extensions = new ArrayList<>();
		for (TlsExtension extension : _extensions) {
			extensions.add(new KeyValueObject(Short.toString(extension.getExtensionType()), "extension data"));
		}
		kvo = new KeyValueObject("Extension list", extensions);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientHello_ExtensionList.html"));
		resultList.add(kvo);

		return resultList;
	}

	@Override
	public String getBodyHtmlInfo() {
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientHello.html");
	}
}
