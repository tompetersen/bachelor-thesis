package jProtocol.tls12.model.fragments;

import jProtocol.tls12.model.TlsFragment;

public class TlsBlockFragment extends TlsFragment {

	private byte[] _iv;
	
	//encrypted
  //protected byte[] _content
	private byte[] _mac;
	private byte[] _padding;
	private byte _paddingLength;
	
	public TlsBlockFragment(byte[] content, byte[] iv) {
		super(content);
		
		_iv = iv;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public byte[] getIv() {
		return _iv;
	}

	public byte[] getMac() {
		return _mac;
	}

	public byte[] getPadding() {
		return _padding;
	}

	public byte getPaddingLength() {
		return _paddingLength;
	}
	
	

}
