package ai;

import java.util.ArrayList;

import main.FileOperations;
import data.UnitType;

public class ActivateAction extends DeployAction
{
	private ColumnSearchCondition columnSearchCondition;
	
	public ActivateAction(int columnPos, UnitType unitType, ColumnSearchCondition columnSearchCondition)
	{
		super(columnPos, unitType);
		this.columnSearchCondition = columnSearchCondition;
	}
	
	public ActivateAction(ArrayList<Integer> integers)
	{
		super(integers);
		this.columnSearchCondition = ColumnSearchCondition.values()[integers.get(Manufacturer.counter)];
		Manufacturer.counter += 1;
	}
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = super.toBytes();
		Byte searchNumber = FileOperations.intToByte(this.columnSearchCondition.ordinal());
		bytes.add(searchNumber);
		return bytes;
	}
	
	public String toString()
	{
		return super.toString() + ", " + this.columnSearchCondition.name();
	}

}
