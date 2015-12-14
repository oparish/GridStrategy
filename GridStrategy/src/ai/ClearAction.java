package ai;

import java.util.HashMap;
import java.util.List;

import main.Main;
import data.GameGrid;
import data.Unit;

public class ClearAction extends Action
{

	public ClearAction(HashMap<ActionFieldName, Integer> fields) {
		super(fields);
	}
	
	public ClearAction(int columnPos)
	{
		super(columnPos);
	}
	
	public ClearAction(List<Integer> integers)
	{
		super(integers);
	}

	@Override
	public String toString() 
	{
		return ("		Clear Action: " + this.getColumnPos());
	}

	@Override
	public int attemptAction(GameGrid gameGrid, boolean isPlayer1,
			int checkResult)
	{		
		int columnPos = this.getColumnPos();
		if (columnPos == Main.NO_SPECIFIC_COLUMN)
			columnPos = checkResult;
		
		int y = isPlayer1 ? gameGrid.getPlayer1DeploymentPoints()[columnPos] : gameGrid.getPlayer2DeploymentPoints()[columnPos];
		
		if (gameGrid.clearPoint(columnPos, y) != Main.GENERIC_CHECK_FAILURE)
			return columnPos;
		else if (columnPos == Main.NO_SPECIFIC_COLUMN)
			return CPlayer.TRY_NEW_COLUMN;
		else
			return Main.GENERIC_CHECK_FAILURE;
	}

}
