package test.tls.messages.handshake;

import static org.junit.Assert.*;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsContentType.ContentType;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;

import org.junit.Test;

public class HandshakeMessageTest {

	private class HandshakeTestMessage extends TlsHandshakeMessage {
		private HandshakeType _type;
		private byte[] _body;
		
		public HandshakeTestMessage(HandshakeType type, byte[] body) {
			_type = type;
			_body = body;
		}

		@Override
		public HandshakeType getHandshakeType() {
			return _type;
		}

		@Override
		public byte[] getBodyBytes() {
			return _body;
		}
	}
	
	private HandshakeType _handshakeType = HandshakeType.client_hello;
	private byte[] _body = {3,1,4,1,5};
	private TlsHandshakeMessage _testMessage = new HandshakeTestMessage(_handshakeType, _body);
	
	@Test
	public void testGetBytesLength() {
		byte[] bytes = _testMessage.getBytes();
		
		assertEquals(bytes.length, 1 + 3 + 5); //type, length, body bytes
	}
	
	@Test
	public void testGetBytesHandshakeTypeField() {
		byte[] bytes = _testMessage.getBytes();
		
		assertEquals(bytes[0], TlsHandshakeType.valueFromHandshakeType(_handshakeType)); 
	}
	
	@Test
	public void testGetBytesLengthField() {
		byte[] bytes = _testMessage.getBytes();
		byte[] lengthBytes = {bytes[1], bytes[2], bytes[3]};
		int length = ByteHelper.threeByteArrayToInt(lengthBytes);
		
		assertEquals(length, _body.length); 
	}
	
	@Test
	public void testGetBytesContentField() {
		byte[] bytes = _testMessage.getBytes();
		byte[] content = new byte[5];
		System.arraycopy(bytes, 4, content, 0, bytes.length - 4);;
		
		assertArrayEquals(content, _body); 
	}
	
	@Test
	public void testMessageType() {
		assertEquals(_testMessage.getContentType(), ContentType.Handshake);
	}

}