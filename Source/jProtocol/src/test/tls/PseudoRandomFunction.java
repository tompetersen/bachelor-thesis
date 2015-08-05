package test.tls;

import static org.junit.Assert.*;
import jProtocol.tls12.model.TlsPseudoRandomFunction;

import org.junit.Test;

public class PseudoRandomFunction {

	@Test
	public void testPrf() {
		/*
		 * TLS 1.2 PRF test vector (SHA256 used) found here: 
		 * https://www.ietf.org/mail-archive/web/tls/current/msg03416.html
		 * and confirmed here:
		 * https://lists.oasis-open.org/archives/pkcs11/201411/msg00019.html
		 */
		byte[] secret = hexStringToByteArray("9bbe436ba940f017b17652849a71db35");
		byte[] seed = hexStringToByteArray("a0ba9f936cda311827a6f796ffd5198c");
		String label = "test label";
		byte[] expectedOutput = hexStringToByteArray("e3f229ba727be17b8d122620557cd453c2aab21d07c3d495329b52d4e61edb5a6b301791e90d35c9c9a46b4e14baf9af0fa022f7077def17abfd3797c0564bab4fbc91666e9def9b97fce34f796789baa48082d122ee42c5a72e5a5110fff70187347b66");
		
		byte[] result = TlsPseudoRandomFunction.prf(secret, label, seed, 100);
		
		assertArrayEquals(result, expectedOutput);
	}

	@Test
	public void testConcatenate() {
		byte[] b1 = {1,2,3};
		byte[] b2 = {4,5,6};
		byte[] expectedResult = {1,2,3,4,5,6};
		
		assertArrayEquals(TlsPseudoRandomFunction.concatenate(b1, b2), expectedResult);
	}
	
	public byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

}
