package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsCertificateRequestMessage extends TlsHandshakeMessage {

	/*
	 struct {
          ClientCertificateType certificate_types<1..2^8-1>;
          SignatureAndHashAlgorithm
            supported_signature_algorithms<2^16-1>;
          DistinguishedName certificate_authorities<0..2^16-1>;
      } CertificateRequest;
	 */
	
	//TODO: Used for authenticated client -> Implement if necessary
	public TlsCertificateRequestMessage() {
		
	}

	public TlsCertificateRequestMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO Parsing for authenticated client -> Implement if necessary
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate_request;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
