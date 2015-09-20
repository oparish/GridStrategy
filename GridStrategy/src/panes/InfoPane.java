package panes;

import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import data.GameGrid;
import main.Main;
import events.MyEvent;
import events.MyEventListener;
import events.OneUnitEvent;
import events.TurnEvent;

public class InfoPane extends JPanel implements MyEventListener
{
	private GameGrid gameGrid;
	private HPLabel player1HP;
	private HPLabel player2HP;
	private CreditLabel player1Credit;
	private CreditLabel player2Credit;
	
	public InfoPane(GameGrid gameGrid)
	{
		super();
		this.gameGrid = gameGrid;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.player1HP = new HPLabel(true, this.gameGrid.getPlayer1HP());
		this.player2HP = new HPLabel(false, this.gameGrid.getPlayer2HP());
		this.player1Credit = new CreditLabel(true, this.gameGrid.getPlayer1Credits());
		this.player2Credit = new CreditLabel(false, this.gameGrid.getPlayer2Credits());
		this.player1HP.setAlignmentX(CENTER_ALIGNMENT);
		this.player2HP.setAlignmentX(CENTER_ALIGNMENT);
		this.add(this.player1HP);
		this.add(this.player2HP);
		this.add(this.player1Credit);
		this.add(this.player2Credit);
		this.setBorder(new LineBorder(Color.BLACK));
	}

	@Override
	public void receiveEvent(MyEvent event)
	{
		switch(event.getType())
		{
			case UNITBASEATTACK:
				this.processBaseAttack((OneUnitEvent) event);
				break;
			case DEPLOYING_UNIT:
				OneUnitEvent oneUnitEvent = (OneUnitEvent) event;
				this.deployingUnit(oneUnitEvent);
				this.changeCredits(oneUnitEvent.getUnit().isOwnedByPlayer1());
				break;
			case CREDITS_CHANGE:
				this.changeCredits(((TurnEvent) event).isPlayer1());
				break;
			default:
		}
		
	}
	
	private void changeCredits(boolean isPlayer1)
	{
		if (isPlayer1)
			this.player1Credit.setSecondLabelText(this.gameGrid.getPlayer1Credits());
		else
			this.player2Credit.setSecondLabelText(this.gameGrid.getPlayer2Credits());
	}
	
	private void deployingUnit(OneUnitEvent event)
	{
		if (event.getUnit().isOwnedByPlayer1())
			this.player1Credit.setSecondLabelText(this.gameGrid.getPlayer1Credits());
		else
			this.player2Credit.setSecondLabelText(this.gameGrid.getPlayer2Credits());
	}
	
	private void processBaseAttack(OneUnitEvent event)
	{
		if (event.getUnit().isOwnedByPlayer1())
			this.player2HP.setSecondLabelText(this.gameGrid.getPlayer2HP());
		else
			this.player1HP.setSecondLabelText(this.gameGrid.getPlayer1HP());
	}
	
	private abstract class PlayerInfoLabel extends JPanel
	{
		protected static final String PLAYER = "Player ";
		protected static final String ONE = "One";
		protected static final String TWO = "Two";
		protected JLabel firstLabel;
		protected JLabel secondLabel;
		
		protected void setupLabels()
		{
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(firstLabel);
			this.add(Box.createHorizontalGlue());
			this.add(secondLabel);
		}
	}
	
	private class HPLabel extends PlayerInfoLabel
	{
		private static final String HP = " HP";		

		
		HPLabel(boolean player1, int hp)
		{
			this.firstLabel = new JLabel(PLAYER + (player1?ONE:TWO));
			this.secondLabel = new JLabel(hp + HP);
			this.setupLabels();
		}
		
		private void setSecondLabelText(int hp)
		{
			this.secondLabel.setText(hp + HP);
		}
	}
	
	private class CreditLabel extends PlayerInfoLabel
	{
		private static final String CREDITS = " CREDITS";		

		
		CreditLabel(boolean player1, int hp)
		{
			this.firstLabel = new JLabel(PLAYER + (player1?ONE:TWO));
			this.secondLabel = new JLabel(hp + CREDITS);
			this.setupLabels();
		}
		
		private void setSecondLabelText(int hp)
		{
			this.secondLabel.setText(hp + CREDITS);
		}
	}
}
