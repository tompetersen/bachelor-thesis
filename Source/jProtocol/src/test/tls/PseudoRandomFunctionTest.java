package test.tls;

import static org.junit.Assert.assertEquals;
import jProtocol.tls12.model.TlsPseudoRandomFunction;

import org.junit.Test;

public class PseudoRandomFunctionTest {

	private byte[] input1 = {12,13,14};
	private byte[] input2 = {12,13,14};
	
	@Test
	public void testPrf() {
		byte[] result = TlsPseudoRandomFunction.prf(input1, "label", input2, 20);
		assertEquals(result.length, 20);
		
		result = TlsPseudoRandomFunction.prf(input1, "", input2, 512);
		assertEquals(result.length, 512);
		
		result = TlsPseudoRandomFunction.prf(input1, "label", input2, 1023);
		assertEquals(result.length, 1023);
		
		//TODO: test for specific values
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

}
