package jProtocol.tls12.model.crypto;

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
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeySpecException 
	 */
	public TlsClientDhKeyAgreement(TlsServerDhParams serverParams) throws InvalidAlgorithmParameterException, InvalidKeySpecException {
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
	}

	
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
	
	public byte[] computePreMasterSecret() throws InvalidKeySpecException, InvalidKeyException  {
		KeyAgreement keyAgreement;
		try {
			keyAgreement = KeyAgreement.getInstance("DiffieHellman");
		    keyAgreement.init(_clientDhKeyPair.getPrivate());
			keyAgreement.doPhase(_serverPublicKey, true);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("DH algorithm not found!");
		}
		   
		byte[] agreedKey = keyAgreement.generateSecret();
		return agreedKey;
	}

}
