package assembler;

import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

public class EnumBox<T> extends JComboBox implements PanelControl
{
	private ControlType controlType;
	public ControlType getControlType() {
		return controlType;
	}

	private PanelType panelType;
	
	public PanelType getPanelType() {
		return panelType;
	}

	public EnumBox(T[] spinnerValues, ControlType controlType, PanelType panelType, ActionListener actionListener, boolean checkbox)
	{
		super(spinnerValues);
		this.controlType = controlType;
		this.panelType = panelType;
		this.addActionListener(actionListener);
		this.setEnabled(false);
		if (checkbox)
		{
			this.checkbox = new AssemblerCheckBox(this);
			this.checkbox.setEnabled(false);
		}
	}

	public void setEnumValue(T value)
	{
		this.setSelectedItem(value);
	}
	
	public T getEnumValue()
	{
		return (T) this.getSelectedItem();
	}

	private boolean dirty;
	
	@Override
	public void setDirty(boolean value)
	{
		this.dirty = value;		
	}
	
	public boolean isDirty()
	{
		return this.dirty;
	}

	private JCheckBox checkbox;
	
	@Override
	public void addCheckBox(JCheckBox checkbox)
	{
		this.checkbox = checkbox;
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
	
	public void setValue(T value)
	{
		if (value == null)
		{
			if (this.checkbox != null)
				this.checkbox.setSelected(false);
			this.setEnabled(false);
		}
		else
		{
			if (this.checkbox != null)
				this.checkbox.setSelected(true);
			this.setEnumValue(value);
			this.setEnabled(true);
		}
	}

	@Override
	public JCheckBox getCheckBox()
	{
		return this.checkbox;
	}
}
