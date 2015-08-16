package jProtocol.tls12.model.messages.handshake;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.nio.ByteBuffer;

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
public abstract class TlsHandshakeMessage extends TlsMessage {
	
	public static TlsHandshakeMessage parseHandshakeMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		TlsHandshakeType type;
		try {
			type = TlsHandshakeType.handshakeTypeFromValue(unparsedContent[0]);
		}
		catch (IllegalArgumentException e) {
			throw new TlsDecodeErrorException("First Handshake message byte must provide a valid handshake type!");
		}
		
		//TODO: parse length field
		
		TlsHandshakeMessage result = null;
		
		switch (type) {
		case client_hello:
			result = new TlsClientHelloMessage(unparsedContent);
			break;
		case certificate:
			result = new TlsCertificateMessage(unparsedContent);
			break;
		case certificate_request:
			result = new TlsCertificateRequestMessage(unparsedContent);
			break;
		case certificate_verify:
			result = new TlsCertificateVerifyMessage(unparsedContent);
			break;
		case client_key_exchange:
			//TODO static client key exchange parsing method
			//result = new TlsClientKeyExchangeMessage(unparsedContent);
			break;
		case finished:
			result = new TlsFinishedMessage(unparsedContent);
			break;
		case hello_request:
			result = new TlsHelloRequestMessage(unparsedContent);
			break;
		case server_hello:
			result = new TlsServerHelloMessage(unparsedContent);
			break;
		case server_hello_done:
			result = new TlsServerHelloDoneMessage(unparsedContent);
			break;
		case server_key_exchange:
			result = new TlsServerKeyExchangeMessage(unparsedContent);
			break;
		}
		
		return result;
	}
	
	public TlsHandshakeMessage() {
		super();
	}

	public TlsHandshakeMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
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
	
	public abstract byte[] getBodyBytes();
	
}
