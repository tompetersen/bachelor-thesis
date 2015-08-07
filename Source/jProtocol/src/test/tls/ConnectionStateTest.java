package test.tls;

import static org.junit.Assert.*;
import jProtocol.tls12.model.TlsCipherSuite;
import jProtocol.tls12.model.TlsConnectionState;
import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.TlsSecurityParameters.BulkCipherAlgorithm;
import jProtocol.tls12.model.TlsSecurityParameters.CipherType;
import jProtocol.tls12.model.TlsSecurityParameters.ConnectionEnd;
import jProtocol.tls12.model.TlsSecurityParameters.MacAlgorithm;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

public class ConnectionStateTest {

	private class TestCipherSuite extends TlsCipherSuite {
		@Override
		public String getName() {
			return "TestCipherSuite";
		}
		
		@Override
		public byte getMacKeyLength() {
			return 2; /* !!! */
		}
		
		@Override
		public byte getEncryptKeyLength() {
			return 3; /* !!! */
		}
		
		@Override
		public byte getFixedIvLength() {
			return 4; /* !!! */
		}

		@Override
		public short getCode() { return 0; }

		@Override
		public CipherType getCipherType() { return null; }

		@Override
		public BulkCipherAlgorithm getBulkCipherAlgorithm() { return null; }

		@Override
		public byte getBlockLength() { return 0; }

		@Override
		public byte getRecordIvLength() { return 0; }

		@Override
		public MacAlgorithm getMacAlgorithm() { return null; }

		@Override
		public byte getMacLength() { return 0; }
	}
	
	private TlsCipherSuite _cs;
	private TlsSecurityParameters _sp;
	private TlsConnectionState _connectionState;
	private byte[] _random32 = new byte[32];
	private byte[] _random48 = new byte[48];
	
	@Before
	public void setUp() throws Exception {
		_cs = new TestCipherSuite();
		
		_sp = new TlsSecurityParameters(ConnectionEnd.server);
		_sp.setCipherSuite(_cs);
		_sp.setClientRandom(_random32);
		_sp.setServerRandom(_random32);
		_sp.computeMasterSecret(_random48);
		
		_connectionState = new TlsConnectionState(_sp);
	}

	@Test
	public void testGetClientWriteMacKey() {
		_connectionState.computeKeys();
		
		assertNotNull(_connectionState.getClientWriteMacKey());
	}
	
	@Test(expected = RuntimeException.class)
	public void testGetClientWriteMacKeyWithoutComputation() {
		_connectionState.getClientWriteMacKey();
	}
	
	@Test
	public void testPrivateSetKeys() throws Exception {
		byte[] testKeyBlock = {2,2,2,2,3,3,3,3,3,3,4,4,4,4,4,4,4,4};
		
		//Reflection used to test private method without having to deal with PRF provided values.
		Method method = TlsConnectionState.class.getDeclaredMethod("setKeys", testKeyBlock.getClass());
		method.setAccessible(true);
		method.invoke(_connectionState, testKeyBlock);
	
		byte[] expectedMacKeys = {2,2};
		byte[] expectedEcryptionKeys = {3,3,3};
		byte[] expectedIvs = {4,4,4,4};
		
		assertArrayEquals(_connectionState.getClientWriteMacKey(), expectedMacKeys);
		assertArrayEquals(_connectionState.getServerWriteMacKey(), expectedMacKeys);
		assertArrayEquals(_connectionState.getClientWriteEncyrptionKey(), expectedEcryptionKeys);
		assertArrayEquals(_connectionState.getServerWriteEncyrptionKey(), expectedEcryptionKeys);
		assertArrayEquals(_connectionState.getClientWriteIv(), expectedIvs);
		assertArrayEquals(_connectionState.getServerWriteIv(), expectedIvs);
	}
}
