package jProtocol.tls12.model.fragments;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsStreamCipherSuite.TlsStreamEncryptionResult;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.view.TlsUiConstants;
import java.awt.Color;
import java.util.ArrayList;

public class TlsStreamFragment implements TlsFragment {

	private TlsStreamEncryptionResult _encryptionResult;
	
	public TlsStreamFragment(TlsStreamEncryptionResult encResult) {
		_encryptionResult = encResult;
	}

	@Override
	public byte[] getBytes() {
		return _encryptionResult.streamCiphered;
	}
	
	/**
	 * Returns the computed MAC for the fragment.
	 * 
	 * @return the MAC
	 */
	public byte[] getMac() {
		return _encryptionResult.mac;
	}

	@Override
	public int getLength() {
		return _encryptionResult.streamCiphered.length;
	}

	@Override
	public byte[] getContent() {
		return _encryptionResult.content;
	}
	
	@Override
	public KeyValueObject getViewData(TlsMessage message) {
		ArrayList<KeyValueObject> resultList = new ArrayList<>();
		
		KeyValueObject kvo = message.getViewData();
		kvo.setBackgroundColor(TlsUiConstants.ENCRYPTED_MESSAGE_FIELD_BACKGROUND);
		resultList.add(kvo);
		
		String mac = ByteHelper.bytesToHexString(_encryptionResult.mac);
		kvo = new KeyValueObject("MAC", mac.length() > 0 ? "0x" + mac : "");
		kvo.setBackgroundColor(TlsUiConstants.ENCRYPTED_MESSAGE_FIELD_BACKGROUND);
		resultList.add(kvo);
		
		KeyValueObject result = new KeyValueObject("StreamFragment", resultList);
		
		return result;
	}
}
