package jProtocol.tls12.model.fragments;

import jProtocol.tls12.model.TlsFragment;

public class TlsAeadFragment extends TlsFragment {

	private byte[] _nonceExplicit;
	
	//encrypted
  //protected byte[] _content
	
	public TlsAeadFragment(byte[] content, byte[] nonceExplicit) {
		super(content);
		
		_nonceExplicit = nonceExplicit;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getNonceExplicit() {
		return _nonceExplicit;
	}

}
