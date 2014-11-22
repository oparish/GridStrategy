package assembler;

import javax.swing.JCheckBox;

public interface PanelControl {

	public PanelType getPanelType();
	public ControlType getControlType();
	public void setDirty(boolean value);
	public boolean getDirty();
	public void addCheckBox(JCheckBox checkbox);
}
