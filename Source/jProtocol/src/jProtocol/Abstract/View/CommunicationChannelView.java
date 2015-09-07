package jProtocol.Abstract.View;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class CommunicationChannelView {

	private JPanel _view;
	private JButton _nextStepButton;
	private JButton _nextStepsButton;
	
	public interface CommunicationChannelViewListener {
		public void nextStepClicked();
		public void nextStepsClicked();
	}
	
	public CommunicationChannelView(final CommunicationChannelViewListener listener) {
		_view = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		_nextStepButton = new JButton(">");
		_nextStepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				_nextStepButton.setEnabled(false);
//				_nextStepsButton.setEnabled(false);
				listener.nextStepClicked();
			}
		});
		
		_nextStepsButton = new JButton(">>");
		_nextStepsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				_nextStepButton.setEnabled(false);
//				_nextStepsButton.setEnabled(false);
				listener.nextStepsClicked();
			}
		});
		
		_view.add(_nextStepButton);
		_view.add(_nextStepsButton);
	}

	public JComponent getView() {
		return _view;
	}
	
	public void enableNextStepView() {
		_nextStepButton.setEnabled(true);
		_nextStepsButton.setEnabled(true);
	}
}
