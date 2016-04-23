package assembler;

import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class NumberSpinner extends ConditionSpinner implements PanelControl
{
	public NumberSpinner(int min, int max, ControlType controlType, PanelType panelType, ChangeListener changeListener, boolean checkbox)
	{
		super(controlType, new SpinnerNumberModel(min, min, max, 1));
		this.addChangeListener(changeListener);
		this.controlType = controlType;
		this.panelType = panelType;
		this.setEnabled(false);
		if (checkbox)
		{
			this.checkbox = new AssemblerCheckBox(this);
			this.checkbox.setEnabled(false);
		}
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
	
	private boolean dirty;
	
	public void setDirty(boolean dirty)
	{
		this.dirty = dirty;
	}
	
	public boolean isDirty()
	{
		return this.dirty;
	}

	public void setValue(Integer value)
	{
		if (this.checkbox != null)
			this.checkbox.setEnabled(true);
		if (value == null)
		{
			if (this.checkbox != null)
				this.checkbox.setSelected(false);
			this.setEnabled(false);
		}
		else
		{
			super.setValue(value);
			if (this.checkbox != null)
				this.checkbox.setSelected(true);
			this.setEnabled(true);
		}
	}
	
	public void switchEnabled(boolean value)
	{
		this.setEnabled(value);
		if (this.checkbox != null)
		{
			if (value == false)
			{
				this.checkbox.setSelected(false);
			}
			this.checkbox.setEnabled(value);
		}
	}
	
	private JCheckBox checkbox;
	
	@Override
	public void addCheckBox(JCheckBox checkbox)
	{
		this.checkbox = checkbox;
	}
	
	@Override
	public JCheckBox getCheckBox()
	{
		return this.checkbox;
	}
}
