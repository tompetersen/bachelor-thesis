package jProtocol.tls12.model.crypto;

import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsServerDhParams;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHPublicKeySpec;

/* TLS 1.2
 * A conventional Diffie-Hellman computation is performed.  The
   negotiated key (Z) is used as the pre_master_secret, and is converted
   into the master_secret, as specified above.  Leading bytes of Z that
   contain all zero bits are stripped before it is used as the
   pre_master_secret.
   Note: Diffie-Hellman parameters are specified by the server and may
   be either ephemeral or contained within the server’s certificate.
 */
public class TlsServerDhKeyAgreement {

	public static final int KEYSIZE = 512;
	
	private PrivateKey _privateServerDhKey;
	private DHPublicKeySpec _serverPublicKeySpec;
	
	/**
	 * Generates a new key pair with random values p and g.
	 */
	public TlsServerDhKeyAgreement() {
		SecureRandom rnd = TlsPseudoRandomNumberGenerator.getRandom();
		
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
		    kpg.initialize(KEYSIZE, rnd);
		    KeyPair kp = kpg.generateKeyPair();
		    _privateServerDhKey = kp.getPrivate();
		    
		    KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
		    _serverPublicKeySpec = (DHPublicKeySpec)keyFactory.getKeySpec(kp.getPublic(), DHPublicKeySpec.class);
		}
		catch (Exception e) {
			throw new RuntimeException("Exception thrown during creation of server DH params! Reason: " + e.getLocalizedMessage());
		}
	}

	public TlsServerDhParams getServerDhParams() {
		return new TlsServerDhParams(
				_serverPublicKeySpec.getP().toByteArray(), 
				_serverPublicKeySpec.getG().toByteArray(), 
				_serverPublicKeySpec.getY().toByteArray());
	}
	
	public byte[] computePreMasterSecret(TlsClientDhPublicKey clientPublicKey) throws TlsAsymmetricOperationException {
		KeyAgreement keyAgreement;
		try {
			BigInteger clientPublicKeyBI = new BigInteger(clientPublicKey.getPublicKey());
			KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");
			DHPublicKeySpec publicKeySpec = new DHPublicKeySpec(clientPublicKeyBI, _serverPublicKeySpec.getP(), _serverPublicKeySpec.getG());
			PublicKey publicClientDhKey = keyFactory.generatePublic(publicKeySpec);
			
			keyAgreement = KeyAgreement.getInstance("DiffieHellman");
		    keyAgreement.init(_privateServerDhKey);
			keyAgreement.doPhase(publicClientDhKey, true);
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("DH algorithm not found!");
		}
		catch (InvalidKeySpecException | InvalidKeyException e) {
			throw new TlsAsymmetricOperationException("DH key agreement failed! " + e.getLocalizedMessage());
		}
		
		   
		byte[] agreedKey = keyAgreement.generateSecret();

		return agreedKey;
	}
}
