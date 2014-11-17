package assembler;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

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
		this.setLayout(new GridLayout(6, 2));
		this.add(new JLabel(ControlType.COLUMN.getText()));
		this.columnSpinner = new NumberSpinner(0, Main.GRIDWIDTH - 1, ControlType.COLUMN, PanelType.CONDITION, assembler);
		this.add(this.columnSpinner);
		this.add(new JLabel(ControlType.UNIT_TYPE.getText()));
		this.unitBox = new EnumBox<UnitType>(UnitType.values(), ControlType.UNIT_TYPE, PanelType.CONDITION, assembler);
		this.add(this.unitBox);
		this.add(new JLabel(ControlType.NUMBER.getText()));
		this.numberSpinner = new NumberSpinner(0, Main.GRIDHEIGHT, ControlType.NUMBER, PanelType.CONDITION, assembler);
		this.add(this.numberSpinner);
		this.add(new JLabel(ControlType.ROW.getText()));
		this.rowSpinner = new NumberSpinner(0, Main.GRIDHEIGHT - 1, ControlType.ROW, PanelType.CONDITION, assembler);
		this.add(this.rowSpinner);	
		this.add(new JLabel(ControlType.CONDITION_TYPE.getText()));
		this.conditionBox = new EnumBox<ConditionType>(ConditionType.values(), ControlType.CONDITION_TYPE, PanelType.CONDITION, assembler);
		this.add(this.conditionBox);		
		this.add(new JLabel("Player"));
		this.playerBox = new EnumBox<Object>(new Object[]{"Self", "Enemy"}, ControlType.UNIT_PLAYER, PanelType.CONDITION, assembler);
		this.add(this.playerBox);	
	}
	
	public void enableBoxes()
	{
		this.columnSpinner.setEnabled(true);
		this.unitBox.setEnabled(true);
		this.numberSpinner.setEnabled(true);
		this.rowSpinner.setEnabled(true);
		this.conditionBox.setEnabled(true);
		this.playerBox.setEnabled(true);
	}
}
