package assembler;

import javax.swing.JCheckBox;

public interface PanelControl {

	public PanelType getPanelType();
	public ControlType getControlType();
	public void setDirty(boolean value);
	public boolean isDirty();
	public void addCheckBox(JCheckBox checkbox);
	public JCheckBox getCheckBox();
	public void setEnabled(boolean value);
	public boolean isEnabled();
}
