package jProtocol.Abstract.View;

public class JProtocolPresenter {

	private JProtocolView _view;
	
	public JProtocolPresenter() {
		_view = new JProtocolView();
		
		StateMachinePresenter clientPresenter = new StateMachinePresenter("Client");
		StateMachinePresenter serverPresenter = new StateMachinePresenter("Server");
		
		ProtocolDataUnitPresenter pduPresenter = new ProtocolDataUnitPresenter(); 
		
		_view.setClientStateMachineView(clientPresenter.getView());
		_view.setServerStateMachineView(serverPresenter.getView());
		_view.setProtocolDataUnitView(pduPresenter.getView());
	}

	public void showView() {
		_view.show();
	}
	
}
