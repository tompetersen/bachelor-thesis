package test.tls.messages.handshake;

import static org.junit.Assert.assertEquals;
import jProtocol.tls12.model.messages.handshake.TlsCertificateMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class CertificateMessageTest {

	private TlsHandshakeMessage _testMessage;
	private byte[] _random8 = new byte[8];
	private TlsCertificate _testCertificate = TlsCertificate.generateRsaCertificate(_random8);
	
	@Before
	public void setUp() {
		List<TlsCertificate> certs= new ArrayList<TlsCertificate>();
		certs.add(_testCertificate);
		_testMessage = new TlsCertificateMessage(certs);
	}

	@Test
	public void testMessageBodyLength() {
		
	}
	
	@Test
	public void testGetBodyBytesXYZField() {
		
	}
	
	@Test
	public void testHandshakeType() {
		assertEquals(_testMessage.getHandshakeType(), TlsHandshakeType.certificate);
	}
}
