package jProtocol.tls12.model.fragments;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.ciphersuites.TlsStreamCipherSuite.TlsStreamEncryptionResult;
import jProtocol.tls12.model.messages.TlsMessage;
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
		
		resultList.add(new KeyValueObject("Content", message.getViewData()));
		resultList.add(new KeyValueObject("MAC", "0x"+ByteHelper.bytesToHexString(_encryptionResult.mac)));
		
		KeyValueObject result = new KeyValueObject("StreamFragment", resultList);
		
		return result;
	}
}
