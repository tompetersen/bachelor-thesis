package jProtocol.tls12.model.values;

public class TlsVerifyData {

	private byte[] _verifyData;
	
	public TlsVerifyData(byte verifyData[]) {
		_verifyData = verifyData;
	}

	public byte[] getBytes() {
		return _verifyData;
	}

}
