package assembler;

public enum AssemblerButtonType 
{
	ADD_RULE("Add Rule"), ADD_CONDITION("Add Condition"), SAVE("Save"), SAVE_AS("Save As"), LOAD("Load"), RESET("Reset");
	
	private String text;
	
	public String getText() {
		return text;
	}

	private AssemblerButtonType(String text)
	{
		this.text = text;
	}
}
