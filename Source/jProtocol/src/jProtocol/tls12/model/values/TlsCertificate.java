package jProtocol.tls12.model.values;

import java.nio.charset.StandardCharsets;

public class TlsCertificate {

	private String _certificateContent;
	
	
	public static TlsCertificate generateCertificate() {
		return new TlsCertificate("CERTIFICATE");
	}
	
	private TlsCertificate(String certificateContent) {
		_certificateContent = certificateContent;
	}
	
	public byte[] getBytes() {
		return _certificateContent.getBytes(StandardCharsets.US_ASCII);
	}
	
	public int getLength() {
		return _certificateContent.length();
	}

}
