package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_DHE_RSA_WITH_AES_128_GCM_SHA256;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_NULL_WITH_NULL_NULL;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_RSA_WITH_AES_128_GCM_SHA256;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TlsCipherSuiteRegistry {
	
	private Map<Short, TlsCipherSuite> _supportedCipherSuites;
	
	public TlsCipherSuiteRegistry() {
		_supportedCipherSuites = new HashMap<Short, TlsCipherSuite>();
		
		//TODO: Add cipher suites (maybe automagically)
		TlsCipherSuite cs = new TlsCipherSuite_NULL_WITH_NULL_NULL();
		_supportedCipherSuites.put(cs.getCode(), cs);
//		
//		cs = new TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA();
//		_supportedCipherSuites.put(cs.getCode(), cs);
//		
//		cs = new TlsCipherSuite_RSA_WITH_AES_128_GCM_SHA256();
//		_supportedCipherSuites.put(cs.getCode(), cs);
		
		cs = new TlsCipherSuite_DHE_RSA_WITH_AES_128_GCM_SHA256();
		_supportedCipherSuites.put(cs.getCode(), cs);
	}
	
	public TlsCipherSuite cipherSuiteFromValue(short value) {
		TlsCipherSuite suite = _supportedCipherSuites.get(value);
		if (suite != null) {
			return suite;
		}
		else {
			throw new IllegalArgumentException("Cipher suite for value " + value + " not found!");
		}
	}
	
	public short valueFromCipherSuite(TlsCipherSuite cipherSuite) {
		for (short s : _supportedCipherSuites.keySet()) {
			if (_supportedCipherSuites.get(s).getClass().equals(cipherSuite.getClass())) {
				return s;
			}
		}
		throw new IllegalArgumentException("Value for cipher suite " + cipherSuite.getName() + " not found!");
	}
	
	public List<TlsCipherSuite> allCipherSuites() {
		List <TlsCipherSuite> result = new ArrayList<TlsCipherSuite>(_supportedCipherSuites.values());
		result.remove(getNullCipherSuite());
		return result;
	}
	
	public TlsCipherSuite getNullCipherSuite() {
		return cipherSuiteFromValue((short) 0);
	}
}
