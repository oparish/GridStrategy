package assembler;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import ai.Action;
import ai.ActivateAction;
import ai.ColumnCondition;
import ai.Condition;
import ai.CreditCondition;
import ai.DeployAction;
import ai.GateCondition;
import ai.NoCondition;
import ai.Rule;

public class AddRuleDialog extends NewConditionDialog
{
	private AssemblerList<Class<? extends Action>> actionOptions;
	
	public AddRuleDialog(Assembler assembler)
	{
		super(assembler);
	}
	
	protected void setupContents()
	{
		this.setupConditionList(1);
		this.setupActionList();
		this.setupOKButton();
		this.setupCancelButton();
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		if (source == this.okButton)
		{
			Condition condition = Condition.getConditionExample((Class<? extends Condition>) this.conditionOptions.getSelectedValue());
			Action action = Action.getActionExample((Class<? extends Action>) this.actionOptions.getSelectedValue());
			Rule rule = new Rule(condition, action);
			this.assembler.addRule(rule);
			this.dispose();
		}
		else
		{
			this.dispose();
		}
		
	}
	
	private void setupActionList()
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridwidth = 1;
		this.actionOptions = new AssemblerList<Class<? extends Action>>(new Class[]{DeployAction.class, ActivateAction.class}, AssemblerListType.ACTIONCLASSES);
		this.actionOptions.setCellRenderer(new ActionOptionsCellRenderer());
		this.add(new JScrollPane(this.actionOptions), gridBagConstraints);
	}
	
	private class ActionOptionsCellRenderer implements ListCellRenderer<Class<? extends Action>>
	{
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Class<? extends Action>> arg0, Class<? extends Action> actionClass, int index, boolean selected,
				boolean arg4)
		{
			String text = Action.getActionClassName(actionClass);
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
	
}
