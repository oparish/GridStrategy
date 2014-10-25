package assembler;

public enum ConditionSpinnerType 
{
	COLUMN("Column"), UNIT_TYPE("Unit Type"), NUMBER("Number"), ROW("Row"), CONDITION_TYPE("Condition Type"), UNIT_PLAYER("Unit Player"), 
	ACTION_TYPE("Action Type");
	
	String text;
	
	public String getText() {
		return text;
	}

	private ConditionSpinnerType(String text)
	{
		this.text = text;
	}
}
