package jProtocol.Abstract.Model;

import jProtocol.Abstract.Model.events.ChannelReceivedMessageEvent;
import jProtocol.helper.MyLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;

public class CommunicationChannel<T extends ProtocolDataUnit> extends Observable {

	// TODO: circular reference problem!

	private StateMachine<T> _client;
	private StateMachine<T> _server;

	private T _pduToSent;
	private CountDownLatch _clientCountdownLatch;
	private CountDownLatch _serverCountdownLatch;

	private List<T> _sentPdus;

	/**
	 * Creates a communication channel between client and server.
	 * 
	 * @param client
	 *            the client state machine
	 * @param server
	 *            the server state machine
	 */
	public CommunicationChannel(StateMachine<T> client, StateMachine<T> server) {
		_sentPdus = new ArrayList<>();

		if (client == null || server == null) {
			throw new IllegalArgumentException("Client and server must not be null!");
		}

		_client = client;
		_server = server;
	}

	public void sendMessage(T pdu, StateMachine<T> sender) {
		boolean clientMessage = (sender == _client);
		pdu.setSentByClient(clientMessage);

		_client.notifyObserversOfStateChanged();
		_server.notifyObserversOfStateChanged();

		_pduToSent = pdu;
		if (clientMessage) {
			_clientCountdownLatch = new CountDownLatch(1);
		}
		else {
			_serverCountdownLatch = new CountDownLatch(1);
		}
		
		try {
			if (clientMessage) {
				_clientCountdownLatch.await();
			}
			else {
				_serverCountdownLatch.await();
			}
		}
		catch (InterruptedException e) {
			throw new RuntimeException("CountdownLatch interrupted unexpectedly!");
		}
	}

	public void sendNextMessage() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (_pduToSent != null) {
					_sentPdus.add(_pduToSent);
					setChanged();
					notifyObservers(new ChannelReceivedMessageEvent());
					
					if (_pduToSent.hasBeenSentByClient()) {
						_server.receiveMessage(_pduToSent);
						_pduToSent = null;
						_clientCountdownLatch.countDown();
					}
					else {
						_client.receiveMessage(_pduToSent);
						_pduToSent = null;
						_serverCountdownLatch.countDown();
					}
				}
			}
		};
		new Thread(r).start();
	}

	public void sendAllMessagesWithoutBreak() {
		// TODO:
	}

	public List<T> getSentProtocolDataUnits() {
		return _sentPdus;
	}
}
