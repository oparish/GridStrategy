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
import ai.SpecificColumnCondition;
import ai.UnitCountCondition;
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
		this.columnSpinner = new NumberSpinner(0, Main.GRIDWIDTH - 1, ControlType.COLUMN, PanelType.CONDITION, assembler, false);
		this.unitBox = new EnumBox<UnitType>(UnitType.values(), ControlType.UNIT_TYPE, PanelType.CONDITION, assembler, true);
		this.numberSpinner = new NumberSpinner(0, 99, ControlType.NUMBER, PanelType.CONDITION, assembler, true);
		this.rowSpinner = new NumberSpinner(0, Main.GRIDHEIGHT - 1, ControlType.ROW, PanelType.CONDITION, assembler, true);
		this.conditionBox = new EnumBox<ConditionType>(ConditionType.values(), ControlType.CONDITION_TYPE, PanelType.CONDITION, assembler, true);
		this.playerBox = new EnumBox<PlayerEnum>(PlayerEnum.values(), ControlType.UNIT_PLAYER, PanelType.CONDITION, assembler, true);
		this.gateBox = new EnumBox<GateType>(GateType.values(), ControlType.GATE_TYPE, PanelType.CONDITION, assembler, true);
		
		this.addControl(assembler, "Test", this.conditionTypeLabel);
		this.addControl(assembler, ControlType.COLUMN.getText(), this.columnSpinner);
		this.addControl(assembler, ControlType.UNIT_TYPE.getText(), this.unitBox);
		this.addControl(assembler, ControlType.NUMBER.getText(), this.numberSpinner);
		this.addControl(assembler, ControlType.ROW.getText(), this.rowSpinner);
		this.addControl(assembler, ControlType.CONDITION_TYPE.getText(), this.conditionBox);
		this.addControl(assembler, ControlType.GATE_TYPE.getText(), this.gateBox);
		this.addControl(assembler, ControlType.UNIT_PLAYER.getText(), this.playerBox);
		
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
		this.columnSpinner.switchEnabled(false);
		this.unitBox.switchEnabled(false);
		this.numberSpinner.switchEnabled(false);
		this.rowSpinner.switchEnabled(false);
		this.conditionBox.switchEnabled(false);
		this.playerBox.switchEnabled(false);
		this.gateBox.switchEnabled(false);
	}
	
	public void changeCondition(Condition condition)
	{	
		this.conditionTypeLabel.setText(condition.getConditionClassName());
		
		if (condition instanceof UnitCountCondition)
		{
			this.switchAllControls(true);
			UnitCountCondition unitCountCondition = (UnitCountCondition) condition;
			this.numberSpinner.setValue(unitCountCondition.getNumber());
			this.unitBox.setValue(unitCountCondition.getUnitType());
			this.rowSpinner.setValue(unitCountCondition.getRow());
			this.conditionBox.setValue(unitCountCondition.getConditionType());
			this.gateBox.switchEnabled(false);
			Boolean player = unitCountCondition.getUnitPlayer();
			if (player == null)
				this.playerBox.setValue(null);
			else if (player == true)
				this.playerBox.setValue(PlayerEnum.ONE);
			else
				this.playerBox.setValue(PlayerEnum.TWO);
			if (condition instanceof SpecificColumnCondition)
			{
				this.columnSpinner.setEnabled(true);
				this.columnSpinner.setValue(((SpecificColumnCondition)unitCountCondition).getColumn());
			}
			else
			{
				this.columnSpinner.setEnabled(false);
			}
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
		
	private void addControl(Assembler assembler, String text, JComponent control)
	{
		this.add(new JLabel(text));
		this.add(control);
		if (control instanceof PanelControl)
		{
			JCheckBox checkbox = ((PanelControl) control).getCheckBox();
			if (checkbox != null)
			{
				this.add(checkbox);
				checkbox.addActionListener(assembler);
			}
			else
			{
				this.add(new JPanel());
			}
		}
		else
		{
			this.add(new JPanel());
		}
	}
}
