package test.tls.messages;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.values.TlsContentType.ContentType;

import org.junit.Before;
import org.junit.Test;

public class ApplicationDataMessageTest {

	private byte[] _content = {0,(byte) 0xFF,23,42};
	private TlsApplicationDataMessage _testMessage;
	
	@Before
	public void setUp() {
		_testMessage = new TlsApplicationDataMessage(_content);
	}
	
	@Test
	public void testGetBytesLength() {
		byte[] bytes = _testMessage.getBytes();
		
		assertEquals(bytes.length, 4);
	}
	
	@Test
	public void testGetBytesContentField() {
		byte[] bytes = _testMessage.getBytes();
		
		assertArrayEquals(bytes, _content); 
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidParameters() {
		new TlsApplicationDataMessage(null); 
	}

	@Test
	public void testMessageType() {
		assertEquals(_testMessage.getContentType(), ContentType.ApplicationData);
	}

}
