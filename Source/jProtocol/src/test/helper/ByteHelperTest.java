package test.helper;

import static org.junit.Assert.*;
import jProtocol.helper.ByteHelper;

import org.junit.Test;

public class ByteHelperTest {

	@Test
	public void testHexStringToByteArray() {
		byte[] hexResult = ByteHelper.hexStringToBytes("0001100fff");
		byte[] expectedResult = {0,1,16,15,(byte)255};
		
		assertArrayEquals(hexResult, expectedResult);
	}

	@Test
	public void testConcatenate() {
		byte[] b1 = {1,2,3};
		byte[] b2 = {4,5,6};
		byte[] expectedResult = {1,2,3,4,5,6};
		
		assertArrayEquals(ByteHelper.concatenate(b1, b2), expectedResult);
	}

}
