package jProtocol.tls12;

import jProtocol.Abstract.StateMachineProvider;
import jProtocol.Abstract.ViewProvider;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.Abstract.View.keyvaluetree.KeyValueTree;
import jProtocol.tls12.model.TlsCiphertext;
import jProtocol.tls12.model.ciphersuites.TlsCipherSuiteRegistry;
import jProtocol.tls12.model.states.TlsClientStateMachine;
import jProtocol.tls12.model.states.TlsServerStateMachine;
import jProtocol.tls12.model.states.TlsStateMachine;
import jProtocol.tls12.view.TlsClientView;
import jProtocol.tls12.view.TlsServerView;
import jProtocol.tls12.view.TlsSettingsView;
import javax.swing.JComponent;

public class Tls12Provider implements ViewProvider<TlsCiphertext>, StateMachineProvider<TlsCiphertext> {
	
	private TlsStateMachine _client;
	private TlsStateMachine _server;
	private TlsCipherSuiteRegistry _registry;
	
	private TlsClientView _clientView;
	private TlsServerView _serverView;

	public Tls12Provider() {
		_registry = new TlsCipherSuiteRegistry();
		
		_client = new TlsClientStateMachine(_registry);
		_server = new TlsServerStateMachine(_registry);
	}

	@Override
	public StateMachine<TlsCiphertext> getServerStateMachine() {
		return _server;
	}

	@Override
	public StateMachine<TlsCiphertext> getClientStateMachine() {
		return _client;
	}

	@Override
	public JComponent getDetailedViewForProtocolDataUnit(TlsCiphertext pdu, HtmlInfoUpdater htmlInfoUpdater) {
		KeyValueTree tree = new KeyValueTree("TlsCiphertext", htmlInfoUpdater, false);
		tree.updateKeyValueObjectList(pdu.getViewData());
		tree.expandAll();
		
		return tree.getView();
	}

	@Override
	public JComponent getViewForClientStateMachine(HtmlInfoUpdater htmlInfoUpdater) {
		_clientView = new TlsClientView(_client, htmlInfoUpdater);
		return _clientView.getView();
	}

	@Override
	public JComponent getViewForServerStateMachine(HtmlInfoUpdater htmlInfoUpdater) {
		_serverView = new TlsServerView(_server, htmlInfoUpdater);
		return _serverView.getView();
	}

	@Override
	public void updateServerView() {
		_serverView.updateView();
	}

	@Override
	public void updateClientView() {
		_clientView.updateView();
	}

	@Override
	public JComponent getSettingsView() {
		TlsSettingsView view = new TlsSettingsView(_registry);
		return view.getView();
	}
}
