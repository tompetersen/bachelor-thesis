package test.tls.crypto;

import static org.junit.Assert.assertArrayEquals;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsHash;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class HashTest {

	private TlsHash _tlsHash = new TlsHash();

	/*
	 * Test vectors for SHA-256 found here:
	 * http://www.di-mgt.com.au/sha_testvectors.html
	 */
	@Test
	public void testHash() {
		byte[] input = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes(StandardCharsets.US_ASCII);
		byte[] result = _tlsHash.hash(input);
		byte[] expected = ByteHelper.hexStringToByteArray("248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1");
		
		assertArrayEquals(expected, result);
	}

}
