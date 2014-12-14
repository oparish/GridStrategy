package assembler;

public enum AssemblerButtonType 
{
	MOVE_UP("Move Rule Up"), MOVE_DOWN("Move Rule Down"), ADD_RULE("Add Rule"), CHANGE_CONDITION("Change Condition"), SAVE_ACTION("Save Action"), SAVE_CONDITION("Save Condition"), SAVE_FILE("Save File"), SAVE_FILE_AS("Save File As"), LOAD("Load"), RESET("Reset");
	
	private String text;
	
	public String getText() {
		return text;
	}

	private AssemblerButtonType(String text)
	{
		this.text = text;
	}
}
