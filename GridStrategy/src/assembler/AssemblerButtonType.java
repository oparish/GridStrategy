package assembler;

public enum AssemblerButtonType 
{
	MOVE_UP("Move Rule Up"), MOVE_DOWN("Move Rule Down"), ADD_RULE("Add Rule"), REMOVE_RULE("Remove Rule"), 
	CHANGE_CONDITION("Change Condition"), NEW_ACTION("New Action"), SAVE_ACTION("Save Action"),	RESET_ACTION("Reset Action"), REMOVE_ACTION("Remove Action"), RESET_CONDITION("Reset Condition"), SAVE_FILE("Save File"), 
	SAVE_FILE_AS("Save File As"), LOAD("Load"), RESET("Reset"), NEW_FILE("New File");
	
	private String text;
	
	public String getText() {
		return text;
	}

	private AssemblerButtonType(String text)
	{
		this.text = text;
	}
}
