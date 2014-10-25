package buttons;

public enum ControlButtonType
{
	NEXT_TURN("Next Turn"), MAIN_MENU("Main Menu"), DEPLOY_UNIT("Deploy Unit"), ACTIVATE_ABILITY("Activate Ability"),
	CANCEL("Cancel"), QUIT("Quit");
	
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private ControlButtonType(String text)
	{
		this.text = text;
	}
}
