package ai;

import java.util.ArrayList;

import main.FileOperations;

import data.UnitType;

public class Action
{
	private final int columnPos;
	private final UnitType unitType;

	public Action(int columnPos, UnitType unitType)
	{
		this.columnPos = columnPos;
		this.unitType = unitType;
	}
	
	public Action(ArrayList<Integer> integers)
	{
		this.columnPos = integers.get(Manufacturer.counter + 1);
		this.unitType = UnitType.getDeployableUnitTypes()[integers.get(Manufacturer.counter)];
	}
	
	public String toString()
	{
		return ("		Action: " + unitType.toString() + ", " + columnPos);
	}
	
	public int getColumnPos() {
		return columnPos;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		Byte typeNumber = FileOperations.intToByte(this.unitType.ordinal());
		Byte columnNumber = FileOperations.intToByte(this.columnPos);
		bytes.add(typeNumber);
		bytes.add(columnNumber);
		return bytes;
	}
}
