package assembler;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import ai.ColumnCondition;
import ai.Condition;
import ai.CreditCondition;
import ai.GateCondition;
import ai.NoCondition;
import ai.Rule;

public class NewConditionDialog extends JDialog implements ActionListener
{
	protected JButton okButton;
	protected JButton cancelButton;
	protected AssemblerList conditionOptions;
	protected Assembler assembler;
	
	NewConditionDialog(Assembler assembler)
	{
		super(assembler, true);
		this.setLayout(new GridBagLayout());
		this.setSize(500, 500);
		this.assembler = assembler;
		this.setupContents();
	}
	
	protected void setupContents()
	{
		this.setupConditionList(2);
		this.setupOKButton();
		this.setupCancelButton();
	}
	
	protected void setupOKButton()
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		this.okButton = new JButton("OK");
		this.okButton.addActionListener(this);
		this.add(this.okButton, gridBagConstraints);
	}
	
	protected void setupCancelButton()
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this);
		this.add(this.cancelButton, gridBagConstraints);
	}
	
	protected void setupConditionList(int width)
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridwidth = width;
		this.conditionOptions = new AssemblerList<Class<? extends Condition>>(new Class[]{ColumnCondition.class, GateCondition.class, CreditCondition.class, NoCondition.class}, AssemblerListType.CONDITIONCLASSES);
		this.conditionOptions.setCellRenderer(new ConditionOptionsCellRenderer());
		this.conditionOptions.setSelectedIndex(0);
		this.add(new JScrollPane(this.conditionOptions), gridBagConstraints);
	}
	
	private class ConditionOptionsCellRenderer implements ListCellRenderer<Class<? extends Condition>>
	{
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Class<? extends Condition>> arg0, Class<? extends Condition> conditionClass, int index, boolean selected,
				boolean arg4)
		{
			String text = Condition.getConditionClassName(conditionClass);
			if (selected)
			{
				return AssemblerList.getSelectedLabel(text);
			}
			else
			{
				return new JLabel(text);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		if (source == this.okButton)
		{
			Condition condition = Condition.getConditionExample((Class<? extends Condition>) this.conditionOptions.getSelectedValue());
			this.assembler.changeCondition(condition);
			this.dispose();
		}
		else
		{
			this.dispose();
		}
		
	}
}
