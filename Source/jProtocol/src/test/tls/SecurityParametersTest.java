package test.tls;

import jProtocol.tls12.model.TlsCipherSuite;
import jProtocol.tls12.model.TlsSecurityParameters;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuite_Null;

import org.junit.Before;
import org.junit.Test;

public class SecurityParametersTest {

	private TlsSecurityParameters _parameters;
	
	@Before
	public void setup() {
		_parameters = new TlsSecurityParameters();
	}
	
	@Test
	public void testSetClientRandom() {
		byte[] random = new byte[32];
		_parameters.setClientRandom(random);
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
		TlsCipherSuite cs = new TlsCipherSuite_Null();
		_parameters.setCipherSuite(cs);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetCipherSuiteNeg() {
		_parameters.setCipherSuite(null);
	}

	@Test
	public void testComputeMasterSecret() {
		byte[] random32 = new byte[32];
		byte[] random48 = new byte[48];
		_parameters.setServerRandom(random32);
		_parameters.setClientRandom(random32);
		_parameters.computeMasterSecret(random48);
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
}
