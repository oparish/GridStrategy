package assembler;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import main.Main;
import ai.ColumnCondition;
import ai.Rule;

public class AssemblerList<T> extends JList<T>
{
	private T[] contents;
	public T[] getContents() {
		return contents;
	}

	private final AssemblerListType type;
	
	public AssemblerListType getType() {
		return type;
	}

	public AssemblerList(T[] objects, AssemblerListType type)
	{
		super(objects);
		this.type = type;
		this.contents = objects;
	}
	
	public AssemblerList(AssemblerListType type)
	{
		super();
		this.type = type;
	}
	
	public static JLabel getSelectedLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setOpaque(true);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		return label;
	}
	
	@Override
	public void setListData(T[] listData)
	{
		super.setListData(listData);
		this.contents = listData;
	}
	
	public void insertBatch(ArrayList<T> batch, int batchCutoff, int listCutoff)
	{
		ArrayList<T> newItems = new ArrayList<T>();
		for (int j = 0; j < this.contents.length; j++)
		{
			if (j != listCutoff)
				newItems.add(this.contents[j]);
		}
		for (int i = 0; i < Main.GRIDWIDTH - 1; i++)
		{
			if (i == batchCutoff)
			{
				newItems.add(this.contents[listCutoff]);
			}
			T item = batch.get(i);
			newItems.add(item);
		}
		if (Main.GRIDWIDTH - 1 == batchCutoff)
		{
			newItems.add(this.contents[listCutoff]);
		}
		this.setListData((T[])newItems.toArray());
	}
}
