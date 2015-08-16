package jProtocol.tls12.model.values;

public class TlsApplicationData {
	
	private byte[] _applicationData;
	
	public TlsApplicationData(byte[] applicationData) {
		_applicationData = applicationData;
	}

	public byte[] getBytes() {
		return _applicationData;
	}

}
