package mapeditor;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import data.UnitType;

public class MapUnitsPanel extends JPanel implements ActionListener
{
	JComboBox<UnitType> unitBox;
	JList<UnitType> unitList;
	JButton addButton;
	JButton removeButton;
	
	public MapUnitsPanel()
	{
		super();
		this.setLayout(new GridBagLayout());
		this.add(this.unitBox = new JComboBox<UnitType>(), MapEditor.getControlConstraints(0, 0, 2));
		this.add(new JScrollPane(this.unitList = new JList<UnitType>()), MapEditor.getExpandConstraints(0, 1, 2));
		this.add(this.addButton = new JButton("Add"), MapEditor.getButtonConstraints(0, 2, 1));
		this.add(this.removeButton = new JButton("Remove"), MapEditor.getButtonConstraints(1, 2, 1));
		this.addButton.addActionListener(this);
		this.removeButton.addActionListener(this);
		this.unitList.setBorder(BorderFactory.createLineBorder(Color.black));
		this.unitList.setListData(UnitType.values());
	}
	
	public void clearList()
	{
		this.unitBox.removeAllItems();
		this.unitBox.setSelectedItem(null);
		this.unitList.setListData(UnitType.values());
	}
	
	public void setListData(UnitType[] data)
	{
		this.unitList.setListData(data);
		this.unitBox.removeAllItems();
		for (UnitType type : UnitType.values())
		{
			boolean check = true;
			for (int i = 0; i < data.length; i++)
			{
				if (type == data[i])
				{
					check = false;
					break;
				}
			}
			if (check)
				this.unitBox.addItem(type);
		}
	}
	
	public UnitType[] getValues()
	{
		ListModel<UnitType> unitModel = this.unitList.getModel();
		UnitType[] units = new UnitType[unitModel.getSize()];
		for (int i = 0; i < unitModel.getSize(); i++)
		{
			units[i] = unitModel.getElementAt(i);
		}
		return units;
	}

	private void removeType()
	{
		UnitType unitType;
		if ((unitType = this.unitList.getSelectedValue()) != null)
		{
			this.unitBox.addItem(unitType);
			ListModel<UnitType> unitModel = this.unitList.getModel();
			int newSize = unitModel.getSize() - 1;
			UnitType[] unitTypes = new UnitType[newSize];
			int i = 0;
			int j = 0;
			while (j < newSize)
			{
				UnitType listType = unitModel.getElementAt(i);
				if (unitType != listType)
				{
					unitTypes[j] = listType;
					j++;
				}
				i++;
			}
			this.unitList.setListData(unitTypes);
		}
	}
	
	private void addType()
	{
		UnitType newType = (UnitType) this.unitBox.getSelectedItem();
		ListModel<UnitType> unitModel = this.unitList.getModel();
		int modelSize = unitModel.getSize();
		UnitType[] unitTypes = new UnitType[modelSize + 1];
		for (int i = 0; i < modelSize; i++)
		{
			unitTypes[i] = unitModel.getElementAt(i);
		}
		unitTypes[modelSize] = newType;
		this.unitList.setListData(unitTypes);
		this.unitBox.removeItem(newType);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == this.addButton)
			this.addType();
		else if (ae.getSource() == this.removeButton)
			this.removeType();	
	}
}
