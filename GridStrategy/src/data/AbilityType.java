package data;

import main.Main;

public enum AbilityType {
	DEPLOYPOINT(null, null), ARTILLERY(null, null), SHIFTER(Main.GRIDWIDTH, Main.GRIDHEIGHT);
	
	private Integer input1Max;
	public Integer getInput1Max() {
		return input1Max;
	}


	public Integer getInput2Max() {
		return input2Max;
	}


	private Integer input2Max;


	AbilityType(Integer input1Max, Integer input2Max)
	{
		this.input1Max = input1Max;
		this.input2Max = input2Max;
	}
}
