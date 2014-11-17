package assembler;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ConditionSpinner extends JSpinner
{
	ControlType spinnerType;
	
	public ConditionSpinner(ControlType spinnerType, AbstractSpinnerModel abstractSpinnerModel)
	{
		super(abstractSpinnerModel);
		this.spinnerType = spinnerType;
	}
}
