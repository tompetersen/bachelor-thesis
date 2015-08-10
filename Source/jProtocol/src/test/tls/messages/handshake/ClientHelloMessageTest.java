package test.tls.messages.handshake;

import static org.junit.Assert.*;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA;
import jProtocol.tls12.model.messages.handshake.TlsClientHelloMessage;
import jProtocol.tls12.model.messages.handshake.TlsHandshakeMessage;
import jProtocol.tls12.model.values.TlsHandshakeType.HandshakeType;
import jProtocol.tls12.model.values.TlsRandom;
import jProtocol.tls12.model.values.TlsSessionId;
import jProtocol.tls12.model.values.TlsVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ClientHelloMessageTest {

	private TlsHandshakeMessage _testMessage;
	private TlsVersion _version = TlsVersion.getTls12Version();
	private int _unixtime = 0x2342;
	private byte[] _random28;
	private TlsRandom _clientRandom;
	private TlsSessionId _sessionId;
	private TlsCipherSuite _testCipherSuite = new TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA();
	private List<TlsCipherSuite> _cipherSuites;
	
	@Before
	public void setUp() {
		_random28 = new byte[28];
		Arrays.fill(_random28, (byte)0xF0);
		_clientRandom = new TlsRandom(_unixtime, _random28);
		
		_sessionId = new TlsSessionId(_random28);
		
		_cipherSuites = new ArrayList<>();
		_cipherSuites.add(_testCipherSuite);
		
		_testMessage = new TlsClientHelloMessage(_version, _clientRandom, _sessionId, _cipherSuites);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullVersionParameter() {
		new TlsClientHelloMessage(null, _clientRandom, _sessionId, _cipherSuites);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullRandomParameter() {
		new TlsClientHelloMessage(_version, null, _sessionId, _cipherSuites);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullSessionIdParameter() {
		new TlsClientHelloMessage(_version, _clientRandom, null, _cipherSuites);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullCipherSuitesParameter() {
		new TlsClientHelloMessage(_version, _clientRandom, _sessionId, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyCipherSuitesParameter() {
		new TlsClientHelloMessage(_version, _clientRandom, _sessionId, new ArrayList<TlsCipherSuite>());
	}
	
	@Test
	public void testMessageBodyLength() {
		int expected = 2; //version
		expected += 32; //random
		expected += 1 + _sessionId.getLength(); //sessionId and length byte
		expected += 2 + 2 * _cipherSuites.size(); //ciphersuites and length bytes
		
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
	public void testGetBodyBytesCipherSuitesLengthField() {
		byte[] body = _testMessage.getBodyBytes();
		int csLengthIdx = 35 + _sessionId.getLength();
		int length = (body[csLengthIdx] << 8) + body[csLengthIdx + 1]; 
				
		assertEquals(2 * _cipherSuites.size(), length);
	}
	
	@Test
	public void testGetBodyBytesCipherSuitesField() {
		byte[] body = _testMessage.getBodyBytes();
		int csIdx = 35 + _sessionId.getLength() + 2;
		short csCode = (short) ((body[csIdx] << 8) + body[csIdx + 1]); 
		
		assertEquals(_testCipherSuite.getCode(), csCode);
	}
	
	@Test
	public void testHandshakeType() {
		assertEquals(HandshakeType.client_hello, _testMessage.getHandshakeType());
	}

}
