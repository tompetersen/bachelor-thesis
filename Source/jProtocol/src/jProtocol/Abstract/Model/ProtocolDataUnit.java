package jProtocol.Abstract.Model;

public abstract class ProtocolDataUnit {
	
	private boolean _sentByClient;

	/**
	 * Returns if this is a client sent protocol data unit.
	 * 
	 * @return true for a protocol data unit sent by the client.
	 */
	public boolean hasBeenSentByClient() {
		return _sentByClient;
	}

	/**
	 * Sets the sender of this protocol data unit.
	 * 
	 * @param sentByClient true, if this protocol data unit has been sent by the client
	 */
	public void setSentByClient(boolean sentByClient) {
		_sentByClient = sentByClient;
	}
	
	/**
	 * Should return the byte representation of this protocol data unit.
	 * 
	 * @return the bytes
	 */
	public abstract byte[] getBytes();
}
