package jProtocol.tls12.model.ciphersuites;

import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.ciphersuites.impl.TlsCipherSuite_DHE_RSA_WITH_AES_128_CBC_SHA;
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

	/**
	 * Creates a cipher suite registry offering access to implemented cipher suites.
	 */
	public TlsCipherSuiteRegistry() {
		_supportedCipherSuites = new HashMap<Short, TlsCipherSuite>();
		_preferredCipherSuite = -1;

		// TODO: Add cipher suites (maybe automagically)
		TlsCipherSuite cs = new TlsCipherSuite_NULL_WITH_NULL_NULL();
		_supportedCipherSuites.put(cs.getCode(), cs);

		try {
			cs = new TlsCipherSuite_RSA_WITH_AES_128_CBC_SHA();
			_supportedCipherSuites.put(cs.getCode(), cs);
		}
		catch (Exception e) {
			MyLogger.info("Couldn't register TLS_RSA_WITH_AES_128_CBC_SHA: " + e.getLocalizedMessage());
		}

		try {
			cs = new TlsCipherSuite_RSA_WITH_AES_128_GCM_SHA256();
			_supportedCipherSuites.put(cs.getCode(), cs);
		}
		catch (Exception e) {
			MyLogger.info("Couldn't register TLS_RSA_WITH_AES_128_GCM_SHA256: " + e.getLocalizedMessage());
		}
		
		try {
			cs = new TlsCipherSuite_DHE_RSA_WITH_AES_128_GCM_SHA256();
			_supportedCipherSuites.put(cs.getCode(), cs);
		}
		catch (Exception e) {
			MyLogger.info("Couldn't register TLS_DHE_RSA_WITH_AES_128_GCM_SHA256: " + e.getLocalizedMessage());
		}
		
		try {
			cs = new TlsCipherSuite_DHE_RSA_WITH_AES_128_CBC_SHA();
			_supportedCipherSuites.put(cs.getCode(), cs);
		}
		catch (Exception e) {
			MyLogger.info("Couldn't register TLS_DHE_RSA_WITH_AES_128_CBC_SHA: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Returns a TLSCipherSuite object registered for the code (see p.75 TLS 1.2 specification RFC 5246). 
	 * 
	 * @param code the cipher suite code
	 * 
	 * @return the cipher suite 
	 * 
	 * @throws TlsInvalidCipherSuiteException if no cipher suite is registered for the code
	 */
	public TlsCipherSuite cipherSuiteFromCode(short code) throws TlsInvalidCipherSuiteException {
		TlsCipherSuite suite = _supportedCipherSuites.get(code);
		if (suite != null) {
			return suite;
		}
		else {
			throw new TlsInvalidCipherSuiteException("Cipher suite for value " + code + " not found!");
		}
	}

	/**
	 * Returns all implemented cipher suites which can be used for a secure
	 * connection. TLS_NULL_WITH_NULL_NULL is not included.
	 * 
	 * @return a list of all implemented cipher suites
	 */
	public List<TlsCipherSuite> allCipherSuites() {
		List<TlsCipherSuite> result = new ArrayList<TlsCipherSuite>(_supportedCipherSuites.values());
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

	/**
	 * Sets the preferred cipher suite for the connection. 
	 * Should be called before connecting for the client.
	 * 
	 * @param cipherSuiteCode the preferred cipher suite code
	 */
	public void setPreferredCipherSuite(short cipherSuiteCode) {
		_preferredCipherSuite = cipherSuiteCode;
	}

	/**
	 * Returns the preferred cipher suite code. 
	 * 
	 * @return the preferred cipher suite code or -1 if no cipher suite is preferred
	 */
	public short getPreferredCipherSuite() {
		return _preferredCipherSuite;
	}

	/**
	 * Returns the cipher suite TLS_NULL_WITH_NULL_NULL (no encryption or mac).
	 * 
	 * @return the TLS null cipher suite
	 */
	public TlsCipherSuite getNullCipherSuite() {
		try {
			return cipherSuiteFromCode((short) 0);
		}
		catch (TlsInvalidCipherSuiteException e) {
			throw new RuntimeException("Tls NULL cipher suite not found !?");
		}
	}
}
