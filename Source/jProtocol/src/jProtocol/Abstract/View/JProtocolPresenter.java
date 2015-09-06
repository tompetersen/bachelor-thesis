package jProtocol.Abstract.View;

import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.CommunicationChannel;
import jProtocol.Abstract.Model.ProtocolDataUnit;

public class JProtocolPresenter<T extends ProtocolDataUnit> {

	private JProtocolView _view;
	
	public JProtocolPresenter(JProtocolViewProvider<T> provider, CommunicationChannel<T> channel) {
		StateMachinePresenter clientPresenter = new StateMachinePresenter("Client", provider.getViewForClientStateMachine());
		StateMachinePresenter serverPresenter = new StateMachinePresenter("Server", provider.getViewForServerStateMachine());
		
		ProtocolDataUnitPresenter<T> pduPresenter = new ProtocolDataUnitPresenter<T>(provider, channel); 
		
		_view = new JProtocolView(clientPresenter.getView(), serverPresenter.getView(), pduPresenter.getView());
	}

	public void showView() {
		
		_view.show();
	}
	
}
