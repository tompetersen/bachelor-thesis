package jProtocol.helper;

import java.nio.ByteBuffer;

public class ByteHelper {
	
	/**
	 * Converts a hex encoded byte string to a byte array.
	 * 
	 * @param s the hex string, e.g. 00 5f 73 f4 (without spaces)
	 * 
	 * @return the resulting byte array
	 */
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    
	    return data;
	}
	
	/**
	 * Concatenates two byte arrays.
	 * 
	 * @param a byte array a
	 * @param b byte array b
	 * 
	 * @return the concatenated byte array [a]+[b]
	 */
	public static byte[] concatenate(byte[] a, byte[] b) {
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		
		return result;
	}
	
	/**
	 * Creates byte array of bytes of a long variable.
	 * 
	 * @param l the long variable
	 * 
	 * @return the bytes
	 */
	public static byte[] longToByteArray(long l) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE);
	    buffer.putLong(l);
	    return buffer.array();
	}
	
	public static byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static int byteArrayToInt(byte[] b) {
		if (b.length != 4) {
			throw new IllegalArgumentException("Array must have length 4!");
		}
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
	
	public static byte[] intToThreeByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	
	public static int threeByteArrayToInt(byte[] b) {
		if (b.length != 3) {
			throw new IllegalArgumentException("Array must have length 3!");
		}
	    return   b[2] & 0xFF |
	            (b[1] & 0xFF) << 8 |
	            (b[0] & 0xFF) << 16;
	}
	
	public static byte[] intToTwoByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	
	public static int twoByteArrayToInt(byte[] b) {
		if (b.length != 2) {
			throw new IllegalArgumentException("Array must have length 2!");
		}
	    return   b[1] & 0xFF |
	            (b[0] & 0xFF) << 8;
	}
}
