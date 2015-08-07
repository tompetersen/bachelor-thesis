package test.tls;

import jProtocol.tls12.model.TlsCipherSuite;
import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.TlsSecurityParameters.ConnectionEnd;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_NULL_WITH_NULL_NULL;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SecurityParametersTest {

	private TlsSecurityParameters _parameters;
	
	@Before
	public void setup() {
		_parameters = new TlsSecurityParameters(ConnectionEnd.server);
	}
	
	@Test
	public void testSetClientRandom() {
		byte[] random = new byte[32];
		_parameters.setClientRandom(random);
		
		assertEquals(random, _parameters.getClientRandom());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetClientRandomWrongSize() {
		byte[] random = new byte[30];
		_parameters.setClientRandom(random);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetClientRandomNull() {
		_parameters.setClientRandom(null);
	}
	
	@Test
	public void testSetServerRandom() {
		byte[] random = new byte[32];
		_parameters.setServerRandom(random);
		
		assertEquals(random, _parameters.getServerRandom());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetServerRandomWrongSize() {
		byte[] random = new byte[30];
		_parameters.setServerRandom(random);
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
		byte[] random32 = new byte[32];
		byte[] random40 = new byte[40];
		_parameters.setServerRandom(random32);
		_parameters.setClientRandom(random32);
		_parameters.computeMasterSecret(random40);
	}
	
	@Test(expected = RuntimeException.class)
	public void testComputeMasterSecretWithoutServerClientRandom() {
		byte[] random48 = new byte[40];
		_parameters.computeMasterSecret(random48);
	}
	
	@Test
	public void testGetMasterSecret() {
		byte[] random32 = new byte[32];
		byte[] random48 = new byte[48];
		_parameters.setServerRandom(random32);
		_parameters.setClientRandom(random32);
		_parameters.computeMasterSecret(random48);
		
		assertNotNull(_parameters.getMasterSecret());
	}
	
	@Test(expected = RuntimeException.class)
	public void testGetMasterSecretWithoutComputing() {
		_parameters.getMasterSecret();
	}
}
