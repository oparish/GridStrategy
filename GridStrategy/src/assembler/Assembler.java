package assembler;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
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
import ai.GateType;
import ai.NumberCondition;
import ai.Rule;
import main.FileOperations;

public class Assembler extends JFrame implements ActionListener, ChangeListener, ListSelectionListener
{
	RulePanel rulePanel;
	AssemblerList<Rule> ruleList;
	private AssemblerList<Condition> hierarchyList;
	private AssemblerList<Condition> gateList;
	private Rule selectedRule;
	private Condition selectedCondition;
	private ConditionPanel conditionPanel;
	private ConditionFieldPanel conditionFieldPanel;
	private ActionPanel actionPanel;
	private ListPanel listPanel;
	private boolean changingControls;
	private int selectedListIndex;
	private List<Condition> hierarchyContents = new ArrayList<Condition>();
	private List<Rule> ruleListContents = new ArrayList<Rule>();
	
	public Assembler() throws IOException
	{
		super();
		CPlayer cPlayer = FileOperations.loadCPlayer(this, true);
		this.setLayout(new GridBagLayout());
		this.ruleListContents = cPlayer.getRules();
		this.setupHierarchyList();
		this.setupGateList();
		this.ruleList = new AssemblerList<Rule>(this.ruleListContents.toArray(new Rule[this.ruleListContents.size()]), 
				AssemblerListType.RULE);
		this.listPanel = new ListPanel(this, this.ruleList);
		this.actionPanel = new ActionPanel(this);
		this.conditionFieldPanel = new ConditionFieldPanel(this);
		this.conditionPanel = new ConditionPanel(this.conditionFieldPanel, this.hierarchyList, this.gateList);
		this.add(this.listPanel, this.getGridBagConstraints(0, 0, 1));
		this.rulePanel = new RulePanel(this, this.conditionPanel, this.actionPanel);
		this.add(this.rulePanel, this.getGridBagConstraints(1, 0, 1));
		this.setSize(2400, 500);
	}
	
	public void addToHierarchyList(Condition condition)
	{
		hierarchyContents.add(condition);
		Condition[] conditions = new Condition[hierarchyContents.size()];
		this.hierarchyContents.toArray(conditions);
		this.hierarchyList.setListData(conditions);
	}
	
	public void truncateHierarchyList(int index)
	{
		Condition[] conditions = new Condition[index];
		this.hierarchyContents = hierarchyContents.subList(0, index);
		this.hierarchyContents.toArray(conditions);
		this.hierarchyList.setListData(conditions);
	}
	
	public void updateGateList(GateCondition gateCondition)
	{
		this.gateList.setListData(new Condition[]{gateCondition.getCondition1(), gateCondition.getCondition2()});
	}
	
	public void clearHierarchyList()
	{
		this.hierarchyContents.clear();
		this.hierarchyList.setListData(new Condition[]{});
	}
	
	public void clearGateList()
	{
		this.gateList.setListData(new Condition[]{});
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
	
	private void setupHierarchyList()
	{
		this.hierarchyList = new AssemblerList<Condition>(new Condition[]{}, AssemblerListType.HIERARCHY);
		this.hierarchyList.setCellRenderer(new ConditionListCellRenderer());
		this.hierarchyList.addListSelectionListener(this);
	}
	
	private void setupGateList()
	{
		
		this.gateList = new AssemblerList<Condition>(new Condition[]{}, AssemblerListType.GATE);
		this.gateList.setCellRenderer(new ConditionListCellRenderer());
		this.gateList.addListSelectionListener(this);
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
		else if (source instanceof AssemblerButton)
		{
			processButtonPress((AssemblerButton) source);
		}
	}
	
	private void moveRule(boolean up)
	{
		int oldIndex = this.ruleList.getSelectedIndex();
		Rule[] oldContents = this.ruleList.getContents();
		int oldLength = oldContents.length;
		if ((oldIndex != 0 || !up) && (oldIndex != (oldLength - 1) || up))
		{
			int newIndex = up ? oldIndex - 1 : oldIndex + 1;

			Rule[] newContents = new Rule[oldLength];
			for (int i = 0; i < oldLength; i++)
			{
				if (i == newIndex)
				{
					newContents[oldIndex] = oldContents[newIndex];
				}
				else if (i == oldIndex)
				{
					newContents[newIndex] = oldContents[oldIndex];
				}
				else
				{
					newContents[i] = oldContents[i];
				}
			}
			this.ruleList.setListData(newContents);
			this.ruleList.setSelectedIndex(newIndex);
		}
	}
	
	private void processButtonPress(AssemblerButton assemblerButton)
	{
		switch(assemblerButton.getButtonType())
		{
		case MOVE_UP:
			this.moveRule(true);
			break;
		case MOVE_DOWN:
			this.moveRule(false);
			break;
		case CHANGE_CONDITION:
			if (this.selectedCondition != null)
			{
				NewConditionDialog newConditionDialog = new NewConditionDialog(this);
				newConditionDialog.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(new JFrame(), "You must select a condition first.", "No selected condition.",
				        JOptionPane.ERROR_MESSAGE);
			}
			break;
		case ADD_RULE:
			AddRuleDialog addRuleDialog = new AddRuleDialog(this);
			addRuleDialog.setVisible(true);
			break;
		}
	}
	
	public void changeCondition(Condition newCondition)
	{
		this.changingControls = true;
		int contentsSize = hierarchyContents.size();
		if (contentsSize == 0)
		{
			this.selectedRule.setCondition(newCondition);
		}
		else
		{
			GateCondition ownerCondition = (GateCondition) hierarchyContents.get(contentsSize - 1);
			if (ownerCondition.getCondition1() == this.selectedCondition)
			{
				ownerCondition.setCondition1(newCondition);
			}
			else
			{
				ownerCondition.setCondition2(newCondition);
			}
		}
		this.changeSelectedCondition(newCondition);
		this.changingControls = false;
	}
	
	public void addRule(Rule rule)
	{
		this.ruleListContents.add(rule);
		this.ruleList.setListData(this.ruleListContents.toArray(new Rule[this.ruleListContents.size()]));
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
		case GATE_TYPE:
			changed = ((EnumBox<GateType>) panelControl).getEnumValue() != ((GateCondition) condition).getGateType();
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
		this.clearHierarchyList();
		this.actionPanel.enableBoxes();
		this.selectedListIndex = index;
	}
	
	private void changeSelectedCondition(Condition condition)
	{
		this.selectedCondition = condition;
		this.conditionFieldPanel.changeCondition(condition);
		if (condition instanceof GateCondition)
			this.updateGateList((GateCondition) condition);
		else
			this.clearGateList();
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
					this.addToHierarchyList(this.selectedCondition);
					this.changeSelectedCondition((Condition) list.getSelectedValue());
					break;
				case HIERARCHY:
					this.changeSelectedCondition((Condition) list.getSelectedValue());
					this.truncateHierarchyList(list.getSelectedIndex());
					break;
			}
		}
		this.changingControls = false;
	}
	
	private class ConditionListCellRenderer implements ListCellRenderer<Condition>
	{
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Condition> arg0, Condition condition, int index, boolean selected,
				boolean arg4)
		{
			String text = condition.getConditionClassName();
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
