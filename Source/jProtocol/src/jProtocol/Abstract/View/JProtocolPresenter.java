package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;

public class JProtocolPresenter<T extends ProtocolDataUnit> {

	private JProtocolView _view;
	
	public JProtocolPresenter(JProtocolViewProvider<T> provider, CommunicationChannel<T> channel) {
		StateMachinePresenter clientPresenter = new StateMachinePresenter(provider.getViewForClientStateMachine());
		StateMachinePresenter serverPresenter = new StateMachinePresenter(provider.getViewForServerStateMachine());
		
		ProtocolDataUnitPresenter<T> pduPresenter = new ProtocolDataUnitPresenter<T>(provider, channel); 
		CommunicationChannelPresenter<T> ccPresenter = new CommunicationChannelPresenter<T>(channel);
		
		_view = new JProtocolView(clientPresenter.getView(), serverPresenter.getView(), pduPresenter.getView(), ccPresenter.getView());
	}

	public void showView() {
		_view.show();
	}
	
}
