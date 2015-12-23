package assembler;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ai.Action;

public class ActionOptionsCellRenderer implements ListCellRenderer<Class<? extends Action>>
{
	@Override
	public Component getListCellRendererComponent(
			JList<? extends Class<? extends Action>> arg0, Class<? extends Action> actionClass, int index, boolean selected,
			boolean arg4)
	{
		String text = Action.getActionClassName(actionClass);
		if (selected)
		{
			return AssemblerList.getSelectedLabel(text);
		}
		else
		{
			return new JLabel(text);
		}
	}
}