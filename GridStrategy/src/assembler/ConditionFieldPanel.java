package assembler;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import ai.ColumnCondition;
import ai.Condition;
import ai.ConditionType;
import ai.CreditCondition;
import ai.GateCondition;
import ai.GateType;
import ai.NoCondition;
import data.UnitType;
import main.Main;

public class ConditionFieldPanel extends JPanel
{
	private JLabel conditionTypeLabel;
	private NumberSpinner columnSpinner;
	private EnumBox<UnitType> unitBox;
	private NumberSpinner numberSpinner;
	private NumberSpinner rowSpinner;
	private EnumBox<ConditionType> conditionBox;
	private EnumBox<Object> playerBox;
	
	public ConditionFieldPanel(Assembler assembler)
	{
		super();
		this.setLayout(new GridLayout(7, 3));
		
		this.conditionTypeLabel = new JLabel("                   ");
		this.columnSpinner = new NumberSpinner(0, Main.GRIDWIDTH - 1, ControlType.COLUMN, PanelType.CONDITION, assembler);
		this.unitBox = new EnumBox<UnitType>(UnitType.values(), ControlType.UNIT_TYPE, PanelType.CONDITION, assembler);
		this.numberSpinner = new NumberSpinner(0, Main.GRIDHEIGHT, ControlType.NUMBER, PanelType.CONDITION, assembler);
		this.rowSpinner = new NumberSpinner(0, Main.GRIDHEIGHT - 1, ControlType.ROW, PanelType.CONDITION, assembler);
		this.conditionBox = new EnumBox<ConditionType>(ConditionType.values(), ControlType.CONDITION_TYPE, PanelType.CONDITION, assembler);
		this.playerBox = new EnumBox<Object>(new Object[]{"Self", "Enemy"}, ControlType.UNIT_PLAYER, PanelType.CONDITION, assembler);
		
		this.addControl("Test", this.conditionTypeLabel, false);
		this.addControl(ControlType.COLUMN.getText(), this.columnSpinner, true);
		this.addControl(ControlType.UNIT_TYPE.getText(), this.unitBox, true);
		this.addControl(ControlType.NUMBER.getText(), this.numberSpinner, false);
		this.addControl(ControlType.ROW.getText(), this.rowSpinner, true);
		this.addControl(ControlType.CONDITION_TYPE.getText(), this.conditionBox, true);
		this.addControl("Player", this.playerBox, true);
	}
	
	public void changeCondition(Condition condition)
	{
		if (condition instanceof GateCondition)
			this.conditionTypeLabel.setText("Gate Condition " + ((GateCondition) condition).getGateType().name());
		else
			this.conditionTypeLabel.setText(condition.getConditionClassName());
		
		if (condition instanceof ColumnCondition)
		{
			ColumnCondition columnCondition = (ColumnCondition) condition;
			this.numberSpinner.setValue(columnCondition.getNumber());
			this.columnSpinner.setValue(columnCondition.getColumn());
			this.unitBox.setValue(columnCondition.getUnitType());
			this.rowSpinner.setValue(columnCondition.getRow());
			this.conditionBox.setValue(columnCondition.getConditionType());
			this.playerBox.setValue(columnCondition.getUnitPlayer());
		}
		else if (condition instanceof GateCondition)
		{
			this.switchAllControls(false);
		}
		else if (condition instanceof CreditCondition)
		{
			CreditCondition creditCondition = (CreditCondition) condition;
			this.switchAllControls(false);
			this.numberSpinner.setValue(creditCondition.getNumber());
		}
		else if (condition instanceof NoCondition)
		{
			this.switchAllControls(false);
		}
	}
	
	private void switchAllControls(boolean value)
	{
		this.numberSpinner.switchEnabled(value);
		this.columnSpinner.switchEnabled(value);
		this.unitBox.switchEnabled(value);
		this.rowSpinner.switchEnabled(value);
		this.conditionBox.switchEnabled(value);
		this.playerBox.switchEnabled(value);
	}
		
	private void addControl(String text, JComponent control, boolean checkBox)
	{
		this.add(new JLabel(text));
		this.add(control);
		if (checkBox)
		{
			JCheckBox checkbox = new JCheckBox();
			this.add(checkbox);
			((PanelControl) control).addCheckBox(checkbox);
		}
		else
		{
			this.add(new JPanel());
		}
	}
}
