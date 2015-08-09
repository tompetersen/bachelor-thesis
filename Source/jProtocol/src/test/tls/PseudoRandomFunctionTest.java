package test.tls;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsPseudoRandomFunction;

import org.junit.Test;

public class PseudoRandomFunctionTest {

	private byte[] input1 = {12,13,14};
	private byte[] input2 = {12,13,14};
	
	@Test
	public void testPrfLength() {
		byte[] result = TlsPseudoRandomFunction.prf(input1, "label", input2, 20);
		assertEquals(result.length, 20);
		
		result = TlsPseudoRandomFunction.prf(input1, "", input2, 512);
		assertEquals(result.length, 512);
		
		result = TlsPseudoRandomFunction.prf(input1, "label", input2, 1023);
		assertEquals(result.length, 1023);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void testPrfNullParam1() {
		TlsPseudoRandomFunction.prf(null, "label", input2, 20);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void testPrfNullParam2() {
		TlsPseudoRandomFunction.prf(input1, null, input2, 20);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void testPrfNullParam3() {
		TlsPseudoRandomFunction.prf(input1, "label", null, 20);
	}
	
	@Test
	public void testPrfTestVector() {
		/*
		 * TLS 1.2 PRF test vector (SHA256 used) found here: 
		 * https://www.ietf.org/mail-archive/web/tls/current/msg03416.html
		 * and confirmed here:
		 * https://lists.oasis-open.org/archives/pkcs11/201411/msg00019.html
		 */
		byte[] secret = ByteHelper.hexStringToByteArray("9bbe436ba940f017b17652849a71db35");
		byte[] seed = ByteHelper.hexStringToByteArray("a0ba9f936cda311827a6f796ffd5198c");
		String label = "test label";
		byte[] expectedOutput = ByteHelper.hexStringToByteArray("e3f229ba727be17b8d122620557cd453c2aab21d07c3d495329b52d4e61edb5a6b301791e90d35c9c9a46b4e14baf9af0fa022f7077def17abfd3797c0564bab4fbc91666e9def9b97fce34f796789baa48082d122ee42c5a72e5a5110fff70187347b66");
		
		byte[] result = TlsPseudoRandomFunction.prf(secret, label, seed, 100);
		
		assertArrayEquals(result, expectedOutput);
	}

}
