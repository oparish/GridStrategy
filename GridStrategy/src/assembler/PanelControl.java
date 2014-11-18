package assembler;

public interface PanelControl {

	public PanelType getPanelType();
	public ControlType getControlType();
	public void setDirty(boolean value);
	public boolean getDirty();
}
