package jProtocol.tls12.model.messages.handshake;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.nio.ByteBuffer;
import java.util.List;

public class TlsCertificateMessage extends TlsHandshakeMessage {

	/*
	  struct {
          ASN.1Cert certificate_list<0..2^24-1>;
      } Certificate;
	 */
	
	private List<TlsCertificate> _certificates;
	
	public TlsCertificateMessage(List<TlsCertificate> certificates) {
		_certificates = certificates;
	}
	
	public TlsCertificateMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO Parsing
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate;
	}

	@Override
	public byte[] getBodyBytes() {
		int certificatesLength = 3; //certificates length field 3 bytes
		
		for (TlsCertificate cert : _certificates) {
			certificatesLength += 3 + cert.getLength(); //length field 3 bytes
		}
		
		ByteBuffer b = ByteBuffer.allocate(certificatesLength);
		b.put(ByteHelper.intToThreeByteArray(certificatesLength));
		for (TlsCertificate cert : _certificates) {
			b.put(ByteHelper.intToThreeByteArray(cert.getLength()));
			b.put(cert.getBytes());
		}
		
		return b.array();
	}

}
