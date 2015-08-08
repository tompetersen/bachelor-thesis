package jProtocol.tls12.model.fragments;

public abstract class TlsFragment {

	/**
	 * Returns the length of the fragment which will be sent.
	 * 
	 * @return the fragment length
	 */
	public abstract int getLength();
	
	/**
	 * Returns the bytes which will be sent in the fragment (including 
	 * the encrypted part and optional unencrypted data like IVs). 
	 * 
	 * @return the bytes to be sent
	 */
	public abstract byte[] getBytes();

	/**
	 * Returns the unencrypted content of the fragment.
	 * 
	 * @return the unencrypted content
	 */
	public abstract byte[] getContent();	
}
