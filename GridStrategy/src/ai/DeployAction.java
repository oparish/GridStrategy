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
	}
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		Byte typeNumber = FileOperations.intToByte(this.unitType.ordinal());
		Byte columnNumber = FileOperations.intToByte(this.columnPos);
		Byte classNumber = FileOperations.intToByte(ActionType.DEPLOY_ACTION.ordinal());
		bytes.add(classNumber);
		bytes.add(typeNumber);
		bytes.add(columnNumber);
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
