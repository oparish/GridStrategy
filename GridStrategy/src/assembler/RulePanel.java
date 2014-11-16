package assembler;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import ai.Action;
import ai.ActivateAction;
import ai.DeployAction;
import ai.Rule;

public class RulePanel extends JPanel
{
	private Rule rule;
	private ConditionPanel conditionPanel;
	private ActionPanel actionPanel;
	
	public RulePanel(ChangeListener listener)
	{
		super();
		this.setLayout(new GridLayout(2,1));
		this.conditionPanel = new ConditionPanel(listener);
		this.add(this.conditionPanel);
		this.actionPanel = new ActionPanel(listener);
		this.add(this.actionPanel);
	}
	
	public void changeSelectedRule(Rule rule)
	{
		this.rule = rule;
		this.conditionPanel.changeCondition(rule.getCondition());
		DeployAction action = (DeployAction) rule.getAction();
		this.actionPanel.changeAction(action);
		this.actionPanel.changePosition(action.getColumnPos());
		this.actionPanel.changeUnitTypeBox(action.getUnitType());
		if (action instanceof ActivateAction)
		{
			ActivateAction activateAction = (ActivateAction) action;
			this.actionPanel.changeConditionBox(activateAction.getColumnSearchCondition());
		}
		else
		{
			this.actionPanel.disablePositionBox();
		}
	}
}
