package jProtocol.tls12.model.values;

public class TlsVerifyData {

	/**
	 * The length of verify data in bytes.
	 */
	public static final int VERIFY_DATA_LENGTH = 12;
	
	private byte[] _verifyData;
	
	public TlsVerifyData(byte verifyData[]) {
		if (verifyData == null ||verifyData.length != VERIFY_DATA_LENGTH) {
			throw new IllegalArgumentException("Verify data must be " + VERIFY_DATA_LENGTH + " bytes long!");
		}
		_verifyData = verifyData;
	}

	public byte[] getBytes() {
		return _verifyData;
	}

}
