package test.tls;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsSymmetricCipher;
import jProtocol.tls12.model.values.TlsBulkCipherAlgorithm;
import jProtocol.tls12.model.values.TlsCipherType;

import org.junit.Before;
import org.junit.Test;

public class CipherTest {

	private TlsSymmetricCipher _blockCipher;
	
	
	@Before
	public void setUp() {
		_blockCipher = new TlsSymmetricCipher(TlsBulkCipherAlgorithm.cipher_aes, TlsCipherType.block);
	}
	
	@Test
	public void testBlockCipher() {
		byte[] key = new byte[16];
		Arrays.fill(key, (byte)0x11);
		
		byte[] iv = new byte[16];
		Arrays.fill(key, (byte)0x22);
		
		byte[] plaintext = new byte[64];
		Arrays.fill(plaintext, (byte)0xFF);
		
		byte[] encrypted = _blockCipher.encrypt(key, iv, plaintext);
		byte[] decrypted = _blockCipher.decrypt(key, iv, encrypted);
		
		assertArrayEquals(plaintext, decrypted);
	}
	
	/*
	 * Test vectors for AES found in RFC 3602
	 *  https://tools.ietf.org/html/rfc3602#section-4
	 */
	@Test
	public void testBlockCipherWithTestVector1() {
		byte[] key = ByteHelper.hexStringToByteArray("06a9214036b8a15b512e03d534120006");
		byte[] iv = ByteHelper.hexStringToByteArray("3dafba429d9eb430b422da802c9fac41");
		byte[] plaintext = "Single block msg".getBytes(StandardCharsets.US_ASCII);
		byte[] expected = ByteHelper.hexStringToByteArray("e353779c1079aeb82708942dbe77181a");
		
		byte[] encrypted = _blockCipher.encrypt(key, iv, plaintext);
		byte[] decrypted = _blockCipher.decrypt(key, iv, encrypted);
		
		assertArrayEquals(encrypted, expected);
		assertArrayEquals(plaintext, decrypted);
	}
	
	@Test
	public void testBlockCipherWithTestVector2() {
		byte[] key = ByteHelper.hexStringToByteArray("56e47a38c5598974bc46903dba290349");
		byte[] iv = ByteHelper.hexStringToByteArray("8ce82eefbea0da3c44699ed7db51b7d9");
		byte[] plaintext = ByteHelper.hexStringToByteArray("a0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebfc0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedf");
		byte[] expected = ByteHelper.hexStringToByteArray("c30e32ffedc0774e6aff6af0869f71aa0f3af07a9a31a9c684db207eb0ef8e4e35907aa632c3ffdf868bb7b29d3d46ad83ce9f9a102ee99d49a53e87f4c3da55");
		
		byte[] encrypted = _blockCipher.encrypt(key, iv, plaintext);
		byte[] decrypted = _blockCipher.decrypt(key, iv, encrypted);
		
		assertArrayEquals(encrypted, expected);
		assertArrayEquals(plaintext, decrypted);
	}

}
