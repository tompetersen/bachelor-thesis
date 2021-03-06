package jProtocol.tls12.model.messages.handshake;

import java.util.ArrayList;
import java.util.List;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsRsaEncryptedPreMasterSecret;

public class TlsClientKeyExchangeMessage_RSA extends TlsClientKeyExchangeMessage {

	/*
	 struct {
          public-key-encrypted PreMasterSecret pre_master_secret;
      } EncryptedPreMasterSecret;
	 */
	
	/* Following http://tools.ietf.org/html/rfc3447#section-7.2.1
	 * RSAES-PKCS1-V1_5-ENCRYPT ((n, e), M)
	 * Input: 
	 * (n, e)   recipient's RSA public key (k denotes the length in octets 
	 * 			of the modulus n) 
	 * M        message to be encrypted, an octet string of length mLen, 
	 * 			where mLen <= k - 11 
	 * Output: 
	 * C        ciphertext, an octet string of length k
	 */
	private static final int LENGTH_FIELD_LENGTH = 2;
	
	private TlsRsaEncryptedPreMasterSecret _encPreMasterSecret;

	/**
	 * Creates a RSA client key exchange message.
	 * 
	 * @param rsaEncryptedPremasterSecret the RSA encrypted premaster secret
	 */
	public TlsClientKeyExchangeMessage_RSA(TlsRsaEncryptedPreMasterSecret rsaEncryptedPremasterSecret) {
		_encPreMasterSecret = rsaEncryptedPremasterSecret;
	}

	/**
	 * Creates a RSA client key exchange message by parsing sent bytes.
	 * 
	 * @param unparsedMessageContent the sent bytes
	 * 
	 * @throws TlsDecodeErrorException if the message has invalid format
	 */
	public TlsClientKeyExchangeMessage_RSA(byte[] unparsedMessageContent) throws TlsDecodeErrorException {
		super();

		int unparsedLength = unparsedMessageContent.length; 
		
		if (unparsedLength <= LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("RSA client key exchange message contains not enough information!");
		}
		
		byte[] lengthBytes = {unparsedMessageContent[0], unparsedMessageContent[1]};
		int length = ByteHelper.twoByteArrayToInt(lengthBytes);
		
		if (unparsedLength != length + LENGTH_FIELD_LENGTH) {
			throw new TlsDecodeErrorException("Invalid length field in RSA client key exchange message!");
		}
		
		byte[] premastersecret = new byte[length];
		System.arraycopy(unparsedMessageContent, LENGTH_FIELD_LENGTH, premastersecret, 0, length);
		
		_encPreMasterSecret = new TlsRsaEncryptedPreMasterSecret(premastersecret);
	}
	
	/**
	 * Returns the RSA encrypted pre master secret.
	 * 
	 * @return the encrypted pre master secret
	 */
	public TlsRsaEncryptedPreMasterSecret getRsaEncryptedPreMasterSecret() {
		return _encPreMasterSecret;
	}

	@Override
	public byte[] getBodyBytes() {
		byte[] encPreMasterSecretBytes = _encPreMasterSecret.getEncryptedPreMasterSecret();
		byte[] lengthBytes = ByteHelper.intToTwoByteArray(encPreMasterSecretBytes.length); 
		return ByteHelper.concatenate(lengthBytes, encPreMasterSecretBytes);
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		KeyValueObject kvo = new KeyValueObject("EncyptedPreMasterSecret", "0x" + ByteHelper.bytesToHexString(_encPreMasterSecret.getEncryptedPreMasterSecret()));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientKeyExchange_EncryptedPreMasterSecret.html"));
		result.add(kvo);
				
		return result;
	}
	
	@Override
	public String getBodyHtmlInfo() {
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_ClientKeyExchange.html");
	}
}
