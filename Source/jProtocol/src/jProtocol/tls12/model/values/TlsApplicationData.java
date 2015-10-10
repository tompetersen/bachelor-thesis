package jProtocol.tls12.model.values;

public class TlsApplicationData {
	
	private byte[] _applicationData;
	
	/**
	 * Creates an appication data object from bytes.
	 * 
	 * @param applicationData the applicatzion data bytes
	 */
	public TlsApplicationData(byte[] applicationData) {
		_applicationData = applicationData;
	}

	/**
	 * Returns the bytes of the application data.
	 * 
	 * @return the bytess
	 */
	public byte[] getBytes() {
		return _applicationData;
	}

}
