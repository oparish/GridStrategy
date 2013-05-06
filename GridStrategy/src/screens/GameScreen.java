package screens;

import static data.UnitType.TEST_UNIT;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import main.Main;

import panes.ControlPane;
import panes.GridPane;

@SuppressWarnings("serial")
public class GameScreen extends JFrame implements ActionListener
{
	private GridPane gridPane;
	private ControlPane controlPane;
	private int turnNumber;

	public GameScreen()
	{
		super();
		this.setLayout(new GridBagLayout());
		this.gridPane = new GridPane();
		this.controlPane = new ControlPane(this);
		this.add(this.gridPane, Main.getFillConstraints(0,0));
		this.add(this.controlPane, Main.getFillConstraints(1,0));
	}
	
	public GridPane getGridPane() {
		return gridPane;
	}

	public void setGridPane(GridPane gridPane) {
		this.gridPane = gridPane;
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{	
		if (this.turnNumber != 0)
		{
			this.gridPane.deleteCellContent(4, 4);
			this.gridPane.deleteCellContent(1, 5);
			this.gridPane.setCellContent(2, 2, TEST_UNIT);
			this.gridPane.setCellContent(3, 3, TEST_UNIT);
		}
		else
		{
			this.gridPane.setCellContent(4, 4, TEST_UNIT);
			this.gridPane.setCellContent(1, 5, TEST_UNIT);
		}

		this.gridPane.repaint();
		turnNumber++;
	}
}
