package test.tls.crypto;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsMac;
import jProtocol.tls12.model.crypto.TlsMacParameters;
import jProtocol.tls12.model.values.TlsMacAlgorithm;

import org.junit.Test;

public class MacTest {
	
	private byte[] _message = "Hi There".getBytes(StandardCharsets.US_ASCII);
	private byte[] _key20 = ByteHelper.hexStringToByteArray("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b");
	private byte[] _key16 = ByteHelper.hexStringToByteArray("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b");
	
	@Test
	public void testNullMac() {
		TlsMac mac = new TlsMac(TlsMacAlgorithm.mac_null);
		assertNull(mac.computeMac(_key20, _message));
	}
	
	/*
	 * Test Vectors found in RFC 4231
	 * https://tools.ietf.org/html/rfc4231
	 */	
	@Test
	public void testSha256() {
		TlsMac mac = new TlsMac(TlsMacAlgorithm.mac_hmac_sha256);
		byte[] expected = ByteHelper.hexStringToByteArray("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7");
		
		assertArrayEquals(mac.computeMac(_key20, _message), expected);
	}
	
	@Test
	public void testSha384() {
		TlsMac mac = new TlsMac(TlsMacAlgorithm.mac_hmac_sha384);
		byte[] expected = ByteHelper.hexStringToByteArray("afd03944d84895626b0825f4ab46907f15f9dadbe4101ec682aa034c7cebc59cfaea9ea9076ede7f4af152e8b2fa9cb6");
		
		assertArrayEquals(mac.computeMac(_key20, _message), expected);
	}
	
	@Test
	public void testSha512() {
		TlsMac mac = new TlsMac(TlsMacAlgorithm.mac_hmac_sha512);
		byte[] expected = ByteHelper.hexStringToByteArray("87aa7cdea5ef619d4ff0b4241a1d6cb02379f4e2ce4ec2787ad0b30545e17cdedaa833b7d6b8a702038b274eaea3f4e4be9d914eeb61f1702e696c203a126854");
		
		assertArrayEquals(mac.computeMac(_key20, _message), expected);
	}
	
	/*
	 * Test vectors found in RFC 2202
	 * https://tools.ietf.org/html/rfc2202
	 */
	@Test
	public void testMd5() {
		TlsMac mac = new TlsMac(TlsMacAlgorithm.mac_hmac_md5);
		byte[] expected = ByteHelper.hexStringToByteArray("9294727a3638bb1c13f48ef8158bfc9d");
		
		assertArrayEquals(mac.computeMac(_key16, _message), expected);
	}
	
	@Test
	public void testSha1() {
		TlsMac mac = new TlsMac(TlsMacAlgorithm.mac_hmac_sha1);
		byte[] expected = ByteHelper.hexStringToByteArray("b617318655057264e28bc0b6fb378c8ef146be00");
		
		assertArrayEquals(mac.computeMac(_key20, _message), expected);
	}
	
	@Test
	public void testTlsMac() {
		TlsMac mac = new TlsMac(TlsMacAlgorithm.mac_hmac_sha1);
		
		byte[] message = new byte[14];
		Arrays.fill(message, (byte)0);		
		
		byte[] fragment = {0};		
		TlsMacParameters parameters = new TlsMacParameters(_key20, (long)0, (byte)0, (byte)0, (byte)0, (short)0, fragment);
		
		byte[] mac1 = mac.computeTlsMac(parameters); 
		byte[] mac2 = mac.computeMac(_key20, message);
		
		assertArrayEquals(mac1, mac2);
	}
}
