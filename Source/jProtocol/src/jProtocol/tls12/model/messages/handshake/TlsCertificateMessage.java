package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TlsCertificateMessage extends TlsHandshakeMessage {

	/*
	  struct {
          ASN.1Cert certificate_list<0..2^24-1>;
      } Certificate;
	 */
	
	private static final int LIST_OF_CERTIFICATES_LENGTH_FIELD_LENGTH = 3;
	private static final int CERTIFICATE_LENGTH_FIELD_LENGTH = 3;
	
	private List<TlsCertificate> _certificates;

	public TlsCertificateMessage(List<TlsCertificate> certificates) {
		_certificates = certificates;
	}
	
	public TlsCertificateMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		
		_certificates = new ArrayList<>();
		
		int unparsedLength = unparsedContent.length; 
		
		if (unparsedLength < CERTIFICATE_LENGTH_FIELD_LENGTH + LIST_OF_CERTIFICATES_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Certificate Message contains not enough information");
		}
		
		byte[] listLengthBytes = new byte[LIST_OF_CERTIFICATES_LENGTH_FIELD_LENGTH];
		System.arraycopy(unparsedContent, 0, listLengthBytes, 0, LIST_OF_CERTIFICATES_LENGTH_FIELD_LENGTH);
		int listLength = ByteHelper.threeByteArrayToInt(listLengthBytes);
		
		if (listLength != unparsedLength - LIST_OF_CERTIFICATES_LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Certificate list length field contains invalid value!");
		}
		
		byte[] listBytes = new byte[listLength];
		System.arraycopy(unparsedContent, LIST_OF_CERTIFICATES_LENGTH_FIELD_LENGTH, listBytes, 0, listLength);
		
		int i = 0; 
		while (i < listLength) {
			byte[] certLengthBytes = {listBytes[i], listBytes[i+1], listBytes[i+2]};
			i += 3;
			int certLength = ByteHelper.threeByteArrayToInt(certLengthBytes);
			
			if (i + certLength > listLength) {
				throw new TlsDecodeErrorException("Certificate length field contains invalid value!");
			}
			
			byte[] certBytes = new byte[certLength];
			System.arraycopy(listBytes, i, certBytes, 0, certLength);
			
			_certificates.add(TlsCertificate.parseCertificate(certBytes));
			i += certLength;
		}
		
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate;
	}

	@Override
	public byte[] getBodyBytes() {
		int certificatesLength = 0;
		for (TlsCertificate cert : _certificates) {
			certificatesLength += CERTIFICATE_LENGTH_FIELD_LENGTH + cert.getLength();
		}
		
		ByteBuffer b = ByteBuffer.allocate(LIST_OF_CERTIFICATES_LENGTH_FIELD_LENGTH + certificatesLength);
		b.put(ByteHelper.intToThreeByteArray(certificatesLength));
		
		for (TlsCertificate cert : _certificates) {
			b.put(ByteHelper.intToThreeByteArray(cert.getLength()));
			b.put(cert.getBytes());
		}
		
		return b.array();
	}

	public List<TlsCertificate> getCertificates() {
		return _certificates;
	}

	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		List<KeyValueObject> certificates = new ArrayList<>();
		for (TlsCertificate cert : _certificates) {
			certificates.add(new KeyValueObject("", cert.getReadableCertificate()));
		}
		KeyValueObject kvo = new KeyValueObject("Certificates", certificates);
		result.add(kvo);
				
		return result;
	}
}
