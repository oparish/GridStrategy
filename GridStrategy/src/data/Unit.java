package data;

import java.awt.image.BufferedImage;

public class Unit
{
	private final boolean ownedByPlayer1;
	private boolean justDeployed = true;
	public boolean isJustDeployed() {
		return justDeployed;
	}

	public void setJustDeployed(boolean justDeployed) {
		this.justDeployed = justDeployed;
	}

	private final UnitType unitType;

	public Unit(boolean ownedByPlayer1, UnitType unitType)
	{
		this.ownedByPlayer1 = ownedByPlayer1;
		this.unitType = unitType;
	}
	
	public boolean isOwnedByPlayer1() {
		return ownedByPlayer1;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public BufferedImage getImage()
	{
		return this.unitType.getImage(ownedByPlayer1);
	}
	
	public String toString()
	{
		return "Unit Type: " + this.unitType.toString() + ", Player: " 
				+ (this.ownedByPlayer1 ? "1" : "2");
	}
	
	public static boolean match(Unit unit1, Unit unit2)
	{
		if (unit1 == null)
			return unit2 == null;
		else
			return (unit1.ownedByPlayer1 == unit2.ownedByPlayer1) &&
				(unit1.unitType == unit2.unitType);
	}
	
	public static boolean match(Unit unit1, UnitType unit2Type, Boolean unit2IsPlayer1)
	{
		if (unit2IsPlayer1 == null)
			return unit2Type == null || unit1.unitType == unit2Type;
		else
			return (unit1.ownedByPlayer1 == unit2IsPlayer1) && ((unit2Type == null) ||
				(unit1.unitType == unit2Type));
	}
}
