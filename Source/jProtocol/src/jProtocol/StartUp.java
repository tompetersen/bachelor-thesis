package jProtocol;

import jProtocol.Abstract.PluginChooser;
import jProtocol.Abstract.ProtocolBuilder;
import jProtocol.Abstract.ProtocolRegistry;
import jProtocol.Abstract.Model.ProtocolDataUnit;
import jProtocol.Abstract.View.HtmlInfoView;
import jProtocol.Abstract.View.UiConstants;
import jProtocol.Abstract.View.resources.HtmlAboutLoader;
import jProtocol.Abstract.View.resources.ImageLoader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class StartUp {

	private JFrame _frame;
	private HtmlInfoView _aboutView;
	private ProtocolBuilder<? extends ProtocolDataUnit> _currentBuilder;

	public static void main(String[] args) {
		new StartUp();
	}

	public StartUp() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
		}
		
		_aboutView = new HtmlInfoView();

		_frame = new JFrame(UiConstants.APPLICATION_NAME);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setBackground(Color.WHITE);
		_frame.setMinimumSize(new Dimension(800, 600));
		_frame.setIconImages(ImageLoader.getIconFileList());
		addMenu(_frame);
		_frame.setVisible(true);

		startProtocolClicked();
	}

	private void addMenu(JFrame frame) {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		JMenuItem menuItem = new JMenuItem("Start Protocol...");
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startProtocolClicked();
			}
		});
		fileMenu.add(menuItem);

		fileMenu.addSeparator();

		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitClicked();
			}
		});
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("About protocol");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutProtocolClicked();
			}
		});
		helpMenu.add(menuItem);

		helpMenu.addSeparator();

		menuItem = new JMenuItem("About");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutClicked();
			}
		});
		helpMenu.add(menuItem);

		frame.setJMenuBar(menuBar);
	}

	private void startProtocolClicked() {
		_currentBuilder = getProtocolFromPluginChooser();

		if (_currentBuilder != null) {
			_frame.getContentPane().removeAll();
			_frame.revalidate();
			_frame.repaint();
			
			_frame.getContentPane().add(_currentBuilder.getView());
			_frame.revalidate();
			_frame.repaint();
		}
	}

	private ProtocolBuilder<? extends ProtocolDataUnit> getProtocolFromPluginChooser() {
		//TODO: as fields?
		ProtocolRegistry registry = new ProtocolRegistry();
		PluginChooser pluginChooser = new PluginChooser(registry, _frame);

		pluginChooser.showChooser();

		return pluginChooser.getSelectedProtocol();
	}

	private void exitClicked() {
		System.exit(0);
	}

	private void aboutClicked() {
		_aboutView.setHtmlContent(HtmlAboutLoader.getHtmlAboutContent());
		_aboutView.show(_currentBuilder.getView());
	}

	private void aboutProtocolClicked() {
		if (_currentBuilder != null) {
			_aboutView.setHtmlContent(_currentBuilder.getHtmlAboutContent());
			_aboutView.show(_currentBuilder.getView());
		}
	}
}
