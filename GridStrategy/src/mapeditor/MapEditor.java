package mapeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.DataMap;
import data.Terrain;
import data.UnitType;
import main.FileOperations;
import main.Main;
import assembler.Assembler;

public class MapEditor extends JFrame implements MouseListener, ActionListener, ChangeListener
{
	private static final String CREDITS = "Credits";
	private static final String LIFE = "Life";
	
	MapPanel mapPanel;
	EditorControlPanel editorControlPanel;
	JComboBox<Terrain> terrainBox;
	JSpinner player1LifeSpinner;
	JSpinner player2LifeSpinner;
	JSpinner player1CreditSpinner;
	JSpinner player2CreditSpinner;
	MapUnitsPanel player1MapUnitsList;
	MapUnitsPanel player2MapUnitsList;
	
	JButton newButton;
	JButton saveButton;
	JButton saveAsButton;
	JButton loadButton;
	
	MapEditor()
	{
		super();
		this.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });
		this.mapPanel = new MapPanel(this);
		this.editorControlPanel = this.setupEditorControlPanel();
		
		this.setLayout(new GridLayout(1,2));
		this.add(this.mapPanel);
		this.add(this.editorControlPanel);
	}
	
	private JPanel setupFileControlPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.add(this.newButton = new JButton("New"));
		panel.add(this.saveButton = new JButton("Save"));
		panel.add(this.saveAsButton = new JButton("Save As"));
		panel.add(this.loadButton = new JButton("Load"));
		this.newButton.addActionListener(this);
		this.saveButton.addActionListener(this);
		this.saveAsButton.addActionListener(this);
		this.loadButton.addActionListener(this);
		return panel;
	}
	
	private EditorControlPanel setupEditorControlPanel()
	{
		EditorControlPanel editorControlPanel = new EditorControlPanel();
		editorControlPanel.addFileControlPanel(this.setupFileControlPanel());
		editorControlPanel.addTerrainBox(this, this.terrainBox = new JComboBox<Terrain>());
		editorControlPanel.addSpinnerPanel(this.makeSpinnerPanel());
		editorControlPanel.addMapUnitsPanel(this.player1MapUnitsList = new MapUnitsPanel(), true);
		editorControlPanel.addMapUnitsPanel(this.player2MapUnitsList = new MapUnitsPanel(), false);
		this.player1CreditSpinner.addChangeListener(this);
		this.player2CreditSpinner.addChangeListener(this);
		this.player1LifeSpinner.addChangeListener(this);
		this.player2LifeSpinner.addChangeListener(this);
		return editorControlPanel;
	}
	
	private JPanel makeSpinnerPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		panel.add(new JLabel(CREDITS));
		panel.add(this.player1CreditSpinner = new JSpinner());
		this.player1CreditSpinner.setValue(Main.PLAYER1_DEFAULTCREDITS);
		panel.add(new JLabel(LIFE));
		panel.add(this.player1LifeSpinner = new JSpinner());
		this.player1LifeSpinner.setValue(Main.PLAYER1_DEFAULTHP);
		panel.add(new JLabel(CREDITS));
		panel.add(this.player2CreditSpinner = new JSpinner());
		this.player2CreditSpinner.setValue(Main.PLAYER2_DEFAULTCREDITS);
		panel.add(new JLabel(LIFE));
		panel.add(this.player2LifeSpinner = new JSpinner());
		this.player2LifeSpinner.setValue(Main.PLAYER2_DEFAULTHP);
		return panel;
	}
	
	public static void main(String args[])
	{
		MapEditor mapEditor;
		mapEditor = new MapEditor();
		mapEditor.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mapEditor.setSize(screenSize.width, screenSize.height);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		int column = this.getColumnFromPosition(e.getX());
		int row = this.getRowFromPosition(e.getY());
		if (column >= 0 && column < Main.GRIDWIDTH && row >= 0 && row < Main.GRIDHEIGHT)
			this.mapPanel.updateCell(column, row, (Terrain) this.terrainBox.getSelectedItem());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	private int getColumnFromPosition(int x)
	{
		return (int) Math.floor((x - MapPanel.STARTX)/Main.CELLWIDTH);
	}
	
	private int getRowFromPosition(int y)
	{
		return (int) Math.floor((y - MapPanel.STARTY)/Main.CELLHEIGHT);
	}
	
	private void newMap()
	{
		this.player1CreditSpinner.setValue(0);
		this.player2CreditSpinner.setValue(0);
		this.player1LifeSpinner.setValue(0);
		this.player2LifeSpinner.setValue(0);
		this.player1MapUnitsList.clearList();
		this.player2MapUnitsList.clearList();
		this.mapPanel.clearMap();
	}
	
	private void saveMapData(boolean saveAs)
	{
		ArrayList<Integer> dataList = new ArrayList<Integer>();
		dataList.add((Integer) this.player1CreditSpinner.getValue());
		dataList.add((Integer) this.player2CreditSpinner.getValue());
		dataList.add((Integer) this.player1LifeSpinner.getValue());
		dataList.add((Integer) this.player2LifeSpinner.getValue());
		UnitType[] player1Types = this.player1MapUnitsList.getValues();
		dataList.add(player1Types.length);
		for (int i = 0; i < player1Types.length; i++)
		{
			dataList.add(player1Types[i].ordinal());
		}
		UnitType[] player2Types = this.player2MapUnitsList.getValues();
		dataList.add(player2Types.length);
		for (int i = 0; i < player2Types.length; i++)
		{
			dataList.add(player2Types[i].ordinal());
		}
		Terrain[][] gridTerrain = this.mapPanel.getGridTerrain();
		for (int i = 0; i < Main.GRIDWIDTH;i++)
		{
			for (int j = 0; j < Main.GRIDHEIGHT;j++)
			{
				dataList.add(gridTerrain[i][j].ordinal());
			}
		}
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		for (Integer data : dataList)
		{
			byteList.add(FileOperations.intToByte(data));
		}
		try {
			if (saveAs || !FileOperations.hasLastMap())
				FileOperations.saveMapAs(byteList, this);
			else
				FileOperations.saveMap(byteList, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadMap()
	{
		ArrayList<Integer> mapData;
		try {
			mapData = FileOperations.loadMap(this);
			this.loadMapData(mapData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void loadMapData(ArrayList<Integer> mapData)
	{
		DataMap map = new DataMap(mapData);
		player1CreditSpinner.setValue(map.getPlayer1Credits());
		player2CreditSpinner.setValue(map.getPlayer2Credits());
		player1LifeSpinner.setValue(map.getPlayer1InitialLife());
		player2LifeSpinner.setValue(map.getPlayer2InitialLife());
		ArrayList<UnitType> types1 = map.getPlayer1Types();
		UnitType[] data1 = new UnitType[types1.size()];
		ArrayList<UnitType> types2 = map.getPlayer2Types();
		UnitType[] data2 = new UnitType[types2.size()];
		this.player1MapUnitsList.setListData(types1.toArray(data1));
		this.player2MapUnitsList.setListData(types2.toArray(data2));
		this.mapPanel.setGridTerrain(map.getGridTerrain());
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.newButton)
		{
			this.newMap();
		}
		else if (ae.getSource() == this.saveButton)
		{
			this.saveMapData(false);
		}
		else if (ae.getSource() == this.saveAsButton)
		{
			this.saveMapData(true);
		}
		else if (ae.getSource() == this.loadButton)
		{
			this.loadMap();
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static GridBagConstraints getGridBagConstraints(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = width;
		c.gridy = y;
		c.gridx = x;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0;
		c.weightx = 0;
		return c;
	}
	
	public static GridBagConstraints getFillerConstraints(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = width;
		c.gridy = y;
		c.gridx = x;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 1;
		c.weightx = 1;
		return c;
	}
	
	public static GridBagConstraints getExpandConstraints(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = width;
		c.gridy = y;
		c.gridx = x;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 1;
		c.weightx = 1;
		return c;
	}
	
	public static GridBagConstraints getButtonConstraints(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = width;
		c.gridy = y;
		c.gridx = x;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 1;
		c.weightx = 1;
		return c;
	}
	
	public static GridBagConstraints getControlConstraints(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = width;
		c.gridy = y;
		c.gridx = x;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.weighty = 0;
		c.weightx = 1;
		return c;
	}
}
