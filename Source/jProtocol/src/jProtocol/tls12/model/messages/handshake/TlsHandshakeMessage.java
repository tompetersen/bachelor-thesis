package jProtocol.tls12.model.messages.handshake;

import java.nio.ByteBuffer;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsContentType.ContentType;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

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
public abstract class TlsHandshakeMessage implements TlsMessage {

	public abstract HandshakeType getHandshakeType();
	
	@Override
	public ContentType getContentType() {
		return ContentType.Handshake;
	}

	@Override
	public byte[] getBytes() {
		byte msgType = TlsHandshakeType.valueFromHandshakeType(getHandshakeType()); //1 
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
