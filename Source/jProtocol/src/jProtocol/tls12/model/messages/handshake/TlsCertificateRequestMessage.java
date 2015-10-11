package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import java.util.ArrayList;
import java.util.List;

public class TlsCertificateRequestMessage extends TlsHandshakeMessage {

	/*
	 struct {
          ClientCertificateType certificate_types<1..2^8-1>;
          SignatureAndHashAlgorithm
            supported_signature_algorithms<2^16-1>;
          DistinguishedName certificate_authorities<0..2^16-1>;
      } CertificateRequest;
	 */
	
	/**
	 * Creates a certificate request message.
	 */
	public TlsCertificateRequestMessage() {
		//TODO: Used for authenticated client -> Implement if necessary
	}

	/**
	 * Creates a certificate request message by parsing sent bytes.
	 * 
	 * @param unparsedContent the sent bytes
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsCertificateRequestMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO: Used for authenticated client -> Implement parsing if necessary
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate_request;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		//TODO: Used for authenticated client -> view data for certificate request
		KeyValueObject kvo = new KeyValueObject("Certificates", "TODO");
		result.add(kvo);
				
		return result;
	}
	
	@Override
	public String getBodyHtmlInfo() {
		//TODO: Used for authenticated client -> view data for certificate request
		return "";
	}
	
}
