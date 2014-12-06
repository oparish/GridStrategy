package assembler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.UnitType;
import ai.Action;
import ai.ActivateAction;
import ai.CPlayer;
import ai.ColumnCondition;
import ai.ColumnSearchCondition;
import ai.Condition;
import ai.ConditionType;
import ai.DeployAction;
import ai.GateCondition;
import ai.NumberCondition;
import ai.Rule;
import main.FileOperations;

public class Assembler extends JFrame implements ActionListener, ChangeListener, ListSelectionListener
{
	RulePanel rulePanel;
	AssemblerList<Rule> ruleList;
	private Rule selectedRule;
	private Condition selectedCondition;
	private ConditionPanel conditionPanel;
	private ConditionFieldPanel conditionFieldPanel;
	private ActionPanel actionPanel;
	private ListPanel listPanel;
	private boolean changingControls;
	private int selectedListIndex;
	
	public Assembler() throws IOException
	{
		super();
		CPlayer cPlayer = FileOperations.loadCPlayer(this, true);
		this.setLayout(new GridBagLayout());
		ArrayList<Rule> rules = cPlayer.getRules();
		this.ruleList = new AssemblerList<Rule>(rules.toArray(new Rule[rules.size()]), AssemblerListType.RULE);
		this.listPanel = new ListPanel(this, this.ruleList);
		this.actionPanel = new ActionPanel(this);
		this.conditionFieldPanel = new ConditionFieldPanel(this);
		this.conditionPanel = new ConditionPanel(this, this.conditionFieldPanel);
		this.add(this.listPanel, this.getGridBagConstraints(0, 0, 1));
		this.rulePanel = new RulePanel(this, this.conditionPanel, this.actionPanel);
		this.add(this.rulePanel, this.getGridBagConstraints(1, 0, 1));
		this.setSize(2400, 500);
	}
	
	private GridBagConstraints getGridBagConstraints(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = width;
		c.gridy = y;
		c.gridx = x;
		c.fill = GridBagConstraints.VERTICAL;
		return c;
	}
	
	public static void main(String args[])
	{
		Assembler assembler;
		try
		{
			assembler = new Assembler();
			assembler.setVisible(true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void changeAction(PanelControl panelControl, ControlType controlType)
	{
		Action action = this.selectedRule.getAction();
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
		Action action = this.selectedRule.getAction();
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
		if (source instanceof PanelControl && !this.changingControls)
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
		else if (source instanceof AssemblerCheckBox && !this.changingControls)
		{
			AssemblerCheckBox fieldCheckBox = (AssemblerCheckBox) source;
			fieldCheckBox.getPanelControl().setEnabled(fieldCheckBox.isSelected());
		}
	}
	
	private void changeCondition(PanelControl panelControl, ControlType controlType)
	{
		Condition condition = this.selectedRule.getCondition();
		boolean changed;
		switch(controlType)
		{
		case COLUMN:
			changed = ((NumberSpinner) panelControl).getNumber() != ((ColumnCondition) condition).getColumn();
			break;
		case UNIT_TYPE:
			changed = ((EnumBox<UnitType>) panelControl).getEnumValue() != ((ColumnCondition) condition).getUnitType();
			break;
		case NUMBER:
			changed = ((NumberSpinner) panelControl).getNumber() != ((NumberCondition) condition).getNumber();
			break;
		case ROW:
			changed = ((NumberSpinner) panelControl).getNumber() != ((ColumnCondition) condition).getRow();
			break;
		case CONDITION_TYPE:
			changed = ((EnumBox<ConditionType>) panelControl).getEnumValue() != ((ColumnCondition) condition).getConditionType();
			break;
		case UNIT_PLAYER:
			PlayerEnum player;
			if (((ColumnCondition) condition).getUnitPlayer())
				player = PlayerEnum.ONE;
			else
				player = PlayerEnum.TWO;
			changed = ((EnumBox<PlayerEnum>) panelControl).getEnumValue() != player;
			break;
		default:
			changed = true;
		}
		panelControl.setDirty(changed);
	}
	
	private boolean showOptionPane(String title, String message)
	{
		return JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, 
				null, new String[]{"Yes", "No"}, null) == 0;
	}
	
	private void changeSelectedRule(Rule rule, int index)
	{
		if (this.actionPanel.isDirty())
		{
			boolean result = this.showOptionPane("Action Changed", "Really cancel action changes?");
			if (!result)
			{
				this.ruleList.setSelectedIndex(this.selectedListIndex);
				return;
			}
		}

		this.selectedRule = rule;
		Condition condition = rule.getCondition();
		this.changeSelectedCondition(condition);
		
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
		this.conditionPanel.clearHierarchyList();
		this.actionPanel.enableBoxes();
		this.selectedListIndex = index;
	}
	
	private void changeSelectedCondition(Condition condition)
	{
		this.selectedCondition = condition;
		this.conditionFieldPanel.changeCondition(condition);
		if (condition instanceof GateCondition)
			this.conditionPanel.updateGateList((GateCondition) condition);
		else
			this.conditionPanel.clearGateList();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		this.processPanelControlEvent(source);
	}


	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object source = e.getSource();
		this.processPanelControlEvent(source);	
	}


	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (this.changingControls)
			return;
		this.changingControls = true;
		if (e.getValueIsAdjusting())
		{
			AssemblerList<?> list = ((AssemblerList<?>) e.getSource());
			AssemblerListType type = list.getType();
			switch(type)
			{
				case RULE:
					this.changeSelectedRule((Rule) list.getSelectedValue(), list.getSelectedIndex());
					break;
				case GATE:
					this.conditionPanel.addToHierarchyList(this.selectedCondition);
					this.changeSelectedCondition((Condition) list.getSelectedValue());
					break;
				case HIERARCHY:
					this.changeSelectedCondition((Condition) list.getSelectedValue());
					this.conditionPanel.truncateHierarchyList(list.getSelectedIndex());
					break;
			}
		}
		this.changingControls = false;
	}
}
