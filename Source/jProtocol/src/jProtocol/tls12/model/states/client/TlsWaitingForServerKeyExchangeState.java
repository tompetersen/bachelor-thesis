package jProtocol.tls12.model.states.client;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import jProtocol.helper.MyLogger;
import jProtocol.tls12.model.messages.TlsMessage;
import jProtocol.tls12.model.messages.handshake.TlsServerKeyExchangeMessage;
import jProtocol.tls12.model.states.TlsState;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.model.states.TlsStateType;
import jProtocol.tls12.model.values.TlsHandshakeType;
import jProtocol.tls12.model.values.TlsServerDhParams;

public class TlsWaitingForServerKeyExchangeState extends TlsState {

	public TlsWaitingForServerKeyExchangeState(TlsStateMachine stateMachine) {
		super(stateMachine);
	}

	@Override
	public boolean expectedTlsMessage(TlsMessage message) {
		return isHandshakeMessageOfType(message, TlsHandshakeType.server_key_exchange);
	}

	@Override
	public void receivedTlsMessage(TlsMessage message) {
		MyLogger.info("Received Server Key exchange!");
		
		TlsServerKeyExchangeMessage keyExchangeMessage = (TlsServerKeyExchangeMessage)message;
		_stateMachine.addHandshakeMessageForVerifyData(keyExchangeMessage);
		
		TlsServerDhParams serverParams = keyExchangeMessage.getDhParams();
		byte[] signedParams = keyExchangeMessage.getSignedParams();
		
		//TODO: compute and store premastersecret
		//TODO: check signed params with server public key
		
		try {
			_stateMachine.createClientDhKeyAgreementFromServerValues(serverParams);
		}
		catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		setTlsState(TlsStateType.CLIENT_IS_WAITING_FOR_SERVER_HELLO_DONE_STATE);
	}

}
