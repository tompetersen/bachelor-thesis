package test.tls.messages;

import static org.junit.Assert.assertEquals;
import jProtocol.tls12.model.messages.TlsAlertMessage;
import jProtocol.tls12.model.values.TlsAlert;
import jProtocol.tls12.model.values.TlsContentType;
import org.junit.Test;

public class AlertMessageTest {

	private TlsAlert _alertType = TlsAlert.bad_record_mac;
	private boolean _isFatal = true;
	private TlsAlertMessage _testMessage = new TlsAlertMessage(_alertType, _isFatal);
	
	@Test
	public void testGetBytesLength() {
		byte[] bytes = _testMessage.getBytes();
		
		assertEquals(bytes.length, 2);
	}
	
	@Test
	public void testGetBytesAlertTypeField() {
		byte[] bytes = _testMessage.getBytes();
		
		assertEquals(bytes[1],_alertType.getValue()); 
	}
	
	@Test
	public void testGetBytesFatalField() {
		byte[] bytes = _testMessage.getBytes();
		byte expected = _isFatal ? (byte) 2 : (byte) 1;
		
		assertEquals(bytes[0], expected); 
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidParameters() {
		new TlsAlertMessage(null, false); 
	}

	@Test
	public void testMessageType() {
		assertEquals(_testMessage.getContentType(), TlsContentType.Alert);
	}
}
