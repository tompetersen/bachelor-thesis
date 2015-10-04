package jProtocol.tls12.model.ciphersuites;

import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_DHE_RSA_WITH_AES_128_GCM_SHA256;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_NULL_WITH_NULL_NULL;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_RSA_WITH_AES_128_GCM_SHA256;
import jProtocol.tls12.model.exceptions.TlsInvalidCipherSuiteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TlsCipherSuiteRegistry {
	
	private Map<Short, TlsCipherSuite> _supportedCipherSuites;
	private short _preferredCipherSuite;
	
	public TlsCipherSuiteRegistry() {
		_supportedCipherSuites = new HashMap<Short, TlsCipherSuite>();
		_preferredCipherSuite = -1;
		
		//TODO: Add cipher suites (maybe automagically)
		TlsCipherSuite cs = new TlsCipherSuite_NULL_WITH_NULL_NULL();
		_supportedCipherSuites.put(cs.getCode(), cs);
		
		cs = new TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA();
		_supportedCipherSuites.put(cs.getCode(), cs);
		
		cs = new TlsCipherSuite_RSA_WITH_AES_128_GCM_SHA256();
		_supportedCipherSuites.put(cs.getCode(), cs);
		
		cs = new TlsCipherSuite_DHE_RSA_WITH_AES_128_GCM_SHA256();
		_supportedCipherSuites.put(cs.getCode(), cs);
	}
	
	public TlsCipherSuite cipherSuiteFromValue(short value) throws TlsInvalidCipherSuiteException {
		TlsCipherSuite suite = _supportedCipherSuites.get(value);
		if (suite != null) {
			return suite;
		}
		else {
			throw new TlsInvalidCipherSuiteException("Cipher suite for value " + value + " not found!");
		}
	}
	
	public short valueFromCipherSuite(TlsCipherSuite cipherSuite) throws TlsInvalidCipherSuiteException {
		for (short s : _supportedCipherSuites.keySet()) {
			if (_supportedCipherSuites.get(s).getClass().equals(cipherSuite.getClass())) {
				return s;
			}
		}
		throw new TlsInvalidCipherSuiteException("Value for cipher suite " + cipherSuite.getName() + " not found!");
	}
	
	/**
	 * Returns all implemented cipher suites which can be used for a secure connection. TLS_NULL_WITH_NULL_NULL is not included.
	 * 
	 * @return a list of all implemented cipher suites
	 */
	public List<TlsCipherSuite> allCipherSuites() {
		List <TlsCipherSuite> result = new ArrayList<TlsCipherSuite>(_supportedCipherSuites.values());
		result.remove(getNullCipherSuite());
		Collections.sort(result, new Comparator<TlsCipherSuite>() {
			@Override
			public int compare(TlsCipherSuite o1, TlsCipherSuite o2) {
				if (o1.getCode() == _preferredCipherSuite) {
					return -1;
				}
				else if (o2.getCode() == _preferredCipherSuite) {
					return 1;
				}
				else {
					return o1.getName().compareTo(o2.getName());
				}
			}
		});
		
		return result;
	}
	
	public void setPreferredCipherSuite(short cipherSuiteCode) {
		_preferredCipherSuite = cipherSuiteCode;
	}
	
	public short getPreferredCipherSuite() {
		return _preferredCipherSuite;
	}
	
	public TlsCipherSuite getNullCipherSuite() {
		try {
			return cipherSuiteFromValue((short) 0);
		}
		catch (TlsInvalidCipherSuiteException e) {
			throw new RuntimeException("Tls NULL cipher suite not found !?");
		}
	}
}
