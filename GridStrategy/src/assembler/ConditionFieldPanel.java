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
	public ConditionFieldPanel(ChangeListener listener)
	{
		super();
		this.setLayout(new GridLayout(6, 2));
		this.add(new JLabel(ConditionSpinnerType.COLUMN.getText()));
		this.add(new NumberSpinner(ConditionSpinnerType.COLUMN, 0, Main.GRIDWIDTH - 1));
		this.add(new JLabel(ConditionSpinnerType.UNIT_TYPE.getText()));
		this.add(new EnumSpinner(UnitType.values()));
		this.add(new JLabel(ConditionSpinnerType.NUMBER.getText()));
		this.add(new NumberSpinner(ConditionSpinnerType.NUMBER, 0, Main.GRIDHEIGHT));
		this.add(new JLabel(ConditionSpinnerType.ROW.getText()));
		this.add(new NumberSpinner(ConditionSpinnerType.ROW, 0, Main.GRIDHEIGHT - 1));
		this.add(new JLabel(ConditionSpinnerType.CONDITION_TYPE.getText()));
		this.add(new EnumSpinner(ConditionType.values()));	
		this.add(new JLabel("Player"));
		this.add(new EnumSpinner(new Object[]{"Player One", "Player Two"}));	
	}
}
