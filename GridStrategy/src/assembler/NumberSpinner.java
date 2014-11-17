package assembler;

import java.awt.event.ActionListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class NumberSpinner extends ConditionSpinner implements PanelControl
{
	public NumberSpinner(int min, int max, ControlType controlType, PanelType panelType, ChangeListener changeListener)
	{
		super(controlType, new SpinnerNumberModel(min, min, max, 1));
		this.addChangeListener(changeListener);
		this.controlType = controlType;
		this.panelType = panelType;
		this.setEnabled(false);
	}

	public int getNumber()
	{
		return ((SpinnerNumberModel) this.getModel()).getNumber().intValue();
	}
	
	private ControlType controlType;
	public ControlType getControlType() {
		return controlType;
	}

	private PanelType panelType;
	
	public PanelType getPanelType() {
		return panelType;
	}
}
