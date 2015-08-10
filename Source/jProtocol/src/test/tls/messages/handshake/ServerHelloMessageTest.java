package test.tls.messages.handshake;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerHelloMessage;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ServerHelloMessageTest {

	private TlsHandshakeMessage _testMessage;
	private TlsVersion _version = TlsVersion.getTls12Version();
	private int _unixtime = 0x2342;
	private byte[] _random28;
	private TlsRandom _serverRandom;
	private TlsSessionId _sessionId;
	private TlsCipherSuite _cipherSuite = new TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA();
	
	@Before
	public void setUp() {
		_random28 = new byte[28];
		Arrays.fill(_random28, (byte)0xF0);
		_serverRandom = new TlsRandom(_unixtime, _random28);
		
		_sessionId = new TlsSessionId(_random28);
		
		
		_testMessage = new TlsServerHelloMessage(_version, _serverRandom, _sessionId, _cipherSuite);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullVersionParameter() {
		new TlsServerHelloMessage(null, _serverRandom, _sessionId, _cipherSuite);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullRandomParameter() {
		new TlsServerHelloMessage(_version, null, _sessionId, _cipherSuite);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullSessionIdParameter() {
		new TlsServerHelloMessage(_version, _serverRandom, null, _cipherSuite);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullCipherSuiteParameter() {
		new TlsServerHelloMessage(_version, _serverRandom, _sessionId, null);
	}
	
	@Test
	public void testMessageBodyLength() {
		int expected = 2; //version
		expected += 32; //random
		expected += 1 + _sessionId.getLength(); //sessionId and length byte
		expected += 2; //ciphersuite
		
		assertEquals(expected, _testMessage.getBodyBytes().length);
	}
	
	@Test
	public void testGetBodyBytesVersionField() {
		byte[] body = _testMessage.getBodyBytes();
		
		assertEquals(_version.getMajorVersion(), body[0]);
		assertEquals(_version.getMinorVersion(), body[1]);
	}
	
	@Test
	public void testGetBodyBytesRandomField() {
		byte[] body = _testMessage.getBodyBytes();
		byte[] gmt = new byte[4];
		byte[] random = new byte[_random28.length];
		System.arraycopy(body, 2, gmt, 0, gmt.length);
		System.arraycopy(body, 6, random, 0, random.length);
		
		assertArrayEquals(ByteHelper.intToByteArray(_unixtime), gmt);
		assertArrayEquals(_random28, random);
	}
	
	@Test
	public void testGetBodyBytesSessionIdLengthField() {
		byte[] body = _testMessage.getBodyBytes();
		
		assertEquals(_sessionId.getLength(), body[34]);
	}
	
	@Test
	public void testGetBodyBytesSessionIdField() {
		byte[] body = _testMessage.getBodyBytes();
		byte[] sessionId = new byte[_sessionId.getLength()];
		System.arraycopy(body, 35, sessionId, 0, sessionId.length);
		
		assertArrayEquals(_sessionId.getSessionId(), sessionId);
	}

	
	@Test
	public void testGetBodyBytesCipherSuiteField() {
		byte[] body = _testMessage.getBodyBytes();
		int csIdx = 35 + _sessionId.getLength();
		short csCode = (short) ((body[csIdx] << 8) + body[csIdx + 1]); 
		
		assertEquals(_cipherSuite.getCode(), csCode);
	}
	
	@Test
	public void testHandshakeType() {
		assertEquals(_testMessage.getHandshakeType(), HandshakeType.server_hello);
	}


}
