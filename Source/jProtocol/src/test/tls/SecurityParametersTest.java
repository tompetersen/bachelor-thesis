package test.tls;

import java.util.Arrays;

import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_NULL_WITH_NULL_NULL;
import jProtocol.tls12.model.values.TlsConnectionEnd;
import jProtocol.tls12.model.values.TlsRandom;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SecurityParametersTest {

	private TlsSecurityParameters _parameters;
	private byte[] _random28 = new byte[28];
	private TlsRandom _tlsRandom;
	
	@Before
	public void setup() {
		_parameters = new TlsSecurityParameters(TlsConnectionEnd.server);
		Arrays.fill(_random28, (byte)0x11);
		_tlsRandom = new TlsRandom(0x115599CC, _random28);
	}
	
	@Test
	public void testSetClientRandom() {
		_parameters.setClientRandom(_tlsRandom);
		assertArrayEquals(_tlsRandom.getBytes(), _parameters.getClientRandom().getBytes());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetClientRandomNull() {
		_parameters.setClientRandom(null);
	}
	
	@Test
	public void testSetServerRandom() {
		_parameters.setServerRandom(_tlsRandom);
		
		assertArrayEquals(_tlsRandom.getBytes(), _parameters.getServerRandom().getBytes());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetServerRandomNull() {
		_parameters.setServerRandom(null);
	}
	
	@Test
	public void testSetCipherSuite() {
		TlsCipherSuite cs = new TlsCipherSuite_NULL_WITH_NULL_NULL();
		_parameters.setCipherSuite(cs);
		
		assertEquals(cs.getCipherType(), _parameters.getCipherType());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetCipherSuiteNull() {
		_parameters.setCipherSuite(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testComputeMasterSecretWrongSize() {
		byte[] random40 = new byte[40];
		_parameters.setServerRandom(_tlsRandom);
		_parameters.setClientRandom(_tlsRandom);
		_parameters.computeMasterSecret(random40);
	}
	
	@Test(expected = RuntimeException.class)
	public void testComputeMasterSecretWithoutServerClientRandom() {
		byte[] random48 = new byte[40];
		_parameters.computeMasterSecret(random48);
	}
	
	@Test
	public void testGetMasterSecret() {
		byte[] random48 = new byte[48];
		_parameters.setServerRandom(_tlsRandom);
		_parameters.setClientRandom(_tlsRandom);
		_parameters.computeMasterSecret(random48);
		
		assertNotNull(_parameters.getMasterSecret());
	}
	
	@Test(expected = RuntimeException.class)
	public void testGetMasterSecretWithoutComputing() {
		_parameters.getMasterSecret();
	}
}
