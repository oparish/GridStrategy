package dialogs;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import screens.GameScreen;
import buttons.LabelledButtonPanel;
import buttons.UnitButton;
import main.Main;
import data.UnitType;

@SuppressWarnings("serial")
public class UnitDialog extends MyDialog implements ActionListener
{
	private static final String CREDITS = "Credits";
	private static final int ROWLENGTH = 5;
	private static final String SELECTUNIT = "Select a Unit";
	
	private GameScreen gameScreen;
	
	public UnitDialog(GameScreen gameScreen)
	{
		super(gameScreen);
		this.gameScreen = gameScreen;
		this.setLayout(new GridBagLayout());
		this.setupButtons();
		this.setTitle(SELECTUNIT);
	}
	
	private void setupButtons()
	{
		int i = 0;
		int j = 0;
		for (UnitType unitType : this.gameScreen.getGameGrid().getAvailableUnitTypes(true))
		{
			UnitButton unitButton = new UnitButton(unitType);
			LabelledButtonPanel unitPanel = new LabelledButtonPanel(unitButton, this.getCostLabelText(unitType.getCost()));
			this.add(unitPanel, Main.getAnchoredConstraints(i, j));
			if (unitType.getCost() > this.gameScreen.getGameGrid().getPlayer1Credits())
				unitButton.setEnabled(false);
			unitButton.addActionListener(this);
			i++;
			if (i >= ROWLENGTH)
			{
				i = 0;
				j++;
			}
		}
	}
	
	private String getCostLabelText(int cost)
	{
		return cost + " " + CREDITS;
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		UnitButton unitButton = (UnitButton) source;
		this.gameScreen.readyToDeployUnit(unitButton.getUnitType());
		this.setVisible(false);
	}
}
