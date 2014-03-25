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

public class InfoPane extends JPanel implements MyEventListener
{
	private GameGrid gameGrid;
	private HPLabel player1HP;
	private HPLabel player2HP;
	
	
	public InfoPane(GameGrid gameGrid)
	{
		super();
		this.gameGrid = gameGrid;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.player1HP = new HPLabel(true, Main.PLAYER1_MAXHP);
		this.player2HP = new HPLabel(false, Main.PLAYER2_MAXHP);
		this.player1HP.setAlignmentX(CENTER_ALIGNMENT);
		this.player2HP.setAlignmentX(CENTER_ALIGNMENT);
		this.add(this.player1HP);
		this.add(this.player2HP);
		this.setBorder(new LineBorder(Color.BLACK));
	}

	@Override
	public void receiveEvent(MyEvent event)
	{
		switch(event.getType())
		{
			case UNITBASEATTACK:
				this.processBaseAttack((OneUnitEvent) event);
			default:
		}
		
	}
	
	private void processBaseAttack(OneUnitEvent event)
	{
		if (event.getUnit().isOwnedByPlayer1())
			this.player2HP.setSecondLabelText(this.gameGrid.getPlayer2HP());
		else
			this.player1HP.setSecondLabelText(this.gameGrid.getPlayer1HP());
	}
	
	private class HPLabel extends JPanel
	{
		private static final String PLAYER = "Player ";
		private static final String HP = " HP";
		private static final String ONE = "One";
		private static final String TWO = "Two";
		
		private JLabel firstLabel;
		private JLabel secondLabel;
		
		HPLabel(boolean player1, int hp)
		{
			super();
			this.firstLabel = new JLabel(PLAYER + (player1?ONE:TWO));
			this.secondLabel = new JLabel(hp + HP);
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(firstLabel);
			this.add(Box.createHorizontalGlue());
			this.add(secondLabel);
		}
		
		private void setSecondLabelText(int hp)
		{
			this.secondLabel.setText(hp + HP);
		}
	}
}
