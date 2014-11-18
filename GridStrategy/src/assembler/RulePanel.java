package assembler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
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
	private ListPanel listPanel;
	private boolean changingRule;
	private int selectedListIndex;
	
	public RulePanel(Assembler assembler, ListPanel listPanel)
	{
		super();
		this.setLayout(new GridLayout(2,1));
		this.listPanel = listPanel;
		this.conditionPanel = new ConditionPanel(assembler);
		this.add(this.conditionPanel);
		this.actionPanel = new ActionPanel(assembler);
		this.add(this.actionPanel);
	}
	
	private void changeAction(PanelControl panelControl, ControlType controlType)
	{
		Action action = this.rule.getAction();
		boolean changed;
		switch(controlType)
		{
		case COLUMN:
			changed = ((NumberSpinner) panelControl).getNumber() != action.getColumnPos();
			break;
		case UNIT_TYPE:
			changed = ((EnumBox<UnitType>) panelControl).getEnumValue() != action.getUnitType();
			break;
		case CONDITION_TYPE:
			changed = ((EnumBox<ColumnSearchCondition>) panelControl).getEnumValue() != ((ActivateAction) action).getColumnSearchCondition();
			break;
		default:
			changed = true;
		}
		panelControl.setDirty(changed);
	}
	
	private void completeActionChange(PanelControl panelControl, ControlType controlType)
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
	
	private boolean showOptionPane(String title, String message)
	{
		return JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
				null, new String[]{"Yes", "No"}, null) == 0;
	}
	
	public void changeSelectedRule(Rule rule, int index)
	{
		if (this.changingRule)
			return;
		this.changingRule = true;
		if (this.actionPanel.isDirty())
		{
			boolean result = this.showOptionPane("Action Changed", "Really cancel action changes?");
			if (!result)
			{
				this.listPanel.setListIndex(this.selectedListIndex);
				this.changingRule = false;
				return;
			}
		}

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
		this.selectedListIndex = index;
	}
}
