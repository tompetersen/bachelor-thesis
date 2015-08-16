package jProtocol.tls12.model.messages.handshake;

import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsCertificateVerifyMessage extends TlsHandshakeMessage {

	/*
	  struct {
           digitally-signed struct {
               opaque handshake_messages[handshake_messages_length];
           }
      } CertificateVerify;
	 */
	
	//TODO: Used for authenticated client -> Implement if necessary
	public TlsCertificateVerifyMessage() {

	}

	public TlsCertificateVerifyMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO Parsing for authenticated client -> Implement if necessary
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate_verify;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}

}
