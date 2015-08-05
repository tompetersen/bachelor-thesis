package jProtocol.helper;

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
	
}
