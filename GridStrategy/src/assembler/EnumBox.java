package assembler;

import javax.swing.JComboBox;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

public class EnumBox<T> extends JComboBox
{

	public EnumBox(T[] spinnerValues)
	{
		super(spinnerValues);
	}

	public void setEnumValue(T value)
	{
		this.setSelectedItem(value);
	}
}
