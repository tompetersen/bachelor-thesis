package jProtocol.tls12.model.states;

import jProtocol.helper.ByteHelper;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.ciphersuites.TlsEncryptionParameters;
import jProtocol.tls12.model.crypto.TlsRsaCipher;
import jProtocol.tls12.model.crypto.TlsServerDhKeyAgreement;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.values.TlsCertificate;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsServerDhParams;
import jProtocol.tls12.model.values.TlsVerifyData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TlsServerStateMachine extends TlsStateMachine {

	private TlsServerDhKeyAgreement _serverDhKeyAgreement;
	private TlsClientDhPublicKey _clientDhPublicKey;
	private List<TlsCertificate> _certificateList; 
	private TlsRsaCipher _rsaCipher;
	
	public TlsServerStateMachine() {
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
	
	public TlsServerDhParams getServerDhParams() {
		return _serverDhKeyAgreement.getServerDhParams();
	}
	
	public byte[] getSignedDhParams() {
		/*  digitally-signed struct {
              opaque client_random[32];
              opaque server_random[32];
              ServerDHParams params;
          } signed_params;*/
		//TODO: signed struct
		
		return new byte[0];
	}

	@Override
	public TlsClientDhPublicKey getClientDhPublicKey() {
		return _clientDhPublicKey;
	}
	
	public void computePreMasterSecretForServerDhKeyAgreement(TlsClientDhPublicKey clientPublicKey) throws TlsAsymmetricOperationException {
		_clientDhPublicKey = clientPublicKey;
		byte[] premastersecret = _serverDhKeyAgreement.computePreMasterSecret(clientPublicKey);
		computeMasterSecret(premastersecret);
		MyLogger.info("[DH] Server agreed on premastersecret: " + ByteHelper.bytesToHexString(premastersecret));
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
	
	public TlsRsaCipher getRsaCipher() {
		return _rsaCipher;
	}
	
	@Override
	public boolean isCorrectVerifyData(TlsVerifyData verifyData) {
		byte[] masterSecret = _securityParameters.getMasterSecret();
		byte[] expected = _securityParameters.getFinishedVerifyDataForClient(masterSecret);
		
		return Arrays.equals(expected, verifyData.getBytes());
	}
	
	@Override
	public TlsVerifyData getVerifyDataToSend() {
		byte[] masterSecret = _securityParameters.getMasterSecret();
		byte[] bytes = _securityParameters.getFinishedVerifyDataForServer(masterSecret);
		
		return new TlsVerifyData(bytes);
	}

	@Override
	protected String getEntityName() {
		return "Server";
	}
}
