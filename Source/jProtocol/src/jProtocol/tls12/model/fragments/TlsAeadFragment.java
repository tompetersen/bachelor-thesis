package jProtocol.tls12.model.fragments;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.ciphersuites.TlsAeadCipherSuite.TlsAeadEncryptionResult;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.view.TlsUiConstants;
import java.util.ArrayList;

public class TlsAeadFragment implements TlsFragment {

	private TlsAeadEncryptionResult _encryptionResult;
	private byte[] _sentBytes;
	
	public TlsAeadFragment(TlsAeadEncryptionResult encResult) {
		_encryptionResult = encResult;
		_sentBytes = ByteHelper.concatenate(encResult.nonce_explicit, encResult.aeadCiphered);
	}

	@Override
	public byte[] getBytes() {
		return _sentBytes;
	}
	
	/**
	 * Returns the explicit nonce used for AEAD encryption.
	 * 
	 * @return the explicit nonce
	 */
	public byte[] getNonceExplicit() {
		return _encryptionResult.nonce_explicit;
	}

	@Override
	public int getLength() {
		return _sentBytes.length;
	}

	@Override
	public byte[] getContent() {
		return _encryptionResult.content;
	}

	@Override
	public KeyValueObject getViewData(TlsMessage message) {
		ArrayList<KeyValueObject> resultList = new ArrayList<>();
		
		KeyValueObject kvo = new KeyValueObject("Nonce explicit", "0x"+ByteHelper.bytesToHexString(_encryptionResult.nonce_explicit));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/fragment/TLS12_NonceExplicit.html"));
		resultList.add(kvo);
		
		kvo = message.getViewData();
		kvo.setBackgroundColor(TlsUiConstants.ENCRYPTED_MESSAGE_FIELD_BACKGROUND);
		resultList.add(kvo);
		
		KeyValueObject result = new KeyValueObject("AEADFragment", resultList);
		result.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/fragment/TLS12_AeadFragment.html"));
		
		return result;
	}
	
}
