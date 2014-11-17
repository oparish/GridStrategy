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
	private final JLabel actionTypeBox;
	private final EnumBox<UnitType> unitTypeBox;
	private final EnumBox<ColumnSearchCondition> conditionBox;
	private final NumberSpinner positionSpinner;
	
	public ActionPanel(Assembler assembler)
	{
		super();
		this.setLayout(new GridBagLayout());
		this.actionTypeBox = new JLabel("");
		this.setupRow(ControlType.ACTION_TYPE, this.actionTypeBox, 0);
		
		this.positionSpinner = new NumberSpinner(0, Main.GRIDWIDTH, ControlType.COLUMN, PanelType.ACTION, assembler);
		this.setupRow(ControlType.COLUMN, this.positionSpinner, 1);

		this.unitTypeBox = new EnumBox<UnitType>(UnitType.values(), ControlType.UNIT_TYPE, PanelType.ACTION, assembler);
		this.setupRow(ControlType.UNIT_TYPE, this.unitTypeBox, 2);
		
		this.conditionBox = new EnumBox<ColumnSearchCondition>(ColumnSearchCondition.values(), ControlType.CONDITION_TYPE, PanelType.ACTION, assembler);
		this.setupRow(ControlType.CONDITION_TYPE, this.conditionBox, 3);
	}
	
	public void enableBoxes()
	{
		this.actionTypeBox.setEnabled(true);
		this.positionSpinner.setEnabled(true);
		this.unitTypeBox.setEnabled(true);
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
		this.actionTypeBox.setText(type.name());
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
	
	private void setupRow(ControlType conditionSpinnerType, JComponent component, int y)
	{
		this.add(new JLabel(conditionSpinnerType.getText()), this.getGridBagConstraints(0, y, 1));
		this.add(component, this.getGridBagConstraints(1, y, 2));
	}
}
