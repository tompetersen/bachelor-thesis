package jProtocol.tls12.model.messages.handshake;

import jProtocol.helper.ByteHelper;
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
public abstract class TlsHandshakeMessage implements TlsMessage {

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
