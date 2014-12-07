package assembler;

public enum AssemblerButtonType 
{
	ADD_RULE("Add Rule"), CHANGE_CONDITION("Change Condition"), SAVE("Save"), SAVE_AS("Save As"), LOAD("Load"), RESET("Reset");
	
	private String text;
	
	public String getText() {
		return text;
	}

	private AssemblerButtonType(String text)
	{
		this.text = text;
	}
}
