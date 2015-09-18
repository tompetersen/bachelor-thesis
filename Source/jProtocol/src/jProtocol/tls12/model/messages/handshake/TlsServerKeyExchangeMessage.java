package jProtocol.tls12.model.messages.handshake;

import java.util.ArrayList;
import java.util.List;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;

public class TlsServerKeyExchangeMessage extends TlsHandshakeMessage {

	/*
	  struct {
          select (KeyExchangeAlgorithm) {
              case dh_anon:
                  ServerDHParams params;
              case dhe_dss:
              case dhe_rsa:
                  ServerDHParams params;
                  digitally-signed struct {
                      opaque client_random[32];
                      opaque server_random[32];
                      ServerDHParams params;
                  } signed_params;
              case rsa:
              case dh_dss:
              case dh_rsa:
                  struct {} ;
                 // message is omitted for rsa, dh_dss, and dh_rsa 
              // may be extended, e.g., for ECDH -- see [TLSECC] 
          };
      } ServerKeyExchange;
	 */
	
	//TODO: Used for DHE -> Implement when necessary
	public TlsServerKeyExchangeMessage() {
	}

	public TlsServerKeyExchangeMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		// TODO Parsing
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.server_key_exchange;
	}

	@Override
	public byte[] getBodyBytes() {
		return null;
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		//TODO: view data for DH server key exchange message
		KeyValueObject kvo = new KeyValueObject("DH", "TODO");
		result.add(kvo);
				
		return result;
	}

}
