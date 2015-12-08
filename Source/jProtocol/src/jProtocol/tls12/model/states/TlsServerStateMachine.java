package jProtocol.tls12.model.states;

import jProtocol.helper.ByteHelper;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.crypto.TlsRsaCipher;
import jProtocol.tls12.model.crypto.TlsServerDhKeyAgreement;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsServerDhParams;
import jProtocol.tls12.model.values.TlsVerifyData;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TlsServerStateMachine extends TlsStateMachine {

	private TlsServerDhKeyAgreement _serverDhKeyAgreement;
	private TlsClientDhPublicKey _clientDhPublicKey;
	private List<TlsCertificate> _certificateList; 
	private TlsRsaCipher _rsaCipher;
	
	/**
	 * Creates a server state machine object.
	 * 
	 * @param registry a cipher suite registry
	 */
	public TlsServerStateMachine(TlsCipherSuiteRegistry registry) {
		super(registry);
		
		createCertificate();
		createServerDhKeyAgreement();
		
		setTlsState(TlsStateType.SERVER_INITIAL_STATE);
	}

	private void createCertificate() {
		_rsaCipher = new TlsRsaCipher();
		
		TlsCertificate certificate = TlsCertificate.generateRsaCertificate(_rsaCipher.getEncodedPublicKey());
		List<TlsCertificate> certList = new ArrayList<>();
		certList.add(certificate);
		
		_certificateList = certList;
	}
	
	private void createServerDhKeyAgreement() {
		_serverDhKeyAgreement = new TlsServerDhKeyAgreement();
	}
	
	@Override
	public List<TlsCertificate> getCertificateList() {
		return _certificateList;
	}
	
	@Override
	public TlsServerDhParams getServerDhParams() {
		return _serverDhKeyAgreement.getServerDhParams();
	}
	
	@Override
	public TlsClientDhPublicKey getClientDhPublicKey() {
		return _clientDhPublicKey;
	}
	
	@Override
	public boolean isCorrectVerifyData(TlsVerifyData verifyData) {
		byte[] expected = _securityParameters.getFinishedVerifyDataForClient();
		
		return Arrays.equals(expected, verifyData.getBytes());
	}
	
	@Override
	public TlsVerifyData getVerifyDataToSend() {
		byte[] bytes = _securityParameters.getFinishedVerifyDataForServer();
		
		return new TlsVerifyData(bytes);
	}

	@Override
	protected String getEntityName() {
		return "Server";
	}
	
	@Override
	protected TlsEncryptionParameters getEncryptionParameters(boolean isReceiving) {
		byte[] encKey = null;
		byte[] macKey = null;
		byte[] iv = null;
		long seqNum = 0;
		
		if (isReceiving) {
			if (currentReadCipherSuiteIsNotTlsNull()) {
				encKey =  _currentReadConnectionState.getClientWriteEncryptionKey();
				macKey =  _currentReadConnectionState.getClientWriteMacKey();
				iv = 	  _currentReadConnectionState.getClientWriteIv();
				seqNum =  _currentReadConnectionState.getSequenceNumber();
			}
		}
		else {
			if (currentWriteCipherSuiteIsNotTlsNull()) {
				encKey =  _currentWriteConnectionState.getServerWriteEncryptionKey();
				macKey =  _currentWriteConnectionState.getServerWriteMacKey();
				iv = 	  _currentWriteConnectionState.getServerWriteIv();
				seqNum = _currentWriteConnectionState.getSequenceNumber();
			}
		}

		return new TlsEncryptionParameters(seqNum, encKey, macKey, iv);
	}
	
	/**
	 * Returns the signature of the servers DH parameters.
	 * 
	 * @return the signature
	 * 
	 * @throws TlsAsymmetricOperationException if the signature couldn't be computed
	 */
	public byte[] getSignedDhParams() throws TlsAsymmetricOperationException {
		/*  digitally-signed struct {
              opaque client_random[32];
              opaque server_random[32];
              ServerDHParams params;
          } signed_params;*/
		
		byte[] clientRandom = _securityParameters.getClientRandom().getBytes();
		byte[] serverRandom = _securityParameters.getServerRandom().getBytes();
		byte[] params = getServerDhParams().getBytes();

		ByteBuffer dataToSign = ByteBuffer.allocate(clientRandom.length + serverRandom.length + params.length);
		
		dataToSign.put(clientRandom);
		dataToSign.put(serverRandom);
		dataToSign.put(params);
		
		return _rsaCipher.signData(dataToSign.array());
	}
	
	/**
	 * Computes the pre master secret when using Diffie-Hellman key exchange.
	 * 
	 * @param clientPublicKey the clients DH public key
	 * 
	 * @throws TlsAsymmetricOperationException if the key couldn't be computed
	 */
	public void computePreMasterSecretForServerDhKeyAgreement(TlsClientDhPublicKey clientPublicKey) throws TlsAsymmetricOperationException {
		_clientDhPublicKey = clientPublicKey;
		byte[] premastersecret = _serverDhKeyAgreement.computePreMasterSecret(clientPublicKey);
		computeMasterSecret(premastersecret);
		MyLogger.info("[DH] Server agreed on premastersecret: " + ByteHelper.bytesToHexString(premastersecret));
	}
	
	/**
	 * Decrypts a RSA encrypted ciphertext with the servers private key.
	 * 
	 * @param ciphertext the ciphertext bytes
	 * 
	 * @return the decrypted plaintext
	 * 
	 * @throws TlsAsymmetricOperationException if the operation couldn't be completed 
	 */
	public byte[] rsaDecrypt(byte[] ciphertext) throws TlsAsymmetricOperationException {
		return _rsaCipher.decrypt(ciphertext);
	}
}
