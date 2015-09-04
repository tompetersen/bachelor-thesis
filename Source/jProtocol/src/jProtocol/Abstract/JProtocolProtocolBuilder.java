package jProtocol.Abstract;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.Event;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.Abstract.View.JProtocolPresenter;
import java.util.Observable;
import java.util.Observer;

public class JProtocolProtocolBuilder<T extends ProtocolDataUnit> implements Observer {

	private JProtocolViewProvider<T> _provider;
	private StateMachine<T> _client;
	private StateMachine<T> _server;

	public JProtocolProtocolBuilder(StateMachine<T> client, StateMachine<T> server, JProtocolViewProvider<T> viewProvider) {
		_provider = viewProvider;
		_client = client;
		_server = server;

		CommunicationChannel<T> channel = new CommunicationChannel<>(client, server);

		client.setCommunicationChannel(channel);
		server.setCommunicationChannel(channel);
		client.addObserver(this);
		server.addObserver(this);

		JProtocolPresenter<T> presenter = new JProtocolPresenter<>(viewProvider, channel);
		presenter.showView();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Event) {
			if (o == _client) {
				_provider.updateClientView();
			}
			else if (o == _server) {
				_provider.updateServerView();
			}
		}
	}

}
