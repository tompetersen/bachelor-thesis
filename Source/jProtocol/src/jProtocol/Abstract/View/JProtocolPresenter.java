package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;

public class JProtocolPresenter<T extends ProtocolDataUnit> {

	private JProtocolView _view;
	
	public JProtocolPresenter(JProtocolViewProvider<T> provider, CommunicationChannel<T> channel, DefaultHtmlInfoUpdater htmlInfoUpdater) {
		StateMachinePresenter clientPresenter = new StateMachinePresenter(provider.getViewForClientStateMachine(htmlInfoUpdater));
		StateMachinePresenter serverPresenter = new StateMachinePresenter(provider.getViewForServerStateMachine(htmlInfoUpdater));
		
		ProtocolDataUnitPresenter<T> pduPresenter = new ProtocolDataUnitPresenter<T>(provider, channel, htmlInfoUpdater); 
		CommunicationChannelPresenter<T> ccPresenter = new CommunicationChannelPresenter<T>(channel, htmlInfoUpdater.getHtmlInfoView());
		
		_view = new JProtocolView(clientPresenter.getView(), serverPresenter.getView(), pduPresenter.getView(), ccPresenter.getView());
	}

	public void showView() {
		_view.show();
	}
	
}
