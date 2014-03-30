package ai;

import java.util.ArrayList;

import main.FileOperations;
import data.UnitType;

public class DeployAction extends Action
{
	private final UnitType unitType;
	
	public DeployAction(int columnPos, UnitType unitType)
	{
		super(columnPos);
		this.unitType = unitType;
	}
	
	public DeployAction(ArrayList<Integer> integers)
	{
		super(integers);
		this.unitType = UnitType.values()[integers.get(Manufacturer.counter)];
		Manufacturer.counter += 1;
	}
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = super.toBytes();
		Byte typeNumber = FileOperations.intToByte(this.unitType.ordinal());
		bytes.add(typeNumber);
		return bytes;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public String toString()
	{
		return ("		Action: " + unitType.toString() + ", " + columnPos);
	}
}
