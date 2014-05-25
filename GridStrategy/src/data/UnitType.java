package data;

import static data.AbilityType.DEPLOYPOINT;
import static data.UnitCategory.FRONTLINE;
import static data.UnitCategory.INTERCEPTOR;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import main.Main;

public enum UnitType
{
	ARTILLERY("Player1Artillery", "Player2Artillery", 0, 0, 1,
			new UnitCategory[]{}, AbilityType.ARTILLERY),
	DEMOLISHER("Player1Demolisher", "Player2Demolisher", 3, 2, 1,
			new UnitCategory[]{}, null),
	INTERCEPTOR("Player1Interceptor", "Player2Interceptor", 2, 1, 5,
			new UnitCategory[]{UnitCategory.INTERCEPTOR}, null),
	DEPLOYER("Player1Deployer", "Player2Deployer", 1, 0, 1,
					new UnitCategory[]{}, DEPLOYPOINT),
	BUNKER("Player1Bunker", "Player2Bunker", 0, 0, -1, new UnitCategory[]{FRONTLINE}, null);
	
	private BufferedImage image1;
	private BufferedImage image2;
	private final int speed;
	private final int baseDamage;
	private final UnitCategory[] categories;
	private final AbilityType abilityType;
	private final int cost;
	
	public int getCost() {
		return cost;
	}

	public AbilityType getAbilityType() {
		return abilityType;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public int getSpeed() {
		return speed;
	}
	
	public boolean hasCategory(UnitCategory testCategory)
	{
		for (UnitCategory category : this.categories)
		{
			if (category == testCategory)
				return true;
		}
		return false;
	}

	public BufferedImage getImage(boolean player1) {
		if (player1)
			return this.image1;
		else
			return this.image2;
	}
	
	private UnitType(String filename1, String filename2, int speed, 
			int baseDamage, int cost, UnitCategory[] categories, AbilityType abilityType)
	{
		this.image1 = Main.loadImage(filename1);
		this.image2 = Main.loadImage(filename2);
		this.speed = speed;
		this.baseDamage = baseDamage;
		this.categories = categories;
		this.abilityType = abilityType;
		this.cost = cost;
	}
	
	public static UnitType[] getDeployableUnitTypes()
	{
		ArrayList<UnitType> unitTypes = new ArrayList<UnitType>();
		for (UnitType unitType : UnitType.values())
		{
			if (unitType.cost != -1)
				unitTypes.add(unitType);
		}
		UnitType[] unitTypeArray = new UnitType[unitTypes.size()];
		int i = 0;
		for(UnitType unitType : unitTypes)
		{
			unitTypeArray[i] = unitType;
			i++;
		}
		return unitTypeArray;
	}
}
