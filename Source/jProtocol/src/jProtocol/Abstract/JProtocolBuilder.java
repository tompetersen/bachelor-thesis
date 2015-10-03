package jProtocol.Abstract;

import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.Abstract.Model.events.StateMachineStateChangedEvent;
import jProtocol.Abstract.View.DefaultHtmlInfoUpdater;
import jProtocol.Abstract.View.JProtocolPresenter;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;

public class JProtocolBuilder<T extends ProtocolDataUnit> implements Observer {

	private JProtocolViewProvider<T> _provider;
	private StateMachine<T> _client;
	private StateMachine<T> _server;

	private JProtocolPresenter<T> _presenter;

	/**
	 * Creates a new protocol instance.
	 * 
	 * @param stateMachineProvider
	 *            a state machine provider for the protocol
	 * @param viewProvider
	 *            a view provider for the protocol
	 */
	public JProtocolBuilder(JProtocolStateMachineProvider<T> stateMachineProvider, JProtocolViewProvider<T> viewProvider) {
		_provider = viewProvider;
		_client = stateMachineProvider.getClientStateMachine();
		_server = stateMachineProvider.getServerStateMachine();

		CommunicationChannel<T> channel = new CommunicationChannel<>(_client, _server);

		_client.setCommunicationChannel(channel);
		_server.setCommunicationChannel(channel);
		_client.addObserver(this);
		_server.addObserver(this);

		DefaultHtmlInfoUpdater htmlInfoUpdater = new DefaultHtmlInfoUpdater();

		_presenter = new JProtocolPresenter<>(viewProvider, channel, htmlInfoUpdater);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof StateMachineStateChangedEvent) {
			if (o == _client) {
				_provider.updateClientView();
			}
			else if (o == _server) {
				_provider.updateServerView();
			}
		}
	}

	/**
	 * Returns the view for the protocol. Views for server, client and protocol
	 * data units are included and handled internally.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _presenter.getView();
	}
	
	/**
	 * Returns the settings view for the protocol.
	 * 
	 * @return the settings view
	 */
	public JComponent getSettingsView() {
		return _provider.getSettingsView();
	}
}
