package jProtocol.Abstract.Model;

import java.util.Arrays;

public abstract class ProtocolDataUnit {

	private boolean _sentByClient;
	private byte[] _alteredBytes;

	/**
	 * Returns whether this is a client sent protocol data unit.
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
		if (!Arrays.equals(alteredBytes, getMessageBytes())) {
			_alteredBytes = alteredBytes;
		}
		else {
			_alteredBytes = null;
		}
	}
	
	/**
	 * Returns whether this message bytes have been altered by the user before sending.
	 * 
	 * @return true, if the bytes have been altered
	 */
	public boolean hasAlteredBytes() {
		return (_alteredBytes != null);
	}

	/**
	 * Returns the bytes of this protocol data unit. If the bytes have been
	 * altered, the new bytes are returned.
	 * 
	 * @return the bytes (the altered bytes if set)
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
	
	/**
	 * Should return the title of the protocol data unit, used in the UI list.
	 * 
	 * @return the protocol data unit title
	 */
	public abstract String getTitle();
	
	/**
	 * Should return the subtitle of the protocol data unit, used in the UI list.
	 * 
	 * @return the protocol data unit title
	 */
	public abstract String getSubtitle();
}
