package jProtocol.tls12.model.crypto;

import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsServerDhParams;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

public class TlsClientDhKeyAgreement {

	public static final int KEYSIZE = 512;
	
	private KeyPair _clientDhKeyPair;
	private PublicKey _serverPublicKey;
	
	/**
	 * Generates a new key pair with server values p and g.
	 * 
	 * @throws TlsAsymmetricOperationException if key generation failed due to invalid parameters
	 */
	public TlsClientDhKeyAgreement(TlsServerDhParams serverParams) throws TlsAsymmetricOperationException {
		try {
			BigInteger p = new BigInteger(serverParams.getDh_p());
			BigInteger g = new BigInteger(serverParams.getDh_g());
			DHParameterSpec dhParamSpec = new DHParameterSpec(p, g);	
			
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
		    kpg.initialize(dhParamSpec, TlsPseudoRandomNumberGenerator.getRandom());
		    _clientDhKeyPair = kpg.generateKeyPair();
		    
			BigInteger serverPublicKeyBI = new BigInteger(serverParams.getDh_Ys());
			KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
			DHPublicKeySpec publicKeySpec = new DHPublicKeySpec(serverPublicKeyBI, p, g);
			_serverPublicKey = keyFactory.generatePublic(publicKeySpec);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("DH algorithm not found!");
		}
		catch (InvalidAlgorithmParameterException | InvalidKeySpecException e) {
			throw new TlsAsymmetricOperationException("DH key generation failed! " + e.getLocalizedMessage());
		}
	}

	/**
	 * Returns the clients public DH key.
	 * 
	 * @return the public DH key
	 */
	public TlsClientDhPublicKey getClientPublicKey() {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
			DHPublicKeySpec publicKeySpec = keyFactory.getKeySpec(_clientDhKeyPair.getPublic(), DHPublicKeySpec.class);
		    return new TlsClientDhPublicKey(publicKeySpec.getY().toByteArray());
		}
		catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException("Public Client key could not be created! Reason: " + e.getLocalizedMessage());
		}
	}
	
	/**
	 * Computes the pre master secret from server params and client keys.
	 * 
	 * @return the pre master secret
	 * 
	 * @throws TlsAsymmetricOperationException if the operation failed due to invalid key parameters
	 */
	public byte[] computePreMasterSecret() throws TlsAsymmetricOperationException {
		try {
			KeyAgreement keyAgreement = KeyAgreement.getInstance("DiffieHellman");
		    keyAgreement.init(_clientDhKeyPair.getPrivate());
			keyAgreement.doPhase(_serverPublicKey, true);
			
			byte[] agreedKey = keyAgreement.generateSecret();
			return agreedKey;
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("DH algorithm not found!");
		}
		catch (InvalidKeyException e) {
			throw new TlsAsymmetricOperationException("DH key agreement failed! " + e.getLocalizedMessage());
		}
	}

}
