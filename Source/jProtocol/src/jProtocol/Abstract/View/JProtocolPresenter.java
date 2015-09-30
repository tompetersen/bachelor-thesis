package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import javax.swing.JComponent;

public class JProtocolPresenter<T extends ProtocolDataUnit> {

	private JProtocolView _view;
	
	/**
	 * Creates a complete protocol presenter.
	 * 
	 * @param provider the provider for client, server and protocol data unit detail views
	 * @param channel the communication channel
	 * @param htmlInfoUpdater an info updater
	 */
	public JProtocolPresenter(JProtocolViewProvider<T> provider, CommunicationChannel<T> channel, DefaultHtmlInfoUpdater htmlInfoUpdater) {
		StateMachinePresenter clientPresenter = new StateMachinePresenter(provider.getViewForClientStateMachine(htmlInfoUpdater));
		StateMachinePresenter serverPresenter = new StateMachinePresenter(provider.getViewForServerStateMachine(htmlInfoUpdater));
		
		ProtocolDataUnitPresenter<T> pduPresenter = new ProtocolDataUnitPresenter<T>(provider, channel, htmlInfoUpdater); 
		CommunicationChannelPresenter<T> ccPresenter = new CommunicationChannelPresenter<T>(channel, htmlInfoUpdater.getHtmlInfoView());
		
		_view = new JProtocolView(clientPresenter.getView(), serverPresenter.getView(), pduPresenter.getView(), ccPresenter.getView());
	}

	public JComponent getView() {
		return _view.getView();
	}
	
}
