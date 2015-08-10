package jProtocol.tls12.model.values;

public class TlsSessionId {

	private byte[] _sessionId;
	
	public TlsSessionId(byte[] sessionId) {
		if (sessionId == null) {
			throw new IllegalArgumentException("SessionID must be set!");
		}
		if (sessionId.length < 0 || sessionId.length > 32) {
			throw new IllegalArgumentException("SessionID must be empty or 1 to 32 bytes long!");
		}
		
		_sessionId = sessionId;
	}

	public byte[] getSessionId() {
		return _sessionId;
	}
	
	public int getLength() {
		return _sessionId.length;
	}
	
	public boolean isEmpty() {
		return _sessionId.length == 0;
	}
	
}
