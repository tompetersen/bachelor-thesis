package jProtocol.tls12.model.fragments;

import jProtocol.tls12.model.TlsFragment;

public class TlsStreamFragment extends TlsFragment {

	//encrypted
  //protected byte[] _content
	private byte[] _mac;
	
	public TlsStreamFragment(byte[] content) {
		super(content);
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public byte[] getMac() {
		return _mac;
	}
}
