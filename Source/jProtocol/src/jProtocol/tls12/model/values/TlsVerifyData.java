package jProtocol.tls12.model.values;

import java.util.Arrays;

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

	@Override
	public int hashCode() {
		return Arrays.hashCode(_verifyData);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TlsVerifyData))
			return false;
		TlsVerifyData other = (TlsVerifyData) obj;
		if (!Arrays.equals(_verifyData, other._verifyData))
			return false;
		return true;
	}

	public byte[] getBytes() {
		return _verifyData;
	}

}
