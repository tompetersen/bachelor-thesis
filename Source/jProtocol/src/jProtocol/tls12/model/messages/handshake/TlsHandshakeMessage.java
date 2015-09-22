package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class TlsHandshakeMessage extends TlsMessage {

	/*
	struct {
	     HandshakeType msg_type;    // handshake type 
	     uint24 length;             // bytes in message 
	     select (HandshakeType) {
	         case hello_request:       HelloRequest;
	         case client_hello:        ClientHello;
	         case server_hello:        ServerHello;
	         case certificate:         Certificate;
	         case server_key_exchange: ServerKeyExchange;
	         case certificate_request: CertificateRequest;
	         case server_hello_done:   ServerHelloDone;
	         case certificate_verify:  CertificateVerify;
	         case client_key_exchange: ClientKeyExchange;
	         case finished:            Finished;
	     } body;
	 } Handshake;
	 */
	
	/**
	 * Parses a handshake message dependent on the handshake type.
	 * 
	 * @param unparsedContent the content (not including ContentType, Protocol Version, length field)
	 * @param registry the already parsed content type of the message
	 * @param algorithm the used key exchange algorithm 
	 * 
	 * @return the parsed messages
	 * 
	 * @throws TlsDecodeErrorException if a message could not be successfully decoded
	 */
	public static TlsHandshakeMessage parseHandshakeMessage(byte[] unparsedContent, TlsCipherSuiteRegistry registry, TlsKeyExchangeAlgorithm algorithm) throws TlsDecodeErrorException {
		TlsHandshakeType type;
		try {
			type = TlsHandshakeType.handshakeTypeFromValue(unparsedContent[0]);
		}
		catch (IllegalArgumentException e) {
			throw new TlsDecodeErrorException("First Handshake message byte must provide a valid handshake type!");
		}
		
		byte[]lengthBytes = new byte[3];
		System.arraycopy(unparsedContent, 1, lengthBytes, 0, 3);
		int length = ByteHelper.threeByteArrayToInt(lengthBytes);
		
		if (unparsedContent.length - 4 != length) {
			throw new TlsDecodeErrorException("Invalid length field in Handshake message!");
		}
		
		byte[] unparsedMessageBody = new byte[length];
		System.arraycopy(unparsedContent, 4, unparsedMessageBody, 0, length);
		
		TlsHandshakeMessage result = null;
		switch (type) {
		case client_hello:
			result = new TlsClientHelloMessage(unparsedMessageBody, registry);
			break;
		case certificate:
			result = new TlsCertificateMessage(unparsedMessageBody);
			break;
		case certificate_request:
			result = new TlsCertificateRequestMessage(unparsedMessageBody);
			break;
		case certificate_verify:
			result = new TlsCertificateVerifyMessage(unparsedMessageBody);
			break;
		case client_key_exchange:
			result = TlsClientKeyExchangeMessage.parseClientKeyExchangeMessage(unparsedMessageBody, algorithm);
			break;
		case finished:
			result = new TlsFinishedMessage(unparsedMessageBody);
			break;
		case hello_request:
			result = new TlsHelloRequestMessage(unparsedMessageBody);
			break;
		case server_hello:
			result = new TlsServerHelloMessage(unparsedMessageBody, registry);
			break;
		case server_hello_done:
			result = new TlsServerHelloDoneMessage(unparsedMessageBody);
			break;
		case server_key_exchange:
			result = new TlsServerKeyExchangeMessage(unparsedMessageBody);
			break;
		}
		
		return result;
	}
	
	public TlsHandshakeMessage() {
		super();
	}

	/**
	 * Constructor for TLS handshake messages.
	 * 
	 * @param unparsedMessageBody the unparsed body of a handshake message
	 * @throws TlsDecodeErrorException if errors (like invalid length fields, invalid values, ...) occur during decoding
	 */
	public TlsHandshakeMessage(byte[] unparsedMessageBody) throws TlsDecodeErrorException {
		super(unparsedMessageBody);
	}

	public abstract TlsHandshakeType getHandshakeType();
	
	@Override
	public TlsContentType getContentType() {
		return TlsContentType.Handshake;
	}

	@Override
	public byte[] getBytes() {
		byte msgType = getHandshakeType().getValue(); //1 
		byte[] body = getBodyBytes();
		int length = body.length; //3
		
		ByteBuffer b = ByteBuffer.allocate(length + 4);
		b.put(msgType);
		b.put(ByteHelper.intToThreeByteArray(length));
		b.put(body);
		
		return b.array();
	}
	
	/**
	 * Returns the handshake message body not including handshake header (handshake type and length field).
	 * 
	 * @return
	 */
	public abstract byte[] getBodyBytes();
	
	public int getLength() {
		return getBodyBytes().length;
	}

	@Override
	public String toString() {
		return getHandshakeType().toString();
	}

	@Override
	public KeyValueObject getViewData() {
		ArrayList<KeyValueObject> children = new ArrayList<KeyValueObject>();
		
		KeyValueObject kvo = new KeyValueObject("HandshakeType", getHandshakeType().toString());
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_HandshakeType.html"));
		children.add(kvo);
		
		kvo = new KeyValueObject("Length", Integer.toString(getLength()));
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_HandshakeLength.html"));
		children.add(kvo);

		KeyValueObject bodyKvo = new KeyValueObject("Body", getBodyViewData());
		bodyKvo.setValue(getHandshakeType().toString());
		bodyKvo.setHtmlHelpContent(getBodyHtmlInfo());
		children.add(bodyKvo);
		
		KeyValueObject result = new KeyValueObject("Content", children);
		result.setValue("TlsHandshake");
		result.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_Handshake.html"));
		
		return result;
	}
	
	public abstract List<KeyValueObject> getBodyViewData();
	
	public abstract String getBodyHtmlInfo();
}
