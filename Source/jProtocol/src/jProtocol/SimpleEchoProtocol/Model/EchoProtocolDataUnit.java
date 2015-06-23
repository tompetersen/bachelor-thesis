package jProtocol.SimpleEchoProtocol.Model;

import jProtocol.Abstract.Model.ProtocolDataUnit;

public class EchoProtocolDataUnit extends ProtocolDataUnit {
	
	private int _version;
	private String _payload;

	public String getPayload() {
		return _payload;
	}

	public void setPayload(String payload) {
		_payload = payload;
	}
	
	public int getVersion() {
		return _version;
	}

	public void setVersion(int version) {
		_version = version;
	}
	
}
