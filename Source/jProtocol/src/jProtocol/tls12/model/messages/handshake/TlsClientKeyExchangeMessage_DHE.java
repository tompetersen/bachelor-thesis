package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import java.util.ArrayList;
import java.util.List;

public class TlsClientKeyExchangeMessage_DHE extends TlsClientKeyExchangeMessage {

	/*
	  struct {
          select (PublicValueEncoding) {
              case implicit: struct { };
              case explicit: opaque dh_Yc<1..2^16-1>; //->2 Bytes length field
          } dh_public;
      } ClientDiffieHellmanPublic;
	 */
	
	private static final int LENGTH_FIELD_LENGTH = 2;
	
	private TlsClientDhPublicKey _dhPublicKey; //Yc
	
	/**
	 * 
	 * 
	 * @param dhPublicKey
	 */
	public TlsClientKeyExchangeMessage_DHE(TlsClientDhPublicKey dhPublicKey) {
		_dhPublicKey = dhPublicKey;
	}

	/**
	 * Creates a Diffie-Hellman ephemeral client key exchange message by parsing sent bytes.
	 * 
	 * @param unparsedContent the sent bytes
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsClientKeyExchangeMessage_DHE(byte[] unparsedMessageContent) throws TlsDecodeErrorException {
		super();
		
		int unparsedLength = unparsedMessageContent.length; 
		
		if (unparsedLength <= LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("DHE client key exchange message contains not enough information!");
		}
		
		byte[] lengthBytes = {unparsedMessageContent[0], unparsedMessageContent[1]};
		int length = ByteHelper.twoByteArrayToInt(lengthBytes);
		
		if (unparsedLength != length + LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid length field in DHE client key exchange message!");
		}
		
		byte[] yc = new byte[length];
		System.arraycopy(unparsedMessageContent, LENGTH_FIELD_LENGTH, yc, 0, length);
		
		_dhPublicKey = new TlsClientDhPublicKey(yc);	
	}
	
	/**
	 * Returns the client Diffie-Hellman public key.
	 * 
	 * @return the DH public key
	 */
	public TlsClientDhPublicKey getDiffieHellmanClientPublicKey() {
		return _dhPublicKey;
	}

	@Override
	public byte[] getBodyBytes() {
		byte[] yc = _dhPublicKey.getPublicKey();
		int length = yc.length;
		byte[] lengthBytes = ByteHelper.intToTwoByteArray(length);
		
		return ByteHelper.concatenate(lengthBytes, yc);
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		KeyValueObject kvo = new KeyValueObject("DH Param dh_Yc", "0x" + ByteHelper.bytesToHexString(_dhPublicKey.getPublicKey()));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientKeyExchange_ClientDhPublic.html"));
		result.add(kvo);
				
		return result;
	}
	
	@Override
	public String getBodyHtmlInfo() {
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientKeyExchange.html");
	}
}
 