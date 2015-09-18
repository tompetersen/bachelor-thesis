package test.tls.crypto;

import static org.junit.Assert.assertArrayEquals;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsAesGcmCipher;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class AesGcmCipherTest {

	private TlsAesGcmCipher _cipher;
	
	@Before
	public void setUp() {
		_cipher = new TlsAesGcmCipher();
	}
	
	@Test
	public void testBlockCipher() {
		byte[] key = new byte[16];
		Arrays.fill(key, (byte)0x12);
		
		byte[] nonce = new byte[12];
		Arrays.fill(key, (byte)0x23);
		
		byte[] additional = new byte[16];
		Arrays.fill(key, (byte)0x45);
		
		byte[] plaintext = new byte[64];
		Arrays.fill(plaintext, (byte)0xFF);
		
		byte[] encrypted = _cipher.encrypt(key, nonce, additional, plaintext);
		byte[] decrypted = _cipher.decrypt(key, nonce, additional, encrypted);
		
		assertArrayEquals(plaintext, decrypted);
	}
	
	/*
	 * Test vectors for AES GCM found in 
	 * The Galois/Counter Mode of Operation (GCM)
	 * David A. McGrew 
	 * John Viega 
	 * 
	 * http://csrc.nist.gov/groups/ST/toolkit/BCM/documents/proposedmodes/gcm/gcm-revised-spec.pdf
	 */
	@Test
	public void testBlockCipherWithTestVector() {
		byte[] key = ByteHelper.hexStringToBytes("feffe9928665731c6d6a8f9467308308");
		byte[] nonce = ByteHelper.hexStringToBytes("cafebabefacedbaddecaf888");
		byte[] additional = ByteHelper.hexStringToBytes("feedfacedeadbeeffeedfacedeadbeefabaddad2");
		byte[] plaintext = ByteHelper.hexStringToBytes("d9313225f88406e5a55909c5aff5269a86a7a9531534f7da2e4c303d8a318a721c3c0c95956809532fcf0e2449a6b525b16aedf5aa0de657ba637b39");
		byte[] expected = ByteHelper.hexStringToBytes("42831ec2217774244b7221b784d0d49ce3aa212f2c02a4e035c17e2329aca12e21d514b25466931c7d8f6a5aac84aa051ba30b396a0aac973d58e0915bc94fbc3221a5db94fae95ae7121a47");
		
		byte[] encrypted = _cipher.encrypt(key, nonce, additional, plaintext);
		byte[] decrypted = _cipher.decrypt(key, nonce, additional, encrypted);
		
		assertArrayEquals(expected, encrypted);
		assertArrayEquals(plaintext, decrypted);
	}

}
