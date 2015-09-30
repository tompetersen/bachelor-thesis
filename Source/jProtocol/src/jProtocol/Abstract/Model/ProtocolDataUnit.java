package jProtocol.Abstract.Model;

public abstract class ProtocolDataUnit {

	private boolean _sentByClient;
	private byte[] _alteredBytes;

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
	 * Sets new bytes for this protocol data unit, so protocol behaviour can be
	 * explored.
	 * 
	 * @param alteredBytes the new bytes
	 */
	public void setAlteredBytes(byte[] alteredBytes) {
		_alteredBytes = alteredBytes;
	}

	/**
	 * Returns the bytes of this protocol data unit. If the bytes have been
	 * altered, the new bytes are returned.
	 * 
	 * @return the bytes (altered if set)
	 */
	public byte[] getBytes() {
		return (_alteredBytes != null) ? _alteredBytes : getMessageBytes();
	}

	/**
	 * Should return the byte representation of this message.
	 * 
	 * @return the bytes
	 */
	public abstract byte[] getMessageBytes();
}
