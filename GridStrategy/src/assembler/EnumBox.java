package assembler;

import java.awt.event.ActionListener;

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

	public EnumBox(T[] spinnerValues, ControlType controlType, PanelType panelType, ActionListener actionListener)
	{
		super(spinnerValues);
		this.controlType = controlType;
		this.panelType = panelType;
		this.addActionListener(actionListener);
		this.setEnabled(false);
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
	
	public boolean getDirty()
	{
		return this.dirty;
	}
}
