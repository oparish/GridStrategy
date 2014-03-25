package buttons;

public enum ButtonType
{
	NEXT_TURN("Next Turn"), MAIN_MENU("Main Menu"), DEPLOY_UNIT("Deploy Unit"), 
	CANCEL("Cancel"), QUIT("Quit");
	
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private ButtonType(String text)
	{
		this.text = text;
	}
}
