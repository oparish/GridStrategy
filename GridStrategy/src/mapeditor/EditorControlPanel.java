package mapeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import data.Terrain;

public class EditorControlPanel extends JPanel
{
	private JPanel[] playerPanels;
	
	public EditorControlPanel()
	{
		super();
		this.setLayout(new GridBagLayout());
		this.playerPanels = new JPanel[2];
		this.add(this.playerPanels[0] = new JPanel(), MapEditor.getExpandConstraints(0,3,1));
		this.add(this.playerPanels[1] = new JPanel(), MapEditor.getExpandConstraints(1,3,1));
	}
	
	public void addTerrainBox(MapEditor mapEditor, JComboBox<Terrain> terrainBox)
	{
		terrainBox.addActionListener(mapEditor);
		this.add(terrainBox, MapEditor.getGridBagConstraints(0,1,2));
		for (Terrain terrain : Terrain.values())
		{
			terrainBox.addItem(terrain);
		}
	}
	
	public void addFileControlPanel(JPanel fileControlPanel)
	{
		this.add(fileControlPanel, MapEditor.getGridBagConstraints(0, 0, 2));
	}
	
	public void addSpinnerPanel(JPanel spinnerPanel)
	{
		this.add(spinnerPanel, MapEditor.getGridBagConstraints(0, 2, 2));
	}
	
	public void addMapUnitsPanel(JPanel mapUnitsPanel, boolean player1)
	{
		this.playerPanels[player1?0:1].add(mapUnitsPanel, MapEditor.getGridBagConstraints(player1?0:1, 2, 2));
	}
}
