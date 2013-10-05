package events;

import data.Unit;

public class CombatEvent extends TwoUnitEvent
{
	private final CombatType combatType;
	private final CombatResult combatResult;
	
	public CombatResult getCombatResult() {
		return combatResult;
	}

	public CombatType getCombatType() {
		return combatType;
	}

	public CombatEvent(Object source, EventType type, int xPos, int yPos, 
			Unit unit1, Unit unit2, int xPos2, int yPos2, CombatType combatType,
			CombatResult combatResult)
	{
		super(source, type, xPos, yPos, unit1, xPos2, yPos2, unit2);
		this.combatType = combatType;
		this.combatResult = combatResult;
	}
}
