package jProtocol.tls12.model.states;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
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

public class TlsClientStateMachine extends TlsStateMachine {

	private TlsClientDhKeyAgreement _clientDhKeyAgreement;
	private TlsServerDhParams _serverDhParams;
	private List<TlsCertificate> _serverCertificateList; 
	private TlsRsaCipher _rsaCipher;
	
	public TlsClientStateMachine(TlsCipherSuiteRegistry registry) {
		super(registry);
		
		setTlsState(TlsStateType.CLIENT_INITIAL_STATE);
	}
	
	public void createClientDhKeyAgreementFromServerValues(TlsServerDhParams serverDhParams) throws TlsAsymmetricOperationException {
		_serverDhParams = serverDhParams;
		_clientDhKeyAgreement = new TlsClientDhKeyAgreement(serverDhParams);
		
		byte[] premastersecret = _clientDhKeyAgreement.computePreMasterSecret();
		computeMasterSecret(premastersecret);
		MyLogger.info("Client agreed on premastersecret: " + ByteHelper.bytesToHexString(premastersecret));
	}

	public TlsClientDhPublicKey getClientDhPublicKey() {
		return (_clientDhKeyAgreement != null) ? _clientDhKeyAgreement.getClientPublicKey() : null;
	}
	
	@Override
	public TlsServerDhParams getServerDhParams() {
		return _serverDhParams;
	}
	
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
	
	@Override
	public List<TlsCertificate> getCertificateList() {
		return _serverCertificateList;
	}

	public void setCertificateList(List<TlsCertificate> certificateList) {
		_serverCertificateList = certificateList;
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
	
	public TlsRsaCipher getRsaCipher() {
		if (_rsaCipher == null) {
			throw new RuntimeException("RSA Cipher must be set first!");
		}
		return _rsaCipher;
	}

	public void setRsaCipher(TlsRsaCipher rsaCipher) {
		_rsaCipher = rsaCipher;
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


}
