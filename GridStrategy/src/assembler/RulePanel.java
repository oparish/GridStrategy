package assembler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import data.UnitType;
import ai.Action;
import ai.ActivateAction;
import ai.ColumnSearchCondition;
import ai.DeployAction;
import ai.Rule;

public class RulePanel extends JPanel
{
	private Rule rule;
	private ConditionPanel conditionPanel;
	private ActionPanel actionPanel;
	private boolean changingRule;
	
	public RulePanel(Assembler assembler)
	{
		super();
		this.setLayout(new GridLayout(2,1));
		this.conditionPanel = new ConditionPanel(assembler);
		this.add(this.conditionPanel);
		this.actionPanel = new ActionPanel(assembler);
		this.add(this.actionPanel);
	}
	
	private void changeAction(PanelControl panelControl, ControlType controlType)
	{
		Action action = this.rule.getAction();
		switch(controlType)
		{
		case COLUMN:
			action.setColumnPos(((NumberSpinner) panelControl).getNumber());
			break;
		case UNIT_TYPE:
			action.setUnitType(((EnumBox<UnitType>) panelControl).getEnumValue());
			break;
		case CONDITION_TYPE:
			((ActivateAction) action).setColumnSearchCondition(((EnumBox<ColumnSearchCondition>) panelControl).getEnumValue());
			break;
		}
	}
	
	public void processPanelControlEvent(Object source)
	{
		if (source instanceof PanelControl && !this.changingRule)
		{
			PanelControl panelControl = (PanelControl) source;
			ControlType controlType = panelControl.getControlType();
			PanelType panelType = panelControl.getPanelType();
			switch(panelType)
			{
				case ACTION:
					this.changeAction(panelControl, controlType);
					break;
				case CONDITION:
					this.changeCondition(panelControl, controlType);
					break;
			}
		}
	}
	
	private void changeCondition(PanelControl panelControl, ControlType controlType)
	{
		
	}
	
	public void changeSelectedRule(Rule rule)
	{
		this.changingRule = true;
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
		this.changingRule = false;
		this.actionPanel.enableBoxes();
		this.conditionPanel.getConditionFieldPanel().enableBoxes();
	}
}
