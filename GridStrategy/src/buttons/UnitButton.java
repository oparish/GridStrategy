package buttons;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import data.UnitType;

@SuppressWarnings("serial")
public class UnitButton	extends JButton
{
	private UnitType unitType;
	
	public UnitButton(UnitType unitType)
	{
		super(new ImageIcon(unitType.getImage(true)));
		this.unitType = unitType;
	}

	public UnitType getUnitType() {
		return unitType;
	}
}
