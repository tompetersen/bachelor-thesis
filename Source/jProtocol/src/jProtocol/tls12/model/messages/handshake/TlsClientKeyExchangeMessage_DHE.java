package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import java.util.ArrayList;
import java.util.List;

public class TlsClientKeyExchangeMessage_DHE extends TlsClientKeyExchangeMessage {

	/*
	  struct {
          select (PublicValueEncoding) {
              case implicit: struct { };
              case explicit: opaque dh_Yc<1..2^16-1>;
          } dh_public;
      } ClientDiffieHellmanPublic;
	 */
	
	private byte[] _yc;
	
	public TlsClientKeyExchangeMessage_DHE(byte[] dhClientPublicValue) {
		_yc = dhClientPublicValue;
	}

	public TlsClientKeyExchangeMessage_DHE() {
		super();
		// TODO Parsing
	}

	@Override
	public byte[] getBodyBytes() {
		return _yc;
	}

	public byte[] getDiffieHellmenClientPublicValue() {
		return _yc;
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		//TODO: view data for DHE client key exchange message
		KeyValueObject kvo = new KeyValueObject("DH", "TODO");
		result.add(kvo);
				
		return result;
	}
}
 