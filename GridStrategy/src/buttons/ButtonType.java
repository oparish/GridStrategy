package buttons;

public enum ButtonType
{
	NEXT_TURN("Next Turn");
	
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
