package jProtocol.tls12.model.values;

/**
 * An enumeration of possible TLS mac algorithms.
 *  
 * @author Tom Petersen
 */
public enum TlsMacAlgorithm {
	mac_null,
	mac_hmac_md5,
	mac_hmac_sha1,
	mac_hmac_sha256,
	mac_hmac_sha384,
	mac_hmac_sha512
}