package jProtocol;

import jProtocol.Abstract.JProtocolProtocolBuilder;
import jProtocol.tls12.Tls12Provider;
import jProtocol.tls12.model.TlsCiphertext;
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

	JFrame _frame;
	
	public static void main(String[] args) {
		new StartUp();
	}

	public StartUp() {
		try {
	      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
	    }
	    catch (Exception e) { }

		_frame = new JFrame("jProtocol");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setBackground(Color.WHITE);
		_frame.setMinimumSize(new Dimension(800, 600));
		addMenu(_frame);
		_frame.setVisible(true);
		
		//TODO: remove
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
		_frame.getContentPane().removeAll();
		_frame.revalidate();
		_frame.repaint();
		
		//TODO: Protocol loading
		Tls12Provider provider = new Tls12Provider();
		JProtocolProtocolBuilder<TlsCiphertext> builder = new JProtocolProtocolBuilder<>(provider, provider);
		
		_frame.getContentPane().add(builder.getView());
		_frame.revalidate();
		_frame.repaint();
	}
	
	private void exitClicked() {
		System.exit(0);
	}
	
	private void aboutClicked() {
		
	}
	
	private void aboutProtocolClicked() {
		
	}
}
