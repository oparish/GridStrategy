package assembler;

public enum ControlType 
{
	COLUMN("Column"), UNIT_TYPE("Unit Type"), NUMBER("Number"), ROW("Row"), CONDITION_TYPE("Condition Type"), UNIT_PLAYER("Unit Player"), 
	ACTION_TYPE("Action Type"), GATE_TYPE("Gate Type"), FURTHER_INPUT("Further Input");
	
	String text;
	
	public String getText() {
		return text;
	}

	private ControlType(String text)
	{
		this.text = text;
	}
}
