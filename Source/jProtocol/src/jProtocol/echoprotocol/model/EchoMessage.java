package jProtocol.echoprotocol.model;

import java.nio.charset.StandardCharsets;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.helper.ByteHelper;

public class EchoMessage extends ProtocolDataUnit {
	
	private String _messageContent;

	public EchoMessage(String messsageContent) {
		_messageContent = messsageContent;
	}
	
	public String getPayload() {
		return _messageContent;
	}
	
	public int getLength() {
		return _messageContent.length();
	}
	
	@Override
	public byte[] getMessageBytes() {
		byte[] lengthBytes = ByteHelper.intToByteArray(getLength());
		byte[] messageContentBytes = _messageContent.getBytes(StandardCharsets.US_ASCII);
		return ByteHelper.concatenate(lengthBytes, messageContentBytes);
	}

	@Override
	public String getTitle() {
		return "Echo message";
	}

	@Override
	public String getSubtitle() {
		return _messageContent;
	}
	
}
