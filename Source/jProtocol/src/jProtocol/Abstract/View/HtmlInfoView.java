package jProtocol.Abstract.View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class HtmlInfoView {

	private JFrame _frame;
	private JEditorPane _editorPane;
	private JScrollPane _scrollPane;
	
	public HtmlInfoView() {
        _editorPane = new JEditorPane();
        _editorPane.setEditable(false);
        _scrollPane = new JScrollPane(_editorPane);
        
        HTMLEditorKit kit = new HTMLEditorKit();
        _editorPane.setEditorKit(kit);
        
        StyleSheet styleSheet = kit.getStyleSheet();
        //TODO: styling the html info view
        styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
        styleSheet.addRule("h1 {color: blue;}");
        styleSheet.addRule("h2 {color: #ff0000;}");
        styleSheet.addRule("pre {padding: 5px; background-color: #EEEEEE; line-height: 70%;}");

        Document doc = kit.createDefaultDocument();
        _editorPane.setDocument(doc);

        _frame = new JFrame("Help");
        _frame.getContentPane().add(_scrollPane, BorderLayout.CENTER);
        _frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        _frame.setAlwaysOnTop(true);
        _frame.setSize(new Dimension(300,200));
        _frame.setLocationRelativeTo(null);
	}
	
	public void setHtmlBodyContent(String htmlBody) {
		String html = "<html>\n"
        		+ "<head></head>\n"
                + "<body>\n"
                + htmlBody
                + "</body>\n"
                + "</html>\n";
		_editorPane.setText(html);
	}
	
	public void setHtmlContent(String htmlContent) {
		_editorPane.setText(htmlContent);
		
		//Doesn't work?!
		JScrollBar scrollBar = _scrollPane.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMinimum());
	}
	
	public void show() {
		_frame.setVisible(true);
	}

}
