package panes;

import java.awt.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import data.Unit;
import main.Main;
import events.CombatEvent;
import events.EventType;
import events.MyEvent;
import events.MyEventListener;
import events.OneUnitEvent;
import events.TwoPositionEvent;
import events.TwoUnitEvent;

@SuppressWarnings("serial")
public class MessagePane extends List implements MyEventListener
{
	
	public MessagePane()
	{
		super();
	}

	@Override
	public void receiveEvent(MyEvent event) {
		if (event instanceof OneUnitEvent)
		{
			if (Main.DEBUG && !Main.checkDebugColumns(((OneUnitEvent) event).getXpos1()))
				return;
		}
		
		EventType type = event.getType();

		if (Main.DEBUG && !Main.checkDebugEventType(type))
			return;
		
		Integer xPos = null;
		Integer yPos = null;
		Integer xPos2 = null;
		Integer yPos2 = null;
		String unit = null;
		String unit2 = null;
		
		if (event instanceof OneUnitEvent)
		{
			OneUnitEvent oneUnitEvent = (OneUnitEvent) event;
			xPos = oneUnitEvent.getXpos1();
			yPos = oneUnitEvent.getYPos1();
			unit = oneUnitEvent.getUnit().toString();
		}
		
		if (event instanceof TwoPositionEvent)
		{
			TwoPositionEvent twoPositionEvent = (TwoPositionEvent) event;
			xPos2 = twoPositionEvent.getXPos2();
			yPos2 = twoPositionEvent.getYPos2();
		}

		if (event instanceof TwoUnitEvent)
		{
			TwoUnitEvent twoUnitEvent = (TwoUnitEvent) event;
			unit2 = twoUnitEvent.getUnit2().toString();
		}
		
			
		switch(type)
		{
		case NEXT_TURN:
			this.add("Next Turn");
			break;
		case NEW_TURN:
			this.add("New Turn");
			break;
		case DEPLOYING_UNIT:
			this.add("Deploying " + unit + " at " + xPos + ", " + yPos);
			break;
		case MOVING_UNIT:
			this.add("Moving " + unit + " from " + xPos + ", " + yPos + " to "+ xPos2 + ", " + yPos2);
			break;
		case PLACE_DEPLOY_POINT:
			this.add("Placing deploy point at " + xPos + ", " + yPos);
			break;
		case COMBAT:
			this.add("Combat between " + unit + " at " + xPos + ", " + yPos + " and " + unit2 + " at "+ xPos2 + ", " + yPos2);
			break;
		case UNITBASEATTACK:
			this.add("Base attacked from " + xPos + ", " + yPos);
			break;
		}
	}

}
