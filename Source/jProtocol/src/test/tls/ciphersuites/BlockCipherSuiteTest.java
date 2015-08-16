package test.tls.ciphersuites;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.TlsPlaintext;
import jProtocol.tls12.model.ciphersuites.TlsBlockCipherSuite;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA;
import jProtocol.tls12.model.exceptions.TlsBadPaddingException;
import jProtocol.tls12.model.exceptions.TlsBadRecordMacException;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.exceptions.TlsException;
import jProtocol.tls12.model.fragments.TlsBlockFragment;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsContentType;
import jProtocol.tls12.model.values.TlsVersion;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class BlockCipherSuiteTest {

	private class TlsTestMessage extends TlsMessage {
		@Override
		public TlsContentType getContentType() {
			return TlsContentType.ApplicationData;
		}

		@Override
		public byte[] getBytes() {
			byte[] test = {3,1,4,1,5,9,2,6,5,35,9};
			return test;
		}
	}
	
	private TlsMessage _testMessage = new TlsTestMessage();
	private TlsBlockCipherSuite _cipherSuite = new TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA();
	private TlsPlaintext _plaintext;
	private TlsEncryptionParameters _parameters;
	private TlsCiphertext _ciphertext;
	
	@Before
	public void setUp() {
		TlsVersion version = TlsVersion.getTls12Version();
		_plaintext = new TlsPlaintext(_testMessage, version);
		
		byte[] encKey = new byte[_cipherSuite.getEncryptKeyLength()];
		Arrays.fill(encKey, (byte)0x22);
		byte[] macWriteKey = new byte[_cipherSuite.getMacKeyLength()];
		Arrays.fill(macWriteKey, (byte)0x33);
		_parameters = new TlsEncryptionParameters(0, encKey, macWriteKey, null);
		
		_ciphertext = _cipherSuite.plaintextToCiphertext(_plaintext, _parameters);
	}
	
	@Test
	public void testCiphertextContainsCorrectFragmentType() {
		assertTrue(_ciphertext.getFragment() instanceof TlsBlockFragment);
	}

	@Test
	public void testCiphertextFragmentHasCorrectIvLength() {
		TlsBlockFragment blockFragment = (TlsBlockFragment)_ciphertext.getFragment();
		byte[] iv = blockFragment.getIv();
		
		assertEquals(iv.length, (int)_cipherSuite.getRecordIvLength());
	}
	
	@Test
	public void testCiphertextFragmentHasMacLength() {
		TlsBlockFragment blockFragment = (TlsBlockFragment)_ciphertext.getFragment();
		byte[] mac = blockFragment.getMac();
		
		assertEquals(mac.length, (int)_cipherSuite.getMacLength());
	}
	
	@Test
	public void testCiphertextFragmentHasCorrectBlockLength() {
		TlsBlockFragment blockFragment = (TlsBlockFragment)_ciphertext.getFragment();
		byte[] padding = blockFragment.getPadding();
		byte[] content = blockFragment.getContent();
		byte[] mac = blockFragment.getMac();
		
		assertTrue((padding.length + content.length + mac.length + 1) % (int)_cipherSuite.getBlockLength() == 0);
	}
	
	@Test
	public void testCiphertextFragmentHasCorrectPadding() {
		TlsBlockFragment blockFragment = (TlsBlockFragment)_ciphertext.getFragment();
		byte[] padding = blockFragment.getPadding();
		byte paddingLength = blockFragment.getPaddingLength();
		
		boolean result = true;
		for (byte b : padding) {
			if (b != paddingLength) {
				result = false;
			}
		}
		
		assertTrue(result);
	}
	
	@Test
	public void testCiphertextFragmentBytesContainCorrectIv() {
		TlsBlockFragment blockFragment = (TlsBlockFragment)_ciphertext.getFragment();
		byte[] sent = blockFragment.getBytes();
		
		byte[] iv = blockFragment.getIv();
		byte[] sentIv = new byte[_cipherSuite.getRecordIvLength()];
		System.arraycopy(sent, 0, sentIv, 0, sentIv.length);
		
		assertArrayEquals(iv, sentIv);
	}
	
	@Test
	public void testCiphertextToPlaintext() throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		TlsPlaintext plaintext = _cipherSuite.ciphertextToPlaintext(_ciphertext, _parameters);
		
		assertArrayEquals(plaintext.getFragment(), _testMessage.getBytes());
	}
	
	@Test(expected = TlsBadRecordMacException.class)
	public void testWrongSequenceNumber() throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		TlsEncryptionParameters wrongParameters = new TlsEncryptionParameters(1337, _parameters.getEncryptionWriteKey(), _parameters.getMacWriteKey(), null);
		 _cipherSuite.ciphertextToPlaintext(_ciphertext, wrongParameters);
	}
	
	@Test(expected = TlsException.class)
	//Could be bad padding or bad record mac
	public void testWrongEncryptionWriteKey() throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		byte[] encKey = new byte[_cipherSuite.getEncryptKeyLength()];
		Arrays.fill(encKey, (byte)0x23);
		
		TlsEncryptionParameters wrongParameters = new TlsEncryptionParameters(0, encKey, _parameters.getMacWriteKey(), null);
		_cipherSuite.ciphertextToPlaintext(_ciphertext, wrongParameters);
	}
	
	@Test(expected = TlsBadRecordMacException.class)
	public void testWrongMacWriteKey() throws TlsBadRecordMacException, TlsBadPaddingException, TlsDecodeErrorException {
		byte[] macWriteKey = new byte[_cipherSuite.getMacKeyLength()];
		Arrays.fill(macWriteKey, (byte)0x34);
		
		TlsEncryptionParameters wrongParameters = new TlsEncryptionParameters(0, _parameters.getEncryptionWriteKey(), macWriteKey, null);
		_cipherSuite.ciphertextToPlaintext(_ciphertext, wrongParameters);
	}
}
