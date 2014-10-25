package assembler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import ai.ActionType;
import ai.ColumnSearchCondition;
import data.UnitType;
import main.Main;

public class ActionPanel extends JPanel
{
	public ActionPanel(ChangeListener listener)
	{
		super();
		this.setLayout(new GridLayout(4, 2));
		this.add(new JLabel(ConditionSpinnerType.ACTION_TYPE.getText()));
		this.add(new EnumSpinner(ActionType.values()));
		this.add(new JLabel(ConditionSpinnerType.COLUMN.getText()));
		this.add(new NumberSpinner(ConditionSpinnerType.NUMBER, 0, Main.GRIDWIDTH));
		this.add(new JLabel(ConditionSpinnerType.UNIT_TYPE.getText()));
		this.add(new EnumSpinner(UnitType.values()));
		this.add(new JLabel(ConditionSpinnerType.CONDITION_TYPE.getText()));
		this.add(new EnumSpinner(ColumnSearchCondition.values()));
	}
}
