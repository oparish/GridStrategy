package data;

import static data.AbilityType.DEPLOYPOINT;
import static data.UnitCategory.FRONTLINE;
import static data.UnitCategory.INTERCEPTOR;
import static data.UnitCategory.LOW;
import static data.UnitCategory.SHIELD;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import main.Main;

public enum UnitType
{
	ARTILLERY("Player1Artillery", "Player1Artillery", "Player2Artillery", "Player2Artillery", 0, 0, 1,
			new UnitCategory[]{}, AbilityType.ARTILLERY),
	DEMOLISHER("UnitAnimation1", "UnitAnimation2", "UnitAnimation1", "UnitAnimation2", 3, 2, 5,
			new UnitCategory[]{}, null),
	INTERCEPTOR("Player1Interceptor", "Player1Interceptor", "Player2Interceptor", "Player2Interceptor",2, 1, 5,
			new UnitCategory[]{UnitCategory.INTERCEPTOR}, null),
	DEPLOYER("Player1Deployer", "Player1Deployer", "Player2Deployer", "Player2Deployer",1, 0, 1,
					new UnitCategory[]{}, DEPLOYPOINT),
	BUNKER("Player1Bunker", "Player1Bunker", "Player2Bunker", "Player2Bunker",0, 0, -1, new UnitCategory[]{FRONTLINE, LOW}, null),
	SPEEDER("Player1Speeder", "Player1Speeder", "Player2Speeder", "Player2Speeder",4, 1, 3, new UnitCategory[]{LOW}, null),
	BARRIER("Player1Barrier", "Player1Barrier", "Player2Barrier", "Player2Barrier",1, 1, 3, new UnitCategory[]{SHIELD}, null);
	
	private static ArrayList<UnitType> deployableUnitTypes;
	private static ArrayList<UnitType> activatableUnitTypes;
	private BufferedImage type1image1;
	private BufferedImage type1image2;
	private BufferedImage type2image1;
	private BufferedImage type2image2;
	private final int speed;
	private final int baseDamage;
	private final UnitCategory[] categories;
	private final AbilityType abilityType;
	private final int cost;
	
	static
	{
		UnitType.deployableUnitTypes = new ArrayList<UnitType>();
		for (UnitType unitType : UnitType.values())
		{
			if (unitType.cost != -1)
				UnitType.deployableUnitTypes.add(unitType);
		}
		
		UnitType.activatableUnitTypes = new ArrayList<UnitType>();
		for (UnitType unitType : UnitType.values())
		{
			if (unitType.abilityType != null)
				UnitType.activatableUnitTypes.add(unitType);
		}
	}
	
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
			return this.type1image1;
		else
			return this.type2image1;
	}
	
	public BufferedImage getImage2(boolean player1) {
		if (player1)
			return this.type1image2;
		else
			return this.type2image2;
	}
	
	private UnitType(String type1filename1, String type1filename2, String type2filename1, String type2filename2,int speed, 
			int baseDamage, int cost, UnitCategory[] categories, AbilityType abilityType)
	{
		this.type1image1 = Main.loadImage(type1filename1);
		this.type1image2 = Main.loadImage(type1filename2);
		this.type2image1 = Main.loadImage(type2filename1);
		this.type2image2 = Main.loadImage(type2filename2);
		this.speed = speed;
		this.baseDamage = baseDamage;
		this.categories = categories;
		this.abilityType = abilityType;
		this.cost = cost;
	}
	
	public static ArrayList<UnitType> getDeployableUnitTypes()
	{
		return UnitType.deployableUnitTypes;
	}
	
	public static ArrayList<UnitType> getActivatableUnitTypes()
	{
		return UnitType.activatableUnitTypes;
	}
}
