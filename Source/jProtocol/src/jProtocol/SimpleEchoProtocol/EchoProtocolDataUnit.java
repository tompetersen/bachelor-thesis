package jProtocol.SimpleEchoProtocol;

import jProtocol.ProtocolDataUnit;

public class EchoProtocolDataUnit extends ProtocolDataUnit {
	private String _payload;

	public String getPayload() {
		return _payload;
	}

	public void setPayload(String payload) {
		this._payload = payload;
	}
	
}
