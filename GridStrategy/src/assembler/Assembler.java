package assembler;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import ai.ActionType;
import ai.ActivateAction;
import ai.CPlayer;
import ai.ClearAction;
import ai.ColumnCondition;
import ai.ColumnSearchCondition;
import ai.Condition;
import ai.ConditionType;
import ai.CreditCondition;
import ai.DeployAction;
import ai.FurtherInputActivateAction;
import ai.GateCondition;
import ai.GateType;
import ai.NoCondition;
import ai.NumberCondition;
import ai.Rule;
import ai.SpecificColumnCondition;
import main.FileOperations;
import main.Main;

public class Assembler extends JFrame implements ActionListener, ChangeListener, ListSelectionListener
{
	private static final String INITDIALOG_TITLE = "Opening";
	private static final String INITDIALOG_MESSAGE = "Load an existing file?";
	
	RulePanel rulePanel;
	AssemblerList<Rule> ruleList;
	private CPlayer cPlayer;
	private AssemblerList<Condition> hierarchyList;
	private AssemblerList<Condition> gateList;
	private AssemblerList<Action> actionsList;
	private Rule selectedRule;
	private Condition selectedCondition;
	private Action selectedAction;
	private ConditionPanel conditionPanel;
	private ActionPanel actionPanel;
	private ConditionFieldPanel conditionFieldPanel;
	private ActionFieldPanel actionFieldPanel;
	private ListPanel listPanel;
	private boolean changingControls;
	private int selectedListIndex;
	private List<Condition> hierarchyContents = new ArrayList<Condition>();
	private List<Rule> ruleListContents = new ArrayList<Rule>();
	private List<Action> actionsContents;
	
