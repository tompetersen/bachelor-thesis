package jProtocol.tls12.model.fragments;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.ciphersuites.TlsBlockCipherSuite.TlsBlockEncryptionResult;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.view.TlsUiConstants;
import java.util.ArrayList;

public class TlsBlockFragment implements TlsFragment {

	private TlsBlockEncryptionResult _encryptionResult;
	private byte[] _sentBytes;
	
	public TlsBlockFragment(TlsBlockEncryptionResult encResult) {
		_encryptionResult = encResult;
		_sentBytes = ByteHelper.concatenate(_encryptionResult.iv, _encryptionResult.blockCiphered);
	}

	@Override
	public byte[] getBytes() {
		return _sentBytes;
	}
	
	@Override
	public int getLength() {
		return _sentBytes.length;
	}
	
	@Override
	public byte[] getContent() {
		return _encryptionResult.content;
	}
	
	/**
	 * Returns the initialization vector used for the encryption.
	 * 
	 * @return the iv
	 */
	public byte[] getIv() {
		return _encryptionResult.iv;
	}

	/**
	 * Returns the computed MAC for the fragment.
	 * 
	 * @return the MAC
	 */
	public byte[] getMac() {
		return _encryptionResult.mac;
	}

	/**
	 * Returns the computed padding for the fragment.
	 * 
	 * @return the padding
	 */
	public byte[] getPadding() {
		return _encryptionResult.padding;
	}

	/**
	 * Returns the used padding length for the fragment.
	 * 
	 * @return the padding length
	 */
	public byte getPaddingLength() {
		return _encryptionResult.paddingLength;
	}

	@Override
	public KeyValueObject getViewData(TlsMessage message) {
		ArrayList<KeyValueObject> resultList = new ArrayList<>();
		
		KeyValueObject kvo = new KeyValueObject("IV", "0x"+ByteHelper.bytesToHexString(_encryptionResult.iv));
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/fragment/TLS12_Iv.html"));
		resultList.add(kvo);
		
		kvo = message.getViewData();
		kvo.setBackgroundColor(TlsUiConstants.ENCRYPTED_MESSAGE_FIELD_BACKGROUND);
		resultList.add(kvo);
		
		kvo = new KeyValueObject("MAC", "0x"+ByteHelper.bytesToHexString(_encryptionResult.mac));
		kvo.setBackgroundColor(TlsUiConstants.ENCRYPTED_MESSAGE_FIELD_BACKGROUND);
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/fragment/TLS12_Mac.html"));
		resultList.add(kvo);
		
		kvo = new KeyValueObject("Padding", "0x"+ByteHelper.bytesToHexString(_encryptionResult.padding));
		kvo.setBackgroundColor(TlsUiConstants.ENCRYPTED_MESSAGE_FIELD_BACKGROUND);
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/fragment/TLS12_Padding.html"));
		resultList.add(kvo);
		
		kvo = new KeyValueObject("Padding length", Byte.toString(_encryptionResult.paddingLength));
		kvo.setBackgroundColor(TlsUiConstants.ENCRYPTED_MESSAGE_FIELD_BACKGROUND);
		kvo.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/fragment/TLS12_PaddingLength.html"));
		resultList.add(kvo);
		
		KeyValueObject result = new KeyValueObject("BlockFragment", resultList);
		result.setHtmlHelpContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/fragment/TLS12_BlockFragment.html"));
		
		return result;
	}	
}
