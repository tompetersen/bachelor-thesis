package test.tls;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsConnectionState;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsCipherType;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsMacAlgorithm;
import jProtocol.tls12.model.values.TlsRandom;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;

public class ConnectionStateTest {

	private class TestCipherSuite implements TlsCipherSuite {
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
		public TlsCipherType getCipherType() { return null; }

		@Override
		public TlsBulkCipherAlgorithm getBulkCipherAlgorithm() { return null; }

		@Override
		public byte getBlockLength() { return 0; }

		@Override
		public byte getRecordIvLength() { return 0; }

		@Override
		public TlsMacAlgorithm getMacAlgorithm() { return null; }

		@Override
		public byte getMacLength() { return 0; }
		
		@Override
		public TlsKeyExchangeAlgorithm getKeyExchangeAlgorithm() { return null; }

		@Override
		public TlsCiphertext plaintextToCiphertext(TlsPlaintext plaintext, TlsEncryptionParameters parameters) {
			return null;
		}

		@Override
		public TlsPlaintext ciphertextToPlaintext(byte[] ciphertextBytes, TlsEncryptionParameters parameters, TlsCipherSuiteRegistry registry, TlsKeyExchangeAlgorithm algorithm) throws TlsBadRecordMacException {
			return null;
		}

		
	}
	
	private TlsCipherSuite _cs;
	private TlsConnectionState _connectionState;
	private byte[] _random28 = new byte[28];
	private TlsRandom _tlsRandom = new TlsRandom(0, _random28);
	private byte[] _random48 = new byte[48];
	
	@Before
	public void setUp() throws Exception {
		_cs = new TestCipherSuite();
		
		_connectionState = new TlsConnectionState(_cs);
	}

	@Test
	public void testGetClientWriteMacKey() {
		_connectionState.computeKeys(_tlsRandom, _tlsRandom, _random48);
		
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
		assertArrayEquals(_connectionState.getClientWriteEncryptionKey(), expectedEcryptionKeys);
		assertArrayEquals(_connectionState.getServerWriteEncryptionKey(), expectedEcryptionKeys);
		assertArrayEquals(_connectionState.getClientWriteIv(), expectedIvs);
		assertArrayEquals(_connectionState.getServerWriteIv(), expectedIvs);
	}
}
