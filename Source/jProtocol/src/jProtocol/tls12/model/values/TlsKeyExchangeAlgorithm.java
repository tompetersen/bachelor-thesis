package jProtocol.tls12.model.values;

/**
 * An enumeration of possible TLS key exchange algorithms.
 *  
 * @author Tom Petersen
 */
public enum TlsKeyExchangeAlgorithm {
	dhe_dss, 
	dhe_rsa, 
	dh_anon, 
	rsa, 
	dh_dss, 
	dh_rsa;
}
