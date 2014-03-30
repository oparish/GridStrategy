package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import main.FileOperations;
import data.UnitType;

@SuppressWarnings("unchecked")
public abstract class Action
{	
	protected final int columnPos;

	public Action(int columnPos)
	{
		this.columnPos = columnPos;
	}
	
	public Action(ArrayList<Integer> integers)
	{
		this.columnPos = integers.get(Manufacturer.counter);
		Manufacturer.counter += 1;
	}
	
	public int getColumnPos() {
		return columnPos;
	}
	
	public abstract String toString();
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		Byte classNumber = FileOperations.intToByte(ActionType.DEPLOY_ACTION.ordinal());
		Byte columnNumber = FileOperations.intToByte(this.columnPos);
		bytes.add(classNumber);
		bytes.add(columnNumber);
		return bytes;
	}
}