	public Assembler() throws IOException
	{
		super();
		this.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });
		if (this.initialDialog())
			this.cPlayer = FileOperations.loadCPlayer(this, true);
		else
			this.cPlayer = new CPlayer(new ArrayList<Rule>(), true);
		this.setLayout(new GridBagLayout());
		this.ruleListContents = this.cPlayer.getRules();
		this.setupHierarchyList();
		this.setupGateList();
		this.setupActionsList();
		this.ruleList = new AssemblerList<Rule>(this.ruleListContents.toArray(new Rule[this.ruleListContents.size()]), 
				AssemblerListType.RULE);
		this.listPanel = new ListPanel(this, this.ruleList);
		this.actionFieldPanel = new ActionFieldPanel(this);
		this.conditionFieldPanel = new ConditionFieldPanel(this);
		this.actionPanel = new ActionPanel(this.actionFieldPanel, this.actionsList);
		this.conditionPanel = new ConditionPanel(this.conditionFieldPanel, this.hierarchyList, this.gateList);
		this.add(this.listPanel, this.getGridBagConstraints(0, 0, 1));
		this.rulePanel = new RulePanel(this, this.conditionPanel, this.actionFieldPanel);
		this.add(this.rulePanel, this.getGridBagConstraints(1, 0, 1));
		this.setSize(2400, 500);
	}
	
	private boolean initialDialog()
	{
		return JOptionPane.showOptionDialog(this, INITDIALOG_MESSAGE, INITDIALOG_TITLE, JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No"}, null) == 0;
	}
	
	public void addToHierarchyList(Condition condition)
	{
		hierarchyContents.add(condition);
		Condition[] conditions = new Condition[hierarchyContents.size()];
		this.hierarchyContents.toArray(conditions);
		this.hierarchyList.setListData(conditions);
	}
	
	public void addToActionList(Action action)
	{
		this.actionsContents.add(action);
		Action[] actionArray = new Action[actionsContents.size()];
		this.actionsContents.toArray(actionArray);
		this.actionsList.setListData(actionArray);
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
	
	private void setupActionsList()
	{
		this.actionsList = new AssemblerList<Action>(new Action[]{}, AssemblerListType.ACTION);
//		this.actionsList.setCellRenderer(new ConditionListCellRenderer());
		this.actionsList.addListSelectionListener(this);
	}
	
	private void clearActionsList()
	{
		this.actionsList.setListData(new Action[]{});
	}
	
	private void setupGateList()
	{
		
		this.gateList = new AssemblerList<Condition>(new Condition[]{}, AssemblerListType.GATE);
		this.gateList.setCellRenderer(new ConditionListCellRenderer());
		this.gateList.addListSelectionListener(this);
	}
	
	private void changeAction(PanelControl panelControl, ControlType controlType)
	{
		boolean changed;
		switch(controlType)
		{
		case ACTION_TYPE:
			ActionType oldType;
			ActionType newType = ((EnumBox<ActionType>) panelControl).getEnumValue();
			oldType = ActionType.getActionType(this.selectedAction.getClass());
			changed = newType != oldType;
			this.actionFieldPanel.changeEnabledControls(newType);
			break;
		case COLUMN:
			changed = ((NumberSpinner) panelControl).getNumber() != this.selectedAction.getColumnPos();
			break;
		case UNIT_TYPE:
			changed = ((EnumBox<UnitType>) panelControl).getEnumValue() != this.selectedAction.getUnitType();
			break;
		case CONDITION_TYPE:
			if (this.selectedAction instanceof ActivateAction)
				changed = ((EnumBox<ColumnSearchCondition>) panelControl).getEnumValue() != ((ActivateAction) this.selectedAction).getColumnSearchCondition();
			else
				changed = false;
			break;
		default:
			changed = true;
		}
		panelControl.setDirty(changed);
	}
	
	private void completeActionChange(PanelControl panelControl, ControlType controlType, Action action)
	{
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
		case FURTHER_INPUT:
			((FurtherInputActivateAction) action).setFurtherInput(((NumberSpinner) panelControl).getNumber());
			break;
		}
	}
	
	private void completeConditionChange(PanelControl panelControl, ControlType controlType, Condition condition)
	{
		switch(controlType)
		{
		case COLUMN:
			((SpecificColumnCondition)condition).setColumn(((NumberSpinner) panelControl).getNumber());
			break;
		case UNIT_TYPE:
			((ColumnCondition)condition).setUnitType(((EnumBox<UnitType>) panelControl).getEnumValue());
			break;
		case NUMBER:
			((NumberCondition)condition).setNumber(((NumberSpinner) panelControl).getNumber());
			break;
		case ROW:
			((ColumnCondition)condition).setRow(((NumberSpinner) panelControl).getNumber());
			break;
		case GATE_TYPE:
			((GateCondition)condition).setGateType(((EnumBox<GateType>) panelControl).getEnumValue());
			break;
		case CONDITION_TYPE:
			((ColumnCondition)condition).setConditionType(((EnumBox<ConditionType>) panelControl).getEnumValue());
			break;
		case UNIT_PLAYER:
			PlayerEnum playerEnum = ((EnumBox<PlayerEnum>) panelControl).getEnumValue();
			boolean value = playerEnum == PlayerEnum.ONE ? true : false;	
			((ColumnCondition) condition).setUnitPlayer(value);
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
			PanelControl panelControl = fieldCheckBox.getPanelControl();
			panelControl.setEnabled(fieldCheckBox.isSelected());
			panelControl.setDirty(true);
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
			this.cPlayer.setRules(newContents);
		}
	}
	
	private void saveCPlayer(boolean as)
	{
		this.saveAction();
		this.saveCondition();
		
		try
		{
			if (as || !FileOperations.hasLastCPlayer())
				FileOperations.saveCPlayerAs(this.cPlayer.toBytes(), this);
			else
				FileOperations.saveCPlayer(this.cPlayer.toBytes(), this);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void resetCPlayer()
	{
		try {
			FileOperations.resetCPlayer(this, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.resetFields();
	}
	
	private void removeSelectedRule()
	{
		this.changingControls = true;
		Rule[] rules = this.ruleList.getContents();
		Rule[] newRules = new Rule[rules.length - 1];
		int i = 0;
		for (Rule rule : rules)
		{
			if (rule != this.selectedRule)
			{
				newRules[i] = rule;
				i++;
			}
		}
		this.ruleList.setListData(newRules);
		this.ruleListContents.remove(this.selectedRule);
		this.selectedRule = null;
		this.conditionFieldPanel.disableControls();
		this.actionFieldPanel.disableControls();
		this.conditionFieldPanel.setNotDirty();
		this.actionFieldPanel.setNotDirty();
		this.changingControls = false;
	}
	
	private void newFile()
	{
		this.selectedRule = null;
		this.selectedCondition = null;
		this.cPlayer = new CPlayer(new ArrayList<Rule>(), true);
		this.conditionFieldPanel.disableControls();
		this.actionFieldPanel.disableControls();
		this.ruleList.setListData(new Rule[0]);
		this.hierarchyList.setListData(new Condition[0]);
		this.gateList.setListData(new Condition[0]);
		this.actionsList.setListData(new Action[0]);
		this.ruleListContents.clear();
		this.hierarchyContents.clear();
		FileOperations.clearLastCPlayer();
	}
	
	private void resetAction()
	{
		this.loadAction(this.selectedAction);
	}
	
	private void resetCondition()
	{
		this.loadCondition(this.selectedCondition);
	}
	
	private void newAction()
	{
		this.addToActionList(new ClearAction(0));
	}
	
	private void processButtonPress(AssemblerButton assemblerButton)
	{
		switch(assemblerButton.getButtonType())
		{
		case RESET:
			this.resetCPlayer();
			break;
		case LOAD:
			this.loadCPlayer();
			break;
		case RESET_ACTION:
			this.resetAction();
			break;
		case RESET_CONDITION:
			this.resetCondition();
			break;
		case SAVE_FILE:
			this.saveCPlayer(false);
			break;
		case SAVE_FILE_AS:
			this.saveCPlayer(true);
			break;
		case MOVE_UP:
			this.moveRule(true);
			break;
		case MOVE_DOWN:
			this.moveRule(false);
			break;
		case NEW_ACTION:
			this.newAction();
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
		case REMOVE_RULE:
			if (this.selectedRule != null)
				this.removeSelectedRule();
			break;
		case NEW_FILE:
			this.newFile();
			break;
		}
	}
	
	private void saveCondition()
	{
		if (this.selectedCondition == null)
			return;
		
		Condition newCondition;
		
		HashMap<ControlType, PanelControl> controlMap = this.conditionFieldPanel.getControls();
		
		if (this.selectedCondition instanceof GateCondition)
			newCondition = Condition.getConditionExample(GateCondition.class);
		else if (this.selectedCondition instanceof CreditCondition)
			newCondition = Condition.getConditionExample(CreditCondition.class);
		else if (this.selectedCondition instanceof NoCondition)
			newCondition = Condition.getConditionExample(NoCondition.class);
		else if (this.selectedCondition instanceof SpecificColumnCondition)
			newCondition = Condition.getConditionExample(SpecificColumnCondition.class);
		else
			newCondition = Condition.getConditionExample(ColumnCondition.class);

		for (Entry<ControlType, PanelControl> entry : controlMap.entrySet())
		{
			PanelControl control = entry.getValue();
			if (control.isEnabled())
				this.completeConditionChange(control, entry.getKey(), newCondition);
		}
		this.selectedRule.setCondition(newCondition);
		this.selectedCondition = newCondition;
		this.conditionFieldPanel.setNotDirty();
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
		int selectedIndex = this.ruleList.getLastVisibleIndex();
		this.ruleList.setSelectedIndex(selectedIndex);
		this.changeSelectedRule(rule, selectedIndex);
	}
	
	public void insertBatch(ArrayList<Rule> batch, int batchCutoff, int listCutoff)
	{
		ArrayList<Rule> newItems = new ArrayList<Rule>();
		Rule[] contents = this.ruleList.getContents();
		for (int j = 0; j < contents.length; j++)
		{
			if (j != listCutoff)
				newItems.add(contents[j]);
		}
		for (int i = 0; i < Main.GRIDWIDTH - 1; i++)
		{
			if (i == batchCutoff)
			{
				newItems.add(contents[listCutoff]);
			}
			Rule item = batch.get(i);
			newItems.add(item);
		}
		if (Main.GRIDWIDTH - 1 == batchCutoff)
		{
			newItems.add(contents[listCutoff]);
		}
		Rule[] ruleArray = new Rule[newItems.size()];
		this.ruleList.setListData(newItems.toArray(ruleArray));
		this.ruleListContents.clear();
		this.ruleListContents.addAll(newItems);
	}
	
	private void changeCondition(PanelControl panelControl, ControlType controlType)
	{
		Condition condition = this.selectedRule.getCondition();
		boolean changed;
		Integer number;
		switch(controlType)
		{
		case COLUMN:
			number = ((SpecificColumnCondition) condition).getColumn();
			changed =  number == null || number != ((NumberSpinner) panelControl).getNumber();
			break;
		case UNIT_TYPE:
			UnitType unitType  = ((ColumnCondition) condition).getUnitType();
			changed = unitType == null || ((EnumBox<UnitType>) panelControl).getEnumValue() != unitType;
			break;
		case NUMBER:
			number = ((NumberCondition) condition).getNumber();
			changed = number == null || ((NumberSpinner) panelControl).getNumber() != number;
			break;
		case ROW:
			number = ((ColumnCondition) condition).getRow();
			changed = number== null || ((NumberSpinner) panelControl).getNumber() != number;
			break;
		case CONDITION_TYPE:
			ConditionType conditionType = ((ColumnCondition) condition).getConditionType();
			changed = conditionType == null || ((EnumBox<ConditionType>) panelControl).getEnumValue() != conditionType;
			break;
		case GATE_TYPE:
			GateType gateType = ((GateCondition) condition).getGateType();
			changed = gateType == null || ((EnumBox<GateType>) panelControl).getEnumValue() != gateType;
			break;
		case UNIT_PLAYER:
			PlayerEnum player;
			Boolean currentValue = ((ColumnCondition) condition).getUnitPlayer();
			if (currentValue == null)
				player = null;
			else if (currentValue == true)
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
		if (this.actionFieldPanel.isDirty())
		{
			this.saveAction();
		}
		
		if (this.conditionFieldPanel.isDirty())
		{
			this.saveCondition();
		}

		this.selectedRule = rule;
		Condition condition = rule.getCondition();
		this.changeSelectedCondition(condition);
		this.loadActions(rule);
		this.actionFieldPanel.changeEnabledControls(ActionType.getActionType(this.selectedAction.getClass()));
		
		this.clearHierarchyList();
		
		this.selectedListIndex = index;
	}
	
	private void loadActions(Rule rule)
	{
		Action[] actionsArray = new Action[rule.getActions().size()];
		this.actionsList.setListData(rule.getActions().toArray(actionsArray));
	}
	
	private void loadAction(Action action)
	{
		this.selectedAction = action;
		this.actionFieldPanel.changeAction(action);
		this.actionFieldPanel.changePosition(action.getColumnPos());
		
		switch(ActionType.getActionType(action.getClass()))
		{
		case ACTIVATE_ACTION:
			ActivateAction activateAction = (ActivateAction) action;
			this.actionFieldPanel.changeConditionBox(activateAction.getColumnSearchCondition());
			break;
		case DEPLOY_ACTION:
		case FURTHERINPUTACTIVATE_ACTION:
			this.actionFieldPanel.changeUnitTypeBox(action.getUnitType());
			this.actionFieldPanel.disablePositionBox();
			break;
		case CLEAR_ACTION:
			this.actionFieldPanel.disableUnitTypeBox();
			this.actionFieldPanel.disablePositionBox();
			break;
		}
	}
	
	private void changeSelectedCondition(Condition condition)
	{
		this.selectedCondition = condition;
		this.loadCondition(condition);
	}
	
	private void loadCondition(Condition condition)
	{
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
				case ACTION:
					this.loadAction(this.actionsList.getSelectedValue());
				break;
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
	
	private void loadCPlayer()
	{
		try {
			this.cPlayer = FileOperations.loadCPlayer(this, true);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		this.resetFields();
	}
	
	private void resetFields()
	{
		this.ruleListContents = this.cPlayer.getRules();
		this.ruleList.setListData(this.ruleListContents.toArray(new Rule[this.ruleListContents.size()]));
		this.selectedRule = null;
		this.selectedCondition = null;
		this.clearHierarchyList();
		this.clearActionsList();
		this.clearGateList();
		this.actionFieldPanel.disableControls();
		this.conditionFieldPanel.disableControls();
	}
	
	private void saveAction()
	{
		if (this.selectedRule == null)
			return;
			
		Action newAction;
		
		HashMap<ControlType, PanelControl> controlMap = this.actionFieldPanel.getControls();
		EnumBox<ActionType> actionBox = (EnumBox<ActionType>) controlMap.get(ControlType.ACTION_TYPE);
		ActionType actionType = actionBox.getEnumValue();
		newAction = Action.getActionExample(actionType.getActionClass());
		
		for (Entry<ControlType, PanelControl> entry : controlMap.entrySet())
		{
			PanelControl control = entry.getValue();
			
			if (entry.getKey() == ControlType.COLUMN && !control.isEnabled())
			{
				newAction.setColumnPos(Main.NO_SPECIFIC_COLUMN);
			}
			else if (control.isEnabled() && entry.getKey() != ControlType.ACTION_TYPE)
				this.completeActionChange(control, entry.getKey(), newAction);
		}
		this.selectedRule.getActions().set(this.actionsList.getSelectedIndex(), newAction);
		this.actionFieldPanel.setNotDirty();
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
