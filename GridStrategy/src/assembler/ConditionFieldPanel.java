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
import data.UnitType;
import main.Main;

public class ConditionFieldPanel extends JPanel
{
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
		
		this.columnSpinner = new NumberSpinner(0, Main.GRIDWIDTH - 1, ControlType.COLUMN, PanelType.CONDITION, assembler);
		this.unitBox = new EnumBox<UnitType>(UnitType.values(), ControlType.UNIT_TYPE, PanelType.CONDITION, assembler);
		this.numberSpinner = new NumberSpinner(0, Main.GRIDHEIGHT, ControlType.NUMBER, PanelType.CONDITION, assembler);
		this.rowSpinner = new NumberSpinner(0, Main.GRIDHEIGHT - 1, ControlType.ROW, PanelType.CONDITION, assembler);
		this.conditionBox = new EnumBox<ConditionType>(ConditionType.values(), ControlType.CONDITION_TYPE, PanelType.CONDITION, assembler);
		this.playerBox = new EnumBox<Object>(new Object[]{"Self", "Enemy"}, ControlType.UNIT_PLAYER, PanelType.CONDITION, assembler);
		
		this.addControl("Test", new JLabel("Test"), false);
		this.addControl(ControlType.COLUMN.getText(), this.columnSpinner, true);
		this.addControl(ControlType.UNIT_TYPE.getText(), this.unitBox, true);
		this.addControl(ControlType.NUMBER.getText(), this.numberSpinner, false);
		this.addControl(ControlType.ROW.getText(), this.rowSpinner, true);
		this.addControl(ControlType.CONDITION_TYPE.getText(), this.conditionBox, true);
		this.addControl("Player", this.playerBox, true);
	}
	
	public void changeCondition(Condition condition)
	{
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
