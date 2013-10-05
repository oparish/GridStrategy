package ai;

import java.util.ArrayList;
import main.Main;
import data.Unit;

public class ObservationBatch
{
	private final boolean isPlayer1;
	public boolean isPlayer1() {
		return isPlayer1;
	}

	private final Unit[][] units;
	
	public Unit[][] getUnits() {
		return units;
	}

	public ObservationBatch(boolean isPlayer1, Unit[][] units)
	{
		this.isPlayer1 = isPlayer1;
		this.units = units;
	}
}
