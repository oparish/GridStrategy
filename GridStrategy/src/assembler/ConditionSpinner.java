package assembler;

import javax.swing.AbstractSpinnerModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ConditionSpinner extends JSpinner
{
	ConditionSpinnerType spinnerType;
	
	public ConditionSpinner(ConditionSpinnerType spinnerType, AbstractSpinnerModel abstractSpinnerModel)
	{
		super(abstractSpinnerModel);
		this.spinnerType = spinnerType;
	}
}
