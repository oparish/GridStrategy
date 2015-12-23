package assembler;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import ai.Action;
import ai.ActionType;
import ai.ActivateAction;
import ai.ColumnCondition;
import ai.Condition;
import ai.CreditCondition;
import ai.DeployAction;
import ai.FurtherInputActivateAction;
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
			ArrayList<Action> actionList = new ArrayList<Action>();
			actionList.add(action);
			Rule rule = new Rule(condition, actionList);
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
		this.add(new JScrollPane(this.actionOptions), gridBagConstraints);
	}
	
}
