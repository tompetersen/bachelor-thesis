package jProtocol.tls12.model;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsVersion;

public class TlsPlaintext {

	private static final int CONTENT_TYPE_LENGTH = 1;
	private static final int VERSION_LENGTH = 2;
	private static final int LENGTH_FIELD_LENGTH = 2;
	public static final int RECORD_HEADER_LENGTH = CONTENT_TYPE_LENGTH + VERSION_LENGTH + LENGTH_FIELD_LENGTH;
	
	private TlsMessage _message;
	
	private TlsContentType _contentType; 		//1 byte
	private TlsVersion _version; 				//2 bytes
	private short _length; 						//2 bytes
	private byte[] _fragment;
	
	/**
	 * Creates a plaintext from an existing message and the currently used TlsVersion.
	 * 
	 * @param message the message
	 * @param version the version
	 */
	public TlsPlaintext(TlsMessage message, TlsVersion version) {
		_message = message;
		_version = version;
		_contentType = message.getContentType();
		_fragment = _message.getBytes();
		_length = (short) _fragment.length;
	}
	
	/**
	 * Creates a plaintext by parsing a decrypted message.
	 * 
	 * @param decryptedBytes the decrypted message bytes (including content type, version and length)
	 * @param registry the cipher suite registry
	 * @param algorithm the used key exchange algorithm
	 * 
	 * @throws TlsDecodeErrorException if the message could not be parsed
	 */
	public TlsPlaintext(byte[] decryptedBytes, TlsCipherSuiteRegistry registry, TlsKeyExchangeAlgorithm algorithm) throws TlsDecodeErrorException {
		if (decryptedBytes.length < RECORD_HEADER_LENGTH) {
			throw new TlsDecodeErrorException("Invalid TLS message - contains not enough information for content type, version and length!");
		}
		
		//parse content type
		TlsContentType contentType;
		try {
			contentType = TlsContentType.contentTypeFromValue(decryptedBytes[0]);
		}
		catch (IllegalArgumentException e) {
			throw new TlsDecodeErrorException("Invalid content type found in message!");
		}
		
		//parse version
		TlsVersion version = new TlsVersion(decryptedBytes[1], decryptedBytes[2]);
		
		//parse length
		byte[] fragmentLengthBytes = {decryptedBytes[3], decryptedBytes[4]};
		int fragmentLength = ByteHelper.twoByteArrayToInt(fragmentLengthBytes);
		
		if (fragmentLength != decryptedBytes.length - RECORD_HEADER_LENGTH) {
			throw new TlsDecodeErrorException("Message contains invalid fragment length!");
		}

		_contentType = contentType;
		_version = version;
		_length = (short) fragmentLength;
		_fragment = new byte[fragmentLength];
		System.arraycopy(decryptedBytes, 5, _fragment, 0, fragmentLength);
		
		TlsMessage message = TlsMessage.parseTlsMessage(_fragment, _contentType, registry, algorithm);
		_message = message;
	}
	
	/**
	 * Returns the content type of the included message.
	 * 
	 * @return the content type
	 */
	public TlsContentType getContentType() {
		return _contentType;
	}

	/**
	 * Returns the used TLS version.
	 * 
	 * @return the version
	 */
	public TlsVersion getVersion() {
		return _version;
	}

	/**
	 * Returns the length of the enclosed fragment used in the TlsPlaintext.length field.
	 * 
	 * @return the fragment length
	 */
	public short getLength() {
		return _length;
	}

	/**
	 * Returns the bytes of the enclosed fragment.
	 * 
	 * @return the fragment bytes
	 */
	public byte[] getFragment() {
		return _fragment;
	}

	/**
	 * Returns the included TLS higher level message.
	 * 
	 * @return the message
	 */
	public TlsMessage getMessage() {
		return _message;
	}
}
