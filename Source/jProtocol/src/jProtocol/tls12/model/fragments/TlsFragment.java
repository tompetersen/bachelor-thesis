package jProtocol.tls12.model.fragments;

import jProtocol.Abstract.View.keyvaluetree.KeyValueObject;
import jProtocol.tls12.model.messages.TlsMessage;

public interface TlsFragment {

	/**
	 * Returns the length of the fragment which will be sent.
	 * 
	 * @return the fragment length
	 */
	public int getLength();
	
	/**
	 * Returns the bytes which will be sent in the fragment (including 
	 * the encrypted part and optional unencrypted data like IVs). 
	 * 
	 * @return the bytes to be sent
	 */
	public byte[] getBytes();

	/**
	 * Returns the unencrypted content of the fragment.
	 * 
	 * @return the unencrypted content
	 */
	public byte[] getContent();	
	
	/**
	 * Should return the view data (key value object) for the fragment and included message.
	 * 
	 * @param message the message view data should also be included for 
	 *  
	 * @return the view data
	 */
	public KeyValueObject getViewData(TlsMessage message);
	
	/**
	 * Should return the fragment name for displaying purposes. 
	 * 
	 * @return the fragment name
	 */
	public String getFragmentName();
}
