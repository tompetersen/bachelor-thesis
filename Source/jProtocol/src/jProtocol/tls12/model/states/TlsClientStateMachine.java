package jProtocol.tls12.model.states;

import jProtocol.helper.ByteHelper;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.crypto.TlsClientDhKeyAgreement;
import jProtocol.tls12.model.crypto.TlsRsaCipher;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsServerDhParams;
import jProtocol.tls12.model.values.TlsVerifyData;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class TlsClientStateMachine extends TlsStateMachine {

	private TlsClientDhKeyAgreement _clientDhKeyAgreement;
	private TlsServerDhParams _serverDhParams;
	private List<TlsCertificate> _serverCertificateList; 
	private TlsRsaCipher _rsaCipher;
	
	/**
	 * Creates a client state machine.
	 * 
	 * @param registry a cipher suite registry
	 */
	public TlsClientStateMachine(TlsCipherSuiteRegistry registry) {
		super(registry);
		
		setTlsState(TlsStateType.CLIENT_INITIAL_STATE);
	}
	
	@Override
	public TlsServerDhParams getServerDhParams() {
		return _serverDhParams;
	}
	
	@Override
	public TlsClientDhPublicKey getClientDhPublicKey() {
		return (_clientDhKeyAgreement != null) ? _clientDhKeyAgreement.getClientPublicKey() : null;
	}
	
	@Override
	public List<TlsCertificate> getCertificateList() {
		return _serverCertificateList;
	}
	
	@Override
	protected TlsEncryptionParameters getEncryptionParameters(boolean isReceiving) {
		byte[] encKey = null;
		byte[] macKey = null;
		byte[] iv = null;
		long seqNum = 0;
		
		if (isReceiving) {
			if (currentReadCipherSuiteIsNotTlsNull()) {
				encKey = _currentReadConnectionState.getServerWriteEncryptionKey();
				macKey = _currentReadConnectionState.getServerWriteMacKey();
				iv = 	 _currentReadConnectionState.getServerWriteIv();
				seqNum =  _currentReadConnectionState.getSequenceNumber();
			}
		}
		else {
			if (currentWriteCipherSuiteIsNotTlsNull()) {
				encKey = _currentWriteConnectionState.getClientWriteEncryptionKey();
				macKey = _currentWriteConnectionState.getClientWriteMacKey();
				iv = 	 _currentWriteConnectionState.getClientWriteIv();
				seqNum = _currentWriteConnectionState.getSequenceNumber();
			}
		}

		return new TlsEncryptionParameters(seqNum, encKey, macKey, iv);
	}
	
	@Override
	public boolean isCorrectVerifyData(TlsVerifyData verifyData) {
		byte[] expected = _securityParameters.getFinishedVerifyDataForServer();
		
		return Arrays.equals(expected, verifyData.getBytes());
	}
	
	@Override
	public TlsVerifyData getVerifyDataToSend() {
		byte[] bytes = _securityParameters.getFinishedVerifyDataForClient();
		
		return new TlsVerifyData(bytes);
	}

	@Override
	protected String getEntityName() {
		return "Client";
	}
	
	/**
	 * Computes the master secret by performing a DH key exchange.
	 * 
	 * @param serverDhParams the DH parameters of the server
	 * 
	 * @throws TlsAsymmetricOperationException id the key agreement couldn't be completed
	 */
	public void performClientDhKeyAgreement(TlsServerDhParams serverDhParams) throws TlsAsymmetricOperationException {
		_serverDhParams = serverDhParams;
		_clientDhKeyAgreement = new TlsClientDhKeyAgreement(serverDhParams);
		
		byte[] premastersecret = _clientDhKeyAgreement.computePreMasterSecret();
		computeMasterSecret(premastersecret);
		MyLogger.info("Client agreed on premastersecret: " + ByteHelper.bytesToHexString(premastersecret));
	}
	
	/**
	 * Checks the signature of the DH parameters.
	 * 
	 * @param params the servers DH parameters 
	 * @param signedParams the signature
	 * 
	 * @return true, if the signature is correct, false otherwise
	 * 
	 * @throws TlsAsymmetricOperationException if the signature could not be checked
	 */
	public boolean checkDhParamSignature(TlsServerDhParams params, byte[] signedParams) throws TlsAsymmetricOperationException {
		byte[] clientRandom = _securityParameters.getClientRandom().getBytes();
		byte[] serverRandom = _securityParameters.getServerRandom().getBytes();
		byte[] paramsBytes = params.getBytes();

		ByteBuffer dataToSign = ByteBuffer.allocate(clientRandom.length + serverRandom.length + paramsBytes.length);
		
		dataToSign.put(clientRandom);
		dataToSign.put(serverRandom);
		dataToSign.put(paramsBytes);
		
		return _rsaCipher.checkSignature(dataToSign.array(), signedParams);
	}
	
	/**
	 * Sets the server certifiacte list.
	 * 
	 * @param certificateList the list of certificates
	 */
	public void setCertificateList(List<TlsCertificate> certificateList) {
		_serverCertificateList = certificateList;
	}
	
	/**
	 * Encrypts the plaintext with the public server RSA key. The RSA cipher suite must be set first.
	 * 
	 * @return the encrypted data
	 * 
	 * @throws TlsAsymmetricOperationException if the encryption process failed 
	 */
	public byte[] rsaEncrypt(byte[] plaintextData) throws TlsAsymmetricOperationException {
		if (_rsaCipher == null) {
			throw new RuntimeException("RSA Cipher must be set first!");
		}
		return _rsaCipher.encrypt(plaintextData);
	}

	/**
	 * Sets the RSA cipher for the public server key.
	 * 
	 * @param rsaPublicKey the servers public RSA key
	 */
	public void setRsaCipherForPublicKey(byte[] rsaPublicKey) {
		if (rsaPublicKey == null) {
			throw new IllegalArgumentException("Public key must be set");
		}
		
		TlsRsaCipher rsaCipher = new TlsRsaCipher(rsaPublicKey);
		_rsaCipher = rsaCipher;
	}
}
