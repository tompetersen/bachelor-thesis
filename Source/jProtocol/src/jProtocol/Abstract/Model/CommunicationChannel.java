package jProtocol.Abstract.Model;

import jProtocol.Abstract.Model.events.ChannelReceivedMessageEvent;
import jProtocol.Abstract.Model.events.ChannelSentMessageEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;

public class CommunicationChannel<T extends ProtocolDataUnit> extends Observable {

	private StateMachine<T> _client;
	private StateMachine<T> _server;

	private T _pduToSend;
	private CountDownLatch _clientCountdownLatch;
	private CountDownLatch _serverCountdownLatch;
	private boolean _sendAllMessages;

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
		_sendAllMessages = false;

		if (client == null || server == null) {
			throw new IllegalArgumentException("Client and server must not be null!");
		}

		_client = client;
		_server = server;
	}

	public void sendMessage(T pdu, StateMachine<T> sender) {
		boolean clientMessage = (sender == _client);
		pdu.setSentByClient(clientMessage);

		_pduToSend = pdu;
		
		setChanged();
		notifyObservers(new ChannelReceivedMessageEvent());
		
		if (_sendAllMessages) {
			sendNextMessage();
		}
		
		try {
			if (clientMessage) {
				_clientCountdownLatch = new CountDownLatch(1);
				_clientCountdownLatch.await();
			}
			else {
				_serverCountdownLatch = new CountDownLatch(1);
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
				if (_pduToSend != null) {
					_sentPdus.add(_pduToSend);
					
					if (_pduToSend.hasBeenSentByClient()) {
						//clear state
						_client.notifyObserversOfStateChanged();
						_server.receiveMessage(_pduToSend);
						_pduToSend = null;
						_clientCountdownLatch.countDown();
					}
					else {
						//clear state
						_server.notifyObserversOfStateChanged();
						_client.receiveMessage(_pduToSend);
						_pduToSend = null;
						_serverCountdownLatch.countDown();
					}
					
					setChanged();
					notifyObservers(new ChannelSentMessageEvent());
				}
			}
		};
		
		try {
			Thread.sleep(200);
		}
		catch (InterruptedException e) { }
		
		new Thread(r).start();
	}

	public void sendAllMessagesWithoutBreak() {
		_sendAllMessages = true;
		sendNextMessage();
	}

	public List<T> getSentProtocolDataUnits() {
		return _sentPdus;
	}
	
	public T getPduToSend() {
		return _pduToSend;
	}
}
