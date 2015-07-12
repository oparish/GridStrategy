package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ai.Condition.ConditionFieldName;
import ai.headers.ConditionHeader;
import ai.headers.GateConditionHeader;
import main.FileOperations;
import main.Main;

public class GateCondition extends Condition
{	
	private final static ConditionFieldName[] conditionFieldNames = {ConditionFieldName.GATETYPE};
	private Condition condition1;
	public void setCondition1(Condition condition1) {
		this.condition1 = condition1;
	}

	public void setCondition2(Condition condition2) {
		this.condition2 = condition2;
	}

	public Condition getCondition1() {
		return condition1;
	}

	private Condition condition2;
	
	public Condition getCondition2() {
		return condition2;
	}

	public GateCondition(Condition condition1, Condition condition2, 
			GateType gateType, boolean isPlayer1)
	{
		super(isPlayer1);
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.setGateType(gateType);
	}
	
	public GateCondition(List<Integer> conditionIntegers, boolean isPlayer1, 
			GateConditionHeader conditionHeader)
	{
		super(conditionIntegers, isPlayer1, conditionHeader);
		
		ConditionHeader condition1Header = conditionHeader.condition1;
		ConditionHeader condition2Header = conditionHeader.condition2;
		this.condition1 = Condition.makeCondition(conditionIntegers.subList(0, condition1Header.getSize()), isPlayer1, condition1Header);
		this.condition2 = Condition.makeCondition(conditionIntegers.subList(condition1Header.getSize(), 
				condition1Header.getSize() + condition2Header.getSize()), isPlayer1, condition1Header);
	}
	
	public GateCondition(HashMap<ConditionFieldName, Integer> fieldMap, boolean isPlayer1)
	{
		super(fieldMap, isPlayer1);
	}
	
	public GateType getGateType()
	{
		int value = this.conditionFields.get(ConditionFieldName.GATETYPE);
		return GateType.values()[value];
	}
	
	public void setGateType(GateType gateType)
	{
		this.conditionFields.put(ConditionFieldName.GATETYPE, gateType.ordinal());
	}
	
	public String toString(int depth)
	{
		return "		" + depth + " - Gate Condition: " + this.getGateType() + "\n" + 
				this.condition1.toString(depth + 1) + "\n" + 
						this.condition2.toString(depth + 1);
	}
	
	public GateCondition clone()
	{
		GateCondition gateCondition = (GateCondition) super.clone();
		gateCondition.setCondition1(this.condition1.clone());
		gateCondition.setCondition2(this.condition2.clone());
		return gateCondition;
	}
	
	protected int runCheck(ObservationBatch observationBatch, Action action)
	{
		int result1 = this.condition1.checkCondition(observationBatch, action);
		int result2 = this.condition2.checkCondition(observationBatch, action);
		
		switch(this.getGateType())
		{
		case AND:
			return (result1 != Main.GENERIC_CHECK_FAILURE && result2 != Main.GENERIC_CHECK_FAILURE ) ? Main.GENERIC_CHECK_SUCCESS : Main.GENERIC_CHECK_FAILURE;
		case OR:
			return (result1 != Main.GENERIC_CHECK_FAILURE || result2 != Main.GENERIC_CHECK_FAILURE ) ? Main.GENERIC_CHECK_SUCCESS : Main.GENERIC_CHECK_FAILURE;
		case NAND:
			return !(result1 != Main.GENERIC_CHECK_FAILURE  && result2 != Main.GENERIC_CHECK_FAILURE ) ? Main.GENERIC_CHECK_SUCCESS : Main.GENERIC_CHECK_FAILURE;
		case NOR:
			return !(result1 != Main.GENERIC_CHECK_FAILURE  || result2 != Main.GENERIC_CHECK_FAILURE ) ? Main.GENERIC_CHECK_SUCCESS : Main.GENERIC_CHECK_FAILURE;
		default:
			return Main.GENERIC_CHECK_FAILURE;
		}
	}
	
	public ArrayList<Byte> toBytes()
	{
		ArrayList<Byte> bytes = super.toBytes();
		bytes.addAll(this.condition1.toBytes());
		bytes.addAll(this.condition2.toBytes());
		return bytes;
	}
	
	public ArrayList<Byte> getHeaderBytes()
	{
		ArrayList<Byte> headerByteList = super.getHeaderBytes();
		headerByteList.addAll(this.condition1.getHeaderBytes());
		headerByteList.addAll(this.condition2.getHeaderBytes());
		return headerByteList;
	}
	
}
