package jProtocol.tls12.model.values;

import jProtocol.helper.ByteHelper;

public class TlsSessionId {

	private byte[] _sessionId;
	
	/**
	 * Creates a session ID object.
	 * 
	 * @param sessionId the session ID bytes. Must be between 0 and 32 bytes.
	 */
	public TlsSessionId(byte[] sessionId) {
		if (sessionId == null) {
			sessionId = new byte[0];
		}
		if (sessionId.length < 0 || sessionId.length > 32) {
			throw new IllegalArgumentException("SessionID must be empty or 1 to 32 bytes long!");
		}
		
		_sessionId = sessionId;
	}

	/**
	 * Returns the session ID bytes.
	 * 
	 * @return the bytes
	 */
	public byte[] getSessionId() {
		return _sessionId;
	}
	
	/**
	 * Returns the session ID length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return _sessionId.length;
	}
	
	/**
	 * Returns whether the session ID has no bytes set.
	 * 
	 * @return true, if the session ID is empty
	 */
	public boolean isEmpty() {
		return _sessionId.length == 0;
	}

	@Override
	public String toString() {
		return (_sessionId.length > 0) ? "0x" + ByteHelper.bytesToHexString(_sessionId) : "";
	}
}
