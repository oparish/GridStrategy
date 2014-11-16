package assembler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.HashMap;

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
import data.UnitType;
import main.Main;

public class ActionPanel extends JPanel
{
	private final EnumBox<ActionType> actionTypeBox;
	private final EnumBox<UnitType> unitTypeBox;
	private final EnumBox<ColumnSearchCondition> conditionBox;
	private final NumberSpinner positionSpinner;
	
	public ActionPanel(ChangeListener listener)
	{
		super();
		this.setLayout(new GridBagLayout());
		this.actionTypeBox = new EnumBox<ActionType>(ActionType.values());
		this.setupRow(ConditionSpinnerType.ACTION_TYPE, this.actionTypeBox, 0);
		
		this.positionSpinner = new NumberSpinner(ConditionSpinnerType.NUMBER, 0, Main.GRIDWIDTH);
		this.setupRow(ConditionSpinnerType.COLUMN, this.positionSpinner, 1);

		this.unitTypeBox = new EnumBox<UnitType>(UnitType.values());
		this.setupRow(ConditionSpinnerType.UNIT_TYPE, this.unitTypeBox, 2);
		
		this.conditionBox = new EnumBox<ColumnSearchCondition>(ColumnSearchCondition.values());
		this.setupRow(ConditionSpinnerType.CONDITION_TYPE, this.conditionBox, 3);
	}
	
	public void changeAction(Action action)
	{
		ActionType type;
		if (action.getClass() == DeployAction.class)
		{
			type = ActionType.DEPLOY_ACTION;
		}
		else
		{
			type = ActionType.ACTIVATE_ACTION;
		}
		this.actionTypeBox.setEnumValue(type);
	}
	
	public void changePosition(int position)
	{
		this.positionSpinner.setValue(position);
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
	
	private void setupRow(ConditionSpinnerType conditionSpinnerType, JComponent component, int y)
	{
		this.add(new JLabel(conditionSpinnerType.getText()), this.getGridBagConstraints(0, y, 1));
		this.add(component, this.getGridBagConstraints(1, y, 2));
	}
}
