package assembler;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import ai.Action;
import ai.ActionType;

public class AddActionDialog extends JDialog implements ActionListener
{
	private AssemblerList<Class<? extends Action>> actionOptions;
	private Assembler assembler;
	
	public AddActionDialog(Assembler assembler)
	{
		super();
		this.assembler = assembler;
		this.setupActionList();
		this.setupOKButton();
		this.setSize(500, 500);
	}
	
	private void setupActionList()
	{
		Class<? extends Action>[] actionTypeList = new Class[ActionType.values().length];
		int i = 0;
		for (ActionType actionType : ActionType.values())
		{
			actionTypeList[i] = actionType.getActionClass();
			i++;
		}
		
		this.actionOptions = new AssemblerList<Class<? extends Action>>(actionTypeList, AssemblerListType.ACTIONCLASSES);
		this.actionOptions.setCellRenderer(new ActionOptionsCellRenderer());
		this.actionOptions.setSelectedIndex(0);
		this.add(new JScrollPane(this.actionOptions), BorderLayout.CENTER);
	}
	
	private void setupOKButton()
	{
		JButton okButton = new JButton("OK");
		okButton.addActionListener(this);
		this.add(okButton, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		this.assembler.addNewActionToList(this.actionOptions.getSelectedValue());
	}
}
