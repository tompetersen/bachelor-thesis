package jProtocol.tls12.model.messages.handshake;

import java.util.ArrayList;
import java.util.List;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
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
		// TODO: Used for authenticated client -> Implement parsing if necessary
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.certificate_verify;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		//TODO: Used for authenticated client -> view data for certificate verify message
		KeyValueObject kvo = new KeyValueObject("CertificateVerify", "TODO");
		result.add(kvo);
				
		return result;
	}

}
