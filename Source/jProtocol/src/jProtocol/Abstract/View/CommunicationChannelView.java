package jProtocol.Abstract.View;

import jProtocol.Abstract.View.images.ImageLoader;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
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
		public void showHelpClicked();
	}
	
	/**
	 * Creates a communication channel view.
	 * 
	 * @param listener the listener for the view handling button clicks
	 */
	public CommunicationChannelView(final CommunicationChannelViewListener listener) {
		_view = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		final int buttonImageSize = UiConstants.BUTTON_IMAGE_SIZE;
		
		_nextStepButton = new JButton("Next message", new ImageIcon(ImageLoader.getNextIcon(buttonImageSize, buttonImageSize)));
		_nextStepButton.setHorizontalTextPosition(JButton.RIGHT);
		_nextStepButton.setVerticalTextPosition(JButton.CENTER);
		_nextStepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_nextStepButton.setEnabled(false);
				_nextStepsButton.setEnabled(false);
				listener.nextStepClicked();
			}
		});
		
		_nextStepsButton = new JButton("All messages", new ImageIcon(ImageLoader.getNextNextIcon(buttonImageSize, buttonImageSize)));
		_nextStepsButton.setHorizontalTextPosition(JButton.RIGHT);
		_nextStepsButton.setVerticalTextPosition(JButton.CENTER);
		_nextStepsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_nextStepButton.setEnabled(false);
				_nextStepsButton.setEnabled(false);
				listener.nextStepsClicked();
			}
		});
		
		JButton helpButton = new JButton("Show info", new ImageIcon(ImageLoader.getInfoIcon(buttonImageSize, buttonImageSize)));
		helpButton.setHorizontalTextPosition(JButton.RIGHT);
		helpButton.setVerticalTextPosition(JButton.CENTER);
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.showHelpClicked();
			}
		});
		
		_view.add(helpButton);
		_view.add(_nextStepButton);
		_view.add(_nextStepsButton);
		
		disableNextStepView();
	}

	/**
	 * Returns the channel view.
	 * 
	 * @return the view
	 */
	public JComponent getView() {
		return _view;
	}
	
	/**
	 * Enables the send next message buttons.
	 */
	public void enableNextStepView() {
		_nextStepButton.setEnabled(true);
		_nextStepsButton.setEnabled(true);
	}
	
	private void disableNextStepView() {
		_nextStepButton.setEnabled(false);
		_nextStepsButton.setEnabled(false);
	}
}
