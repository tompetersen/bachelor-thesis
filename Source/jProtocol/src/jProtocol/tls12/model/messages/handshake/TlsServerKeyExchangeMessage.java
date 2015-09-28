package jProtocol.tls12.model.messages.handshake;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsServerDhParams;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
      
       struct {
          opaque dh_p<1..2^16-1>;
          opaque dh_g<1..2^16-1>;
          opaque dh_Ys<1..2^16-1>;
      } ServerDHParams;     // Ephemeral DH parameters 
	 */
	
	private static final int LENGTH_FIELD_LENGTHS = 2;
	
	private TlsServerDhParams _dhParams;
	private byte[] _signedParams;
	
	public TlsServerKeyExchangeMessage(TlsServerDhParams dhParams, byte[] signedParams) {
		_dhParams = dhParams;
		_signedParams = signedParams;
	}

	public TlsServerKeyExchangeMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);
		
		int completeLength = unparsedContent.length;
		int parsedLength = 0;
		byte[] remainingUnparsedContent = new byte[unparsedContent.length];
		System.arraycopy(unparsedContent, 0, remainingUnparsedContent, 0, unparsedContent.length);
		
		//p
		byte[] p = getNextValueFromUnparsedContent(remainingUnparsedContent);
		parsedLength += LENGTH_FIELD_LENGTHS + p.length;
		remainingUnparsedContent = new byte[completeLength - parsedLength];
		System.arraycopy(unparsedContent, parsedLength, remainingUnparsedContent, 0, completeLength - parsedLength);
		
		//g
		byte[] g = getNextValueFromUnparsedContent(remainingUnparsedContent);
		parsedLength += LENGTH_FIELD_LENGTHS + g.length;
		remainingUnparsedContent = new byte[completeLength - parsedLength];
		System.arraycopy(unparsedContent, parsedLength, remainingUnparsedContent, 0, completeLength - parsedLength);
		
		//ys
		byte[] ys = getNextValueFromUnparsedContent(remainingUnparsedContent);
		parsedLength += LENGTH_FIELD_LENGTHS + ys.length;
		remainingUnparsedContent = new byte[completeLength - parsedLength];
		System.arraycopy(unparsedContent, parsedLength, remainingUnparsedContent, 0, completeLength - parsedLength);
		
		//signed
		byte[] signedParams = getNextValueFromUnparsedContent(remainingUnparsedContent);
		parsedLength += LENGTH_FIELD_LENGTHS + signedParams.length;
		remainingUnparsedContent = new byte[completeLength - parsedLength];
		System.arraycopy(unparsedContent, parsedLength, remainingUnparsedContent, 0, completeLength - parsedLength);
		
		_dhParams = new TlsServerDhParams(p, g, ys);
		_signedParams = signedParams;
	}
	
	private byte[] getNextValueFromUnparsedContent(byte[] remainingUnparsedContent) throws TlsDecodeErrorException {
		int unparsedLength = remainingUnparsedContent.length; 
		if (unparsedLength < LENGTH_FIELD_LENGTHS) {
			throw new TlsDecodeErrorException("DHE server key exchange message contains not enough information!");
		}
		
		byte[] lengthBytes = {remainingUnparsedContent[0], remainingUnparsedContent[1]};
		int length = ByteHelper.twoByteArrayToInt(lengthBytes);
		
		if (unparsedLength < length + LENGTH_FIELD_LENGTHS) {
			throw new TlsDecodeErrorException("Invalid length field in DHE server key exchange message!");
		}
		
		byte[] result = new byte[length];
		System.arraycopy(remainingUnparsedContent, LENGTH_FIELD_LENGTHS, result, 0, length);
		
		return result;
	}

	public TlsServerDhParams getDhParams() {
		return _dhParams;
	}

	public byte[] getSignedParams() {
		return _signedParams;
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.server_key_exchange;
	}

	@Override
	public byte[] getBodyBytes() {
		byte[] p = _dhParams.getDh_p();
		byte[] g = _dhParams.getDh_g();
		byte[] ys = _dhParams.getDh_Ys();
		
		int capacity = 4 * LENGTH_FIELD_LENGTHS + p.length + g.length + ys.length + _signedParams.length;
		ByteBuffer b = ByteBuffer.allocate(capacity);
		
		b.put(createLengthValueCombination(p));
		b.put(createLengthValueCombination(g));
		b.put(createLengthValueCombination(ys));
		b.put(createLengthValueCombination(_signedParams));
		
		return b.array();
	}
	
	private byte[] createLengthValueCombination(byte[] value) {
		int length = value.length;
		byte[] lengthBytes = ByteHelper.intToTwoByteArray(length);
		return ByteHelper.concatenate(lengthBytes, value);
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		ArrayList<KeyValueObject> serverParams = new ArrayList<>();
		KeyValueObject kvo = new KeyValueObject("dh_p", "0x" + ByteHelper.bytesToHexString(_dhParams.getDh_p()));
		serverParams.add(kvo);
		
		kvo = new KeyValueObject("dh_g", "0x" + ByteHelper.bytesToHexString(_dhParams.getDh_g()));
		serverParams.add(kvo);
		
		kvo = new KeyValueObject("dh_Ys", "0x" + ByteHelper.bytesToHexString(_dhParams.getDh_Ys()));
		serverParams.add(kvo);
		
		kvo = new KeyValueObject("ServerDHParams", serverParams);
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerKeyExchange_ServerDhParams.html"));
		result.add(kvo);
		
		kvo = new KeyValueObject("Signed params", "0x" + ByteHelper.bytesToHexString(_signedParams));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerKeyExchange_SignedParams.html"));
		result.add(kvo);
		
		return result;
	}

	@Override
	public String getBodyHtmlInfo() {
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ServerKeyExchange.html");
	}
}
