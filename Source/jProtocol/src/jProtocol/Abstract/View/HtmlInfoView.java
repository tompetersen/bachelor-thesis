package jProtocol.Abstract.View;

import jProtocol.Abstract.View.resources.ImageLoader;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class HtmlInfoView {

	private JFrame _frame;
	private JEditorPane _editorPane;
	private JScrollPane _scrollPane;

	/**
	 * Creates a HTML info view.
	 */
	public HtmlInfoView() {
		_editorPane = new JEditorPane();
		_editorPane.setEditable(false);
		_scrollPane = new JScrollPane(_editorPane);

		//open included links in system browser
		_editorPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(event.getURL().toURI());
						}
						catch (IOException | URISyntaxException e) {
							JOptionPane.showMessageDialog(_editorPane, "Couldn't open link: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		HTMLEditorKit kit = new HTMLEditorKit();
		_editorPane.setEditorKit(kit);

		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("body {color:#000; font-family:times; margin: 5px; }");
		styleSheet.addRule("h1 {margin-top:0px;}");
		styleSheet.addRule("p {margin-top: 3px; margin-bottom: 3px;}");
		styleSheet.addRule("pre {padding: 5px; color: #FFFFFF; background-color: #555555; border: 1px solid #CCCCCC; line-height: 70%;}");

		styleSheet.addRule(".citation {padding: 5px; margin-top: 5px; margin-bottom: 5px; border:1px solid #CCCCCC; background-color: #EEEEEE; }");
		styleSheet.addRule(".source {font-style: italic; text-align: right; margin-top: 5px;}");
		styleSheet.addRule(".seealso {color: blue;}");

		Document doc = kit.createDefaultDocument();
		_editorPane.setDocument(doc);

		_frame = new JFrame("Info");
		_frame.getContentPane().add(_scrollPane, BorderLayout.CENTER);
		_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		_frame.setAlwaysOnTop(true);
		_frame.setSize(new Dimension(400, 400));
		_frame.setIconImages(ImageLoader.getIconFileList());
	}

	/**
	 * Sets the info view HTML body content. A default header will be added.
	 * 
	 * @param htmlBody the HTML body
	 */
	public void setHtmlBodyContent(String htmlBody) {
		String html = "<html>\n" + "<head></head>\n" + "<body>\n" + htmlBody + "</body>\n" + "</html>\n";
		_editorPane.setText(html);
	}

	/**
	 * Sets the info view HTML content. HTML header must be added.
	 * 
	 * @param htmlContent the html content
	 */
	public void setHtmlContent(String htmlContent) {
		_editorPane.setText(htmlContent);

		// Doesn't work?!
		JScrollBar scrollBar = _scrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMinimum());
	}

	/**
	 * Makes the info view visible.
	 * 
	 * @param relativeToComponent the Component the info view should be placed relative to
	 */
	public void show(JComponent relativeToComponent) {
		if (!_frame.isVisible()) {
			_frame.setLocationRelativeTo(relativeToComponent);
			_frame.setVisible(true);
		}
	}

}
