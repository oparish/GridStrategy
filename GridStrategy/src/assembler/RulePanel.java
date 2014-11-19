package assembler;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;

import data.UnitType;
import ai.Action;
import ai.ActivateAction;
import ai.ColumnSearchCondition;
import ai.DeployAction;
import ai.Rule;

public class RulePanel extends JPanel
{	
	public RulePanel(Assembler assembler, ConditionPanel conditionPanel, ActionPanel actionPanel)
	{
		super();
		this.setLayout(new GridLayout(2,1));
		this.add(conditionPanel);
		this.add(actionPanel);
	}
}
