package assembler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

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
	private EnumBox<PlayerEnum> playerBox;
	private EnumBox<GateType> gateBox;
	private HashMap<ControlType, PanelControl> controlMap;
	
	public ConditionFieldPanel(Assembler assembler)
	{
		super();
		this.setLayout(new GridLayout(8, 3));
		
		this.conditionTypeLabel = new JLabel("                   ");
		this.columnSpinner = new NumberSpinner(0, Main.GRIDWIDTH - 1, ControlType.COLUMN, PanelType.CONDITION, assembler);
		this.unitBox = new EnumBox<UnitType>(UnitType.values(), ControlType.UNIT_TYPE, PanelType.CONDITION, assembler);
		this.numberSpinner = new NumberSpinner(0, 99, ControlType.NUMBER, PanelType.CONDITION, assembler);
		this.rowSpinner = new NumberSpinner(0, Main.GRIDHEIGHT - 1, ControlType.ROW, PanelType.CONDITION, assembler);
		this.conditionBox = new EnumBox<ConditionType>(ConditionType.values(), ControlType.CONDITION_TYPE, PanelType.CONDITION, assembler);
		this.playerBox = new EnumBox<PlayerEnum>(PlayerEnum.values(), ControlType.UNIT_PLAYER, PanelType.CONDITION, assembler);
		this.gateBox = new EnumBox<GateType>(GateType.values(), ControlType.GATE_TYPE, PanelType.CONDITION, assembler);
		
		this.addControl(assembler, "Test", this.conditionTypeLabel, false);
		this.addControl(assembler, ControlType.COLUMN.getText(), this.columnSpinner, true);
		this.addControl(assembler, ControlType.UNIT_TYPE.getText(), this.unitBox, true);
		this.addControl(assembler, ControlType.NUMBER.getText(), this.numberSpinner, false);
		this.addControl(assembler, ControlType.ROW.getText(), this.rowSpinner, true);
		this.addControl(assembler, ControlType.CONDITION_TYPE.getText(), this.conditionBox, true);
		this.addControl(assembler, ControlType.GATE_TYPE.getText(), this.gateBox, false);
		this.addControl(assembler, "Player", this.playerBox, true);
		
		this.setupControlMap();
	}
	
	private void setupControlMap()
	{
		this.controlMap = new HashMap<ControlType, PanelControl>();
		controlMap.put(ControlType.COLUMN, this.columnSpinner);
		controlMap.put(ControlType.UNIT_TYPE, this.unitBox);
		controlMap.put(ControlType.NUMBER, this.numberSpinner);
		controlMap.put(ControlType.ROW, this.rowSpinner);
		controlMap.put(ControlType.CONDITION_TYPE, this.conditionBox);
		controlMap.put(ControlType.GATE_TYPE, this.gateBox);
		controlMap.put(ControlType.UNIT_PLAYER, this.playerBox);
	}
	
	public HashMap<ControlType, PanelControl> getControls()
	{
		return this.controlMap;
	}
	
	public boolean isDirty()
	{
		for (Entry<ControlType, PanelControl> entry : this.controlMap.entrySet())
		{
			PanelControl control = entry.getValue();
			if (control.isEnabled() && control.isDirty())
				return true;
		}
		return false;
	}
	
	public void setNotDirty()
	{
		this.columnSpinner.setDirty(false);
		this.unitBox.setDirty(false);
		this.numberSpinner.setDirty(false);
		this.rowSpinner.setDirty(false);
		this.conditionBox.setDirty(false);
		this.playerBox.setDirty(false);
		this.gateBox.setDirty(false);
	}
	
	public void disableControls()
	{
		this.columnSpinner.setEnabled(false);
		this.unitBox.setEnabled(false);
		this.numberSpinner.setEnabled(false);
		this.rowSpinner.setEnabled(false);
		this.conditionBox.setEnabled(false);
		this.playerBox.setEnabled(false);
		this.gateBox.setEnabled(false);
	}
	
	public void changeCondition(Condition condition)
	{	
		this.conditionTypeLabel.setText(condition.getConditionClassName());
		
		if (condition instanceof ColumnCondition)
		{
			this.switchAllControls(true);
			ColumnCondition columnCondition = (ColumnCondition) condition;
			this.numberSpinner.setValue(columnCondition.getNumber());
			this.columnSpinner.setValue(columnCondition.getColumn());
			this.unitBox.setValue(columnCondition.getUnitType());
			this.rowSpinner.setValue(columnCondition.getRow());
			this.conditionBox.setValue(columnCondition.getConditionType());
			this.gateBox.switchEnabled(false);
			if (columnCondition.getUnitPlayer())
				this.playerBox.setValue(PlayerEnum.ONE);
			else
				this.playerBox.setValue(PlayerEnum.TWO);
		}
		else if (condition instanceof GateCondition)
		{
			this.switchAllControls(false);
			this.gateBox.setValue(((GateCondition) condition).getGateType());
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
		this.gateBox.switchEnabled(value);
	}
		
	private void addControl(Assembler assembler, String text, JComponent control, boolean checkBox)
	{
		this.add(new JLabel(text));
		this.add(control);
		if (checkBox)
		{
			PanelControl panelControl = (PanelControl) control;
			AssemblerCheckBox checkbox = new AssemblerCheckBox(panelControl);
			this.add(checkbox);
			checkbox.addActionListener(assembler);
			panelControl.addCheckBox(checkbox);
		}
		else
		{
			this.add(new JPanel());
		}
	}
}
