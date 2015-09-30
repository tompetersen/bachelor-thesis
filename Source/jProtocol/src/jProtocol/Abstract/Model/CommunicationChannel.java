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

	/**
	 * Sends a protocol data unit from sender. The current thread will be paused until 
	 * the user clicks the next message button if not sendAllMessagesWithoutBreak() has
	 * been called before. A channel can just deliver a single message at the same time.
	 * 
	 * @param pdu the protocol data unit
	 * @param sender the sending state machine
	 */
	public void sendMessage(T pdu, StateMachine<T> sender) {
		if (_pduToSend != null) {
			throw new RuntimeException("Channel is currently delivering message!");
		}
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

	/**
	 * Sends the next message, which has been set with sendMessage().
	 * Gets called from the UI thread. 
	 */
	public void sendNextMessage() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (_pduToSend != null) {
					T pdu = _pduToSend;
					_pduToSend = null;
					_sentPdus.add(pdu);
					
					if (pdu.hasBeenSentByClient()) {
						_client.notifyObserversOfStateChanged();
						_server.receiveMessage(pdu);
						_clientCountdownLatch.countDown();
					}
					else {
						_server.notifyObserversOfStateChanged();
						_client.receiveMessage(pdu);
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

	/**
	 * All messages will be sent without a paused thread, when this method has been called.
	 * Gets called from the UI thread.
	 */
	public void sendAllMessagesWithoutBreak() {
		_sendAllMessages = true;
		sendNextMessage();
	}

	/**
	 * Returns a list with all protocol data units which have been sent.
	 * 
	 * @return the protocol data unit list
	 */
	public List<T> getSentProtocolDataUnits() {
		return _sentPdus;
	}
	
	/**
	 * Returns the current protocol data unit which will be sent when sendNextMessage() gets called.
	 * 
	 * @return the current protocol data unit to be sent
	 */
	public T getPduToSend() {
		return _pduToSend;
	}
}
