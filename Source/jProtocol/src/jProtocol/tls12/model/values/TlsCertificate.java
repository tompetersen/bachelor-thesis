package jProtocol.tls12.model.values;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.exceptions.TlsDecodeErrorException;
import java.nio.charset.StandardCharsets;

public class TlsCertificate {

	private static final String CERT_HEADER = "Certificate for PublicKey: ";
	
	private String _certificateContent;
	private byte[] _rsaPublicKey;
	
		
	/**
	 * Generates a certificate from a public key.
	 * 
	 * @param publicKey the public key
	 * 
	 * @return the certificate
	 */
	public static TlsCertificate generateRsaCertificate(byte[] publicKey) {
		return new TlsCertificate(CERT_HEADER, publicKey);
	}
	
	/**
	 * Parses a certificate from bytes representation.
	 * 
	 * @param unparsedCertificateBytes the unparsed bytes
	 * 
	 * @return the certificate
	 * 
	 * @throws TlsDecodeErrorException if the certificate contains wrong length or header fields
	 */
	public static TlsCertificate parseCertificate(byte[] unparsedCertificateBytes) throws TlsDecodeErrorException {
		int certificateHeaderLength = CERT_HEADER.length();
		int unparsedLenght = unparsedCertificateBytes.length;
		int keyLength = unparsedLenght - certificateHeaderLength;
		
		if (unparsedCertificateBytes.length <= certificateHeaderLength) {
			throw new TlsDecodeErrorException("Certificate bytes don't contain enough information!");
		}
		
		byte[] certHeaderBytes = new byte[certificateHeaderLength];
		System.arraycopy(unparsedCertificateBytes, 0, certHeaderBytes, 0, certificateHeaderLength);
		String certHeader = new String(certHeaderBytes, StandardCharsets.US_ASCII);
		byte[] rsaKey = new byte[keyLength];
		System.arraycopy(unparsedCertificateBytes, certificateHeaderLength, rsaKey, 0, keyLength);
		
		if (!certHeader.equals(CERT_HEADER)) {
			throw new TlsDecodeErrorException("Invalid certificate format!");
		}
		
		return new TlsCertificate(certHeader, rsaKey);
	}
	
	private TlsCertificate(String certificateContent, byte[] publicKey) {
		_certificateContent = certificateContent;
		_rsaPublicKey = publicKey;
	}
	
	/**
	 * Returns the included public key.
	 * 
	 * @return the public key
	 */
	public byte[] getRsaPublicKey() {
		return _rsaPublicKey;
	}
	
	/**
	 * Returns a byte representation of the certificate.
	 *  
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return ByteHelper.concatenate(_certificateContent.getBytes(StandardCharsets.US_ASCII), _rsaPublicKey);
	}
	
	/**
	 * Returns the certificate byte represenatation length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return _certificateContent.length() + _rsaPublicKey.length;
	}
	
	/**
	 * Returns a readable representation of the certificate.
	 * 
	 * @return the readable certificate
	 */
	public String getReadableCertificate() {
		return _certificateContent + "0x" + ByteHelper.bytesToHexString(_rsaPublicKey);
	}
}
