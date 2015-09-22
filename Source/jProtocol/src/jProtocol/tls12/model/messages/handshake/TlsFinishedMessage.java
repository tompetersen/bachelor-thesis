package jProtocol.tls12.model.messages.handshake;

import java.util.ArrayList;
import java.util.List;
import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.helper.ByteHelper;
import jProtocol.tls12.htmlinfo.TlsHtmlInfoLoader;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsVerifyData;

public class TlsFinishedMessage extends TlsHandshakeMessage {

	/*
	 struct {
          opaque verify_data[verify_data_length];
      } Finished;
	 */
	
	private TlsVerifyData _verifyData;
	
	public TlsFinishedMessage(TlsVerifyData verifyData) {
		if (verifyData == null) {
			throw new IllegalArgumentException("Verify data must be set!");
		}
		_verifyData = verifyData;
	}

	public TlsFinishedMessage(byte[] unparsedContent) throws TlsDecodeErrorException {
		super(unparsedContent);

		if (unparsedContent.length != TlsVerifyData.VERIFY_DATA_LENGTH) {
			throw new TlsDecodeErrorException("Verify data must be " + TlsVerifyData.VERIFY_DATA_LENGTH + " bytes long!");
		}
		
		_verifyData = new TlsVerifyData(unparsedContent);
	}

	@Override
	public TlsHandshakeType getHandshakeType() {
		return TlsHandshakeType.finished;
	}

	@Override
	public byte[] getBodyBytes() {
		return _verifyData.getBytes(); //Fixed length VERIFY_DATA_LENGTH, therefore no length field needed
	}

	public TlsVerifyData getVerifyData() {
		return _verifyData;
	}
	
	@Override
	public List<KeyValueObject> getBodyViewData() {
		ArrayList<KeyValueObject> result = new ArrayList<>();
		
		KeyValueObject kvo = new KeyValueObject("VerifyData", "0x" + ByteHelper.bytesToHexString(_verifyData.getBytes()));
		kvo.setHtmlInfoContent(TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_VerifyData.html"));
		result.add(kvo);
				
		return result;
	}
	
	@Override
	public String getBodyHtmlInfo() {
		return TlsHtmlInfoLoader.loadHtmlInfoForFileName("messages/tlsmessages/handshake/TLS12_Finished.html");
	}
}
