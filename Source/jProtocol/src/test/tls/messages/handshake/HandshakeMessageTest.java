package test.tls.messages.handshake;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.util.List;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import org.junit.Test;

public class HandshakeMessageTest {

	private class HandshakeTestMessage extends TlsHandshakeMessage {
		private TlsHandshakeType _type;
		private byte[] _body;
		
		public HandshakeTestMessage(TlsHandshakeType type, byte[] body) {
			_type = type;
			_body = body;
		}

		@Override
		public TlsHandshakeType getHandshakeType() {
			return _type;
		}

		@Override
		public byte[] getBodyBytes() {
			return _body;
		}

		@Override
		public List<KeyValueObject> getBodyViewData() {
			return null;
		}
	}
	
	private TlsHandshakeType _handshakeType = TlsHandshakeType.client_hello;
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
		
		assertEquals(bytes[0], _handshakeType.getValue()); 
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
		assertEquals(_testMessage.getContentType(), TlsContentType.Handshake);
	}

}