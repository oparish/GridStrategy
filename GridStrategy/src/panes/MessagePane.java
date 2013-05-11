package panes;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MessagePane extends JScrollPane
{
	private JTextArea textArea;
	
	public MessagePane()
	{
		super();
		this.textArea = new JTextArea();
		this.add(this.textArea);
	}
}
