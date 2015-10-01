package test.tls;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.fragments.TlsFragment;
import jProtocol.tls12.model.messages.TlsApplicationDataMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.values.TlsApplicationData;
import jProtocol.tls12.model.values.TlsVersion;
import org.junit.Before;
import org.junit.Test;

public class CiphertextTest {
	
	private class TlsTestFragment implements TlsFragment {
		@Override
		public int getLength() {
			return 3;
		}

		@Override
		public byte[] getBytes() {
			byte[] result = {4,5,6};
			return result;
		}

		@Override
		public byte[] getContent() {
			byte[] result = {7,8,9};
			return result;
		}

		@Override
		public KeyValueObject getViewData(TlsMessage message) {
			return null;
		}

		@Override
		public String getFragmentName() {
			return "";
		}
	}

	private byte[] _applicationData = {1,2,3};
	private TlsCiphertext _testCiphertext;
	private TlsApplicationDataMessage _testMessage;
	private TlsVersion _version = TlsVersion.getTls12Version();
	
	@Before
	public void setUp() throws Exception {
		_testMessage = new TlsApplicationDataMessage(new TlsApplicationData(_applicationData));
		_testCiphertext = new TlsCiphertext(_testMessage, _version, new TlsTestFragment());
	}

	@Test
	public void testGetBytesContentField() {
		assertEquals(_testMessage.getContentType().getValue(), _testCiphertext.getBytes()[0]);
	}
	
	@Test
	public void testGetBytesVersionField() {
		assertEquals(_version.getMajorVersion(), _testCiphertext.getBytes()[1]);
		assertEquals(_version.getMinorVersion(), _testCiphertext.getBytes()[2]);
	}
	
	@Test
	public void testGetBytesLengthField() {
		byte[] lengthBytes = {_testCiphertext.getBytes()[3], _testCiphertext.getBytes()[4]};
		int length = ByteHelper.twoByteArrayToInt(lengthBytes);
		
		assertEquals(3, length);
	}
	
	@Test
	public void testGetBytesFragmentField() {
		byte[] fragmentBytes = {_testCiphertext.getBytes()[5], _testCiphertext.getBytes()[6], _testCiphertext.getBytes()[7]};
		byte[] expected = {4, 5, 6};
		
		assertArrayEquals(expected, fragmentBytes);
	}

}
