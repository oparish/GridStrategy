package assembler;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

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
}
