package ai;

import java.util.ArrayList;
import java.util.List;

import main.FileOperations;
import main.Main;
import data.Unit;
import data.UnitType;

public class ColumnCondition extends Condition
{	
	private final ConditionType conditionType;
	private final int number;
	private Integer column;
	private Unit unit;
	private Integer row;
	
	public Integer getRow() {
		return row;
	}

	public Integer getColumn() {
		return column;
	}
	public Unit getUnit() {
		return this.unit;
	}

	public int getNumber() {
		return number;
	}
	
	public ConditionType getConditionType() {
		return conditionType;
	}
	
	public void setColumn(Integer column) {
		this.column = column;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public ColumnCondition(ConditionType conditionType, int number)
	{
		this.conditionType = conditionType;
		this.number = number;
	}
	
	public ColumnCondition(List<Integer> integers, boolean player1)
	{
		this.conditionType = ConditionType.values()[integers.get(0)];
		this.number = integers.get(1);
		int columnNumber = integers.get(2);
		if (columnNumber != Main.GRIDWIDTH)
			this.column = columnNumber;
		int rowNumber = integers.get(3);
		if (rowNumber != Main.GRIDHEIGHT)
			this.row = rowNumber;
		int unitNumber = integers.get(4);
		if (unitNumber != UnitType.values().length)
		{
			boolean unitPlayer = integers.get(5) == 0 ? true : false;
			this.unit = new Unit(unitPlayer, UnitType.values()[unitNumber]);
		}
		
	}
	
	public String toString(int depth)
	{
		String unitString = (this.unit == null) ? null : "{" +
			this.unit.toString() + "}";
		return "		" + depth + " - Column Condition: " + this.conditionType + 
				", Number: " + this.number + ", Column: " + this.column + ", Unit: " + 
				unitString + ", Row: " + this.row;
	}

	@Override
	protected boolean runCheck(ObservationBatch observationBatch)
	{
		ArrayList<Unit> conditionUnits = this.findUnits(observationBatch);
		this.filterTypes(this.unit, conditionUnits);
		return this.checkNumber(observationBatch, conditionUnits);	
	}
	
	private boolean checkNumber(ObservationBatch observationBatch, 
			ArrayList<Unit> conditionUnits)
	{
		int unitNumber = conditionUnits.size();
		
		switch(this.conditionType)
		{
		case GREATER_THAN:
			return unitNumber > this.number;
		case SMALLER_THAN:
			return unitNumber < this.number;
		case EQUAL_TO:
			return unitNumber == this.number;
		default:
			return false;
		}
	}
	
	private void filterTypes(Unit filterType, ArrayList<Unit> conditionUnits)
	{
		if (filterType == null)
		{
			return;
		}
		else
		{
			ArrayList<Unit> removeList = new ArrayList<Unit>();
			for (Unit unit : conditionUnits)
			{
				if (!Unit.match(unit, filterType))
					removeList.add(unit);
			}
			for (Unit unit : removeList)
			{
				conditionUnits.remove(unit);
			}
		}
	}
	
	private void addUnitToList(ArrayList<Unit> units, Unit unit)
	{
		if (unit != null)
			units.add(unit);
	}
	
	private ArrayList<Unit> findUnits(ObservationBatch observationBatch)
	{
		ArrayList<Unit> conditionUnits;
		if (this.column == null && this.row == null)
		{
			conditionUnits = findUnitsOnGrid(observationBatch);
		}
		else if (this.column == null)
		{
			conditionUnits = findUnitsPastRow(observationBatch, this.row);
		}
		else if (this.row == null)
		{
			conditionUnits = findUnitsInColumn(observationBatch, this.column);
		}
		else
		{
			conditionUnits = findUnitsInColumnPastRow(observationBatch, 
					this.column, this.row);
		}
		return conditionUnits;
	}
	
	private ArrayList<Unit> findUnitsOnGrid(ObservationBatch observationBatch)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				this.addUnitToList(units, observationBatch.getUnits()[i][j]);
			}
		}
		return units;
	}
	
	private ArrayList<Unit> findUnitsPastRow(ObservationBatch observationBatch, 
			int row)
	{
		int rowNumber;
		if (!observationBatch.isPlayer1())
			rowNumber = Main.GRIDHEIGHT - 1 - this.getRow();
		else
			rowNumber = this.row;
		
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int i = 0; i < Main.GRIDWIDTH; i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT; j++)
			{
				if (j > rowNumber)
				{
					this.addUnitToList(units, observationBatch.getUnits()[i][j]);
				}
			}
		}
		return units;
	}
	
	private ArrayList<Unit> findUnitsInColumn(ObservationBatch observationBatch, 
			int column)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int j = 0; j < Main.GRIDHEIGHT; j++)
		{
			this.addUnitToList(units, observationBatch.getUnits()[column][j]);
		}
		return units;
	}
	
	private ArrayList<Unit> findUnitsInColumnPastRow
	(ObservationBatch observationBatch, int column, int row)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		for (int j = 0; j < Main.GRIDHEIGHT; j++)
		{
			if (j > row)
			{
				this.addUnitToList(units, observationBatch.getUnits()[column][j]);
			}
		}
		return units;
	}

	@Override
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		Byte conditionClass = FileOperations.intToByte(COLUMNCONDITIONTYPE);	
		Byte conditionType = FileOperations.intToByte(this.conditionType.ordinal());
		Byte conditionNumber = FileOperations.intToByte(this.number);
		Byte columnNumber;
		
		if (this.column != null)
			columnNumber =  FileOperations.intToByte(this.column);
		else
			columnNumber = FileOperations.intToByte(Main.GRIDWIDTH);
		
		Byte rowNumber;
		if (this.row != null)
			rowNumber =  FileOperations.intToByte(this.row);
		else
			rowNumber = FileOperations.intToByte(Main.GRIDHEIGHT);
		
		Byte unitType;
		Byte unitPlayer;
		if (this.unit != null)
		{
			unitType =  FileOperations.intToByte(this.unit.getUnitType().ordinal());
			unitPlayer = 
					FileOperations.intToByte(this.unit.isOwnedByPlayer1() ? 0 : 1);
		}
		else
		{
			unitType = FileOperations.intToByte(UnitType.values().length);
			unitPlayer = FileOperations.intToByte(2);
		}
			
		bytes.add(conditionClass);
		bytes.add(conditionType);
		bytes.add(conditionNumber);
		bytes.add(columnNumber);
		bytes.add(rowNumber);
		bytes.add(unitType);
		bytes.add(unitPlayer);
		
		return bytes;
	}
}
