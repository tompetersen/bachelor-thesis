package jProtocol.tls12.model;

import jProtocol.tls12.model.fragments.TlsAeadFragment;
import jProtocol.tls12.model.fragments.TlsBlockFragment;
import jProtocol.tls12.model.fragments.TlsStreamFragment;

public class TlsFragmentFactory {
	
	private TlsSecurityParameters _securityParameters;
	
	/**
	 * 
	 * 
	 * @param plaintext
	 * @return
	 */
	public TlsFragment createFragmentFromPlaintext(TlsPlaintext plaintext) {
		TlsFragment result = null;
		
		switch (_securityParameters.getCipherType()) {
		case stream:
			result = createStreamFragmentFromPlaintext(plaintext);
			break;
		case block:
			result = createBlockFragmentFromPlaintext(plaintext);
			break;
		case aead:
			result = createAeadFragmentFromPlaintext(plaintext);
			break;
		}
		
		return result;
	}
	
	private TlsStreamFragment createStreamFragmentFromPlaintext(TlsPlaintext plaintext) {
		return null;
	}
	
	private TlsBlockFragment createBlockFragmentFromPlaintext(TlsPlaintext plaintext) {
		return null;
	}

	private TlsAeadFragment createAeadFragmentFromPlaintext(TlsPlaintext plaintext) {
		return null;
	}
}
