package jProtocol.echoprotocol;

import jProtocol.Abstract.JProtocolStateMachineProvider;
import jProtocol.Abstract.JProtocolViewProvider;
import jProtocol.Abstract.Model.StateMachine;
import jProtocol.Abstract.View.HtmlInfoUpdater;
import jProtocol.echoprotocol.model.EchoClient;
import jProtocol.echoprotocol.model.EchoProtocolDataUnit;
import jProtocol.echoprotocol.model.EchoServer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EchoProvider implements JProtocolViewProvider<EchoProtocolDataUnit>, JProtocolStateMachineProvider<EchoProtocolDataUnit> {

	@Override
	public StateMachine<EchoProtocolDataUnit> getServerStateMachine() {
		return new EchoServer();
	}

	@Override
	public StateMachine<EchoProtocolDataUnit> getClientStateMachine() {
		return new EchoClient();
	}

	@Override
	public JComponent getDetailedViewForProtocolDataUnit(EchoProtocolDataUnit pdu, HtmlInfoUpdater htmlInfoUpdater) {
		return new JLabel(pdu.getPayload());
	}

	@Override
	public JComponent getViewForClientStateMachine(HtmlInfoUpdater htmlInfoUpdater) {
		return new JPanel();
	}

	@Override
	public JComponent getViewForServerStateMachine(HtmlInfoUpdater htmlInfoUpdater) {
		return new JPanel();
	}

	@Override
	public void updateServerView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClientView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JComponent getSettingsView() {
		// TODO Auto-generated method stub
		return new JPanel();
	}

}
