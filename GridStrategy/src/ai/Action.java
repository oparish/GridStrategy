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
		this.columnPos = integers.get(Manufacturer.counter + 1);
	}
	
	public int getColumnPos() {
		return columnPos;
	}
	
	public abstract String toString();
	
	public abstract ArrayList<Byte> toBytes();
}
