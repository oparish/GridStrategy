package assembler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import ai.Action;
import ai.ActionType;
import ai.ColumnSearchCondition;
import ai.DeployAction;
import ai.FurtherInputActivateAction;
import data.UnitType;
import main.Main;

public class ActionPanel extends JPanel
{
	private final EnumBox<ActionType> actionTypeBox;
	private final EnumBox<UnitType> unitTypeBox;
	private final EnumBox<ColumnSearchCondition> conditionBox;
	private final NumberSpinner positionSpinner;
	private final NumberSpinner furtherInputSpinner;
	private HashMap<ControlType, PanelControl> controlMap;
	private Assembler assembler;
	
	public ActionPanel(Assembler assembler)
	{
		super();
		this.assembler = assembler;
		this.setLayout(new GridBagLayout());
		this.actionTypeBox = new EnumBox<ActionType>(ActionType.values(), ControlType.ACTION_TYPE, PanelType.ACTION, assembler, false);
		this.setupRow(ControlType.ACTION_TYPE, this.actionTypeBox, 0);
		
		this.positionSpinner = new NumberSpinner(0, Main.GRIDWIDTH, ControlType.COLUMN, PanelType.ACTION, assembler, true);
		this.setupRow(ControlType.COLUMN, this.positionSpinner, 1);

		this.unitTypeBox = new EnumBox<UnitType>(UnitType.values(), ControlType.UNIT_TYPE, PanelType.ACTION, assembler, false);
		this.setupRow(ControlType.UNIT_TYPE, this.unitTypeBox, 2);
		
		this.conditionBox = new EnumBox<ColumnSearchCondition>(ColumnSearchCondition.values(), ControlType.CONDITION_TYPE, PanelType.ACTION, assembler, false);
		this.setupRow(ControlType.CONDITION_TYPE, this.conditionBox, 3);
		
		this.furtherInputSpinner = new NumberSpinner(0, 2, ControlType.FURTHER_INPUT, PanelType.ACTION, assembler, false);
		this.setupRow(ControlType.FURTHER_INPUT, this.furtherInputSpinner, 4);
		
		this.setupControlMap();
	}
	
	public boolean isDirty()
	{
		for (Entry<ControlType, PanelControl> entry : this.controlMap.entrySet())
		{
			if (entry.getValue().isDirty())
				return true;
		}

		return false;
	}
	
	public void changeEnabledControls(ActionType actionType)
	{
		if (actionType == ActionType.FURTHERINPUTACTIVATE_ACTION)
		{
			this.conditionBox.setEnabled(true);
			this.furtherInputSpinner.setEnabled(true);
		}
		else if (actionType == ActionType.ACTIVATE_ACTION)
		{
			this.conditionBox.setEnabled(true);
			this.furtherInputSpinner.setEnabled(false);
		}
		else
		{
			this.conditionBox.setEnabled(false);
			this.furtherInputSpinner.setEnabled(false);
		}
	}
	
	public void setNotDirty()
	{
		for (Entry<ControlType, PanelControl> entry : this.controlMap.entrySet())
		{
			entry.getValue().setDirty(false);
		}
	}
	
	public void enableBoxes()
	{
		this.actionTypeBox.setEnabled(true);
		this.positionSpinner.getCheckBox().setEnabled(true);
		this.unitTypeBox.setEnabled(true);
	}
	
	public void disableControls()
	{
		for (Entry<ControlType, PanelControl> entry : this.controlMap.entrySet())
		{
			entry.getValue().setEnabled(false);
		}
	}
	
	public void changeAction(Action action)
	{
		ActionType type;
		if (action.getClass() == DeployAction.class)
		{
			type = ActionType.DEPLOY_ACTION;
		}
		else if (action.getClass() == FurtherInputActivateAction.class)
		{
			type = ActionType.FURTHERINPUTACTIVATE_ACTION;
		}
		else
		{
			type = ActionType.ACTIVATE_ACTION;
		}
		this.actionTypeBox.setEnumValue(type);
		this.setNotDirty();
	}
	
	public void changePosition(int position)
	{
		if (position == Main.NO_SPECIFIC_COLUMN)
		{
			this.positionSpinner.getCheckBox().setSelected(false);
			this.positionSpinner.setEnabled(false);
		}
		else
		{
			this.positionSpinner.getCheckBox().setSelected(true);
			this.positionSpinner.setEnabled(true);
			this.positionSpinner.setValue(position);
		}
	}
	
	public void changeUnitTypeBox(UnitType unitType)
	{
		this.unitTypeBox.setEnumValue(unitType);
	}
	
	public void changeConditionBox(ColumnSearchCondition columnSearchCondition)
	{
		this.conditionBox.setEnabled(true);
		this.conditionBox.setEnumValue(columnSearchCondition);
	}
	
	public void disablePositionBox()
	{
		this.conditionBox.setEnabled(false);
	}
	
	private GridBagConstraints getGridBagConstraints(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = width;
		c.gridy = y;
		c.gridx = x;
		c.fill = GridBagConstraints.HORIZONTAL;
		return c;
	}
	
	private void setupRow(ControlType conditionSpinnerType, JComponent component, int y)
	{
		this.add(new JLabel(conditionSpinnerType.getText()), this.getGridBagConstraints(0, y, 1));
		this.add(component, this.getGridBagConstraints(1, y, 2));
		JCheckBox checkbox = ((PanelControl) component).getCheckBox();
		if (checkbox != null)
		{
			this.add(checkbox, this.getGridBagConstraints(3, y, 1));
			checkbox.addActionListener(this.assembler);
		}
	}
	
	private void setupControlMap()
	{
		this.controlMap = new HashMap<ControlType, PanelControl>();
		controlMap.put(ControlType.UNIT_TYPE, this.unitTypeBox);
		controlMap.put(ControlType.CONDITION_TYPE, this.conditionBox);
		controlMap.put(ControlType.COLUMN, this.positionSpinner);
		controlMap.put(ControlType.ACTION_TYPE, this.actionTypeBox);
		controlMap.put(ControlType.FURTHER_INPUT, this.furtherInputSpinner);
	}
	
	public HashMap<ControlType, PanelControl> getControls()
	{
		return this.controlMap;
	}
}
