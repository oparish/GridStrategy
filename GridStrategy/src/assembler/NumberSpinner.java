package assembler;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class NumberSpinner extends ConditionSpinner
{
	public NumberSpinner(ConditionSpinnerType spinnerType, int min, int max)
	{
		super(spinnerType, new SpinnerNumberModel(min, min, max, 1));
	}
}
