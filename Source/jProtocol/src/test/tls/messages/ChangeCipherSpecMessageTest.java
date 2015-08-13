package test.tls.messages;

import static org.junit.Assert.assertEquals;
import jProtocol.tls12.model.messages.TlsChangeCipherSpecMessage;
import jProtocol.tls12.model.values.TlsContentType;

import org.junit.Test;

public class ChangeCipherSpecMessageTest {

	private TlsChangeCipherSpecMessage _testMessage = new TlsChangeCipherSpecMessage();
	
	@Test
	public void testGetBytesLength() {
		byte[] bytes = _testMessage.getBytes();
		
		assertEquals(bytes.length, 1);
	}
	
	@Test
	public void testGetBytesContentField() {
		byte[] bytes = _testMessage.getBytes();
		
		assertEquals(bytes[0], 1); 
	}

	@Test
	public void testMessageType() {
		assertEquals(_testMessage.getContentType(), TlsContentType.ChangeCipherSpec);
	}

}
