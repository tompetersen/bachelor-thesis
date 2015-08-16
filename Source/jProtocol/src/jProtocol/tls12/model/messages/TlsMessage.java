package jProtocol.tls12.model.messages;

import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsContentType;

public abstract class TlsMessage {

	/**
	 * Parses a message dependent on the content type.
	 * 
	 * @param unparsedContent the raw content (not including ContentType, Protocol Version, length field)
	 * @param contentType the already parsed content type of the message
	 * 
	 * @return the parsed messages
	 * 
	 * @throws TlsDecodeErrorException when a message could not be successfully decoded
	 */
	public static TlsMessage parseTlsMessage(byte[] unparsedContent, TlsContentType contentType, TlsCipherSuiteRegistry registry) throws TlsDecodeErrorException {
		if (contentType == null) {
			throw new IllegalArgumentException("Content type must not be null!");
		}

		TlsMessage result = null;
		switch (contentType) {
		case Handshake:
			TlsHandshakeMessage.parseHandshakeMessage(unparsedContent, registry);
			break;
		case ChangeCipherSpec:
			result = new TlsChangeCipherSpecMessage(unparsedContent);
			break;
		case Alert:
			result = new TlsAlertMessage(unparsedContent);
			break;
		case ApplicationData:
			result = new TlsApplicationDataMessage(unparsedContent);
			break;
		}
		
		return result;
	}

	/**
	 * Constructor to be called, when parsing the message content is necessary
	 * (after decrypting a sent TlsCiphertext).
	 * 
	 * @param unparsedContent the raw content (not including ContentType, Protocol Version, length field)
	 * 
	 * @throws TlsDecodeErrorException when a message could not be successfully decoded
	 */
	public TlsMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		if (unparsedContent == null) {
			throw new IllegalArgumentException("Unparsed content must not be null!");
		}
	}
	
	/**
	 * Default constructor.
	 */
	public TlsMessage() {
	}

	public abstract TlsContentType getContentType();

	public abstract byte[] getBytes();

}
