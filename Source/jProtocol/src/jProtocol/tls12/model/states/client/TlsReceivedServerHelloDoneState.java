package jProtocol.tls12.model.states.client;

import jProtocol.helper.ByteHelper;
import jProtocol.tls12.model.crypto.TlsPseudoRandomNumberGenerator;
import jProtocol.tls12.model.exceptions.TlsAsymmetricOperationException;
import jProtocol.tls12.model.messages.TlsChangeCipherSpecMessage;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage_DHE;
import jProtocol.tls12.model.messages.handshake.TlsClientKeyExchangeMessage_RSA;
import jProtocol.tls12.model.messages.handshake.TlsFinishedMessage;
import jProtocol.tls12.model.states.TlsClientStateMachine;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsClientDhPublicKey;
import jProtocol.tls12.model.values.TlsKeyExchangeAlgorithm;
import jProtocol.tls12.model.values.TlsRsaEncryptedPreMasterSecret;
import jProtocol.tls12.model.values.TlsVerifyData;
import jProtocol.tls12.model.values.TlsVersion;

public class TlsReceivedServerHelloDoneState extends TlsState {

	public TlsReceivedServerHelloDoneState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return false;
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		throw new RuntimeException("Client should not receive messages in ReceivedServerHelloDoneState!");
	}

	@Override
	public void onEnter() {
		super.onEnter();
		
		setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_CHANGE_CIPHER_SPEC_STATE);
		
		sendClientKeyExchangeMessage();
		sendChangeCipherSpecMessage();
		
		_stateMachine.changeWriteStateToPendingState();
		
		sendFinishedMessage();
	}
	
	private void sendClientKeyExchangeMessage() {
		TlsKeyExchangeAlgorithm algorithm = _stateMachine.getPendingCipherSuite().getKeyExchangeAlgorithm();
		
		if (algorithm == TlsKeyExchangeAlgorithm.rsa) {
			sendRsaClientKeyExchangeMessage();
		}
		else if (algorithm == TlsKeyExchangeAlgorithm.dhe_rsa) {
			sendDhClientKeyExchangeMessage();
		}
		else {
			throw new RuntimeException("Key exchange algorithm [" + algorithm.toString() + "] not implemented yet!");
		}
	}
	
	private void sendRsaClientKeyExchangeMessage() {
		try {
			TlsVersion version = _stateMachine.getVersion(); 
			byte[] premastersecret = ByteHelper.concatenate(version.getBytes(), TlsPseudoRandomNumberGenerator.nextBytes(46));
			_stateMachine.computeMasterSecret(premastersecret);
			
			TlsClientStateMachine clientStateMachine = (TlsClientStateMachine) _stateMachine;
			byte[] encryptedPreMasterSecretBytes = clientStateMachine.rsaEncrypt(premastersecret);
			TlsRsaEncryptedPreMasterSecret encPreMasterSecret = new TlsRsaEncryptedPreMasterSecret(encryptedPreMasterSecretBytes);
			TlsClientKeyExchangeMessage_RSA message = new TlsClientKeyExchangeMessage_RSA(encPreMasterSecret);
			
			_stateMachine.addHandshakeMessageForVerifyData(message);
			
			sendTlsMessage(message);
		}
		catch (TlsAsymmetricOperationException e) {
			setTlsState(TlsStateType.DECRYPT_ERROR_OCCURED_STATE);
		}
	}
	
	private void sendDhClientKeyExchangeMessage() {
		TlsClientStateMachine clientStateMachine = (TlsClientStateMachine) _stateMachine;
		TlsClientDhPublicKey clientDhPublicKey = clientStateMachine.getClientDhPublicKey();
		TlsClientKeyExchangeMessage_DHE message = new TlsClientKeyExchangeMessage_DHE(clientDhPublicKey);
		
		_stateMachine.addHandshakeMessageForVerifyData(message);
		
		sendTlsMessage(message);
	}

	private void sendChangeCipherSpecMessage() {
		TlsMessage message = new TlsChangeCipherSpecMessage();
		sendTlsMessage(message);
	}
	
	private void sendFinishedMessage() {
		TlsVerifyData verifyData = _stateMachine.getVerifyDataToSend();
		TlsFinishedMessage message = new TlsFinishedMessage(verifyData);
		sendTlsMessage(message);
	}
}
