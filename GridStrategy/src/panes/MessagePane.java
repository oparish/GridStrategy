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
import events.EventBase;
import events.EventCell;
import events.EventLocation;
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
			if (Main.DEBUG && !Main.checkDebugColumns(((OneUnitEvent) event).getEventLocation().getColumn()))
				return;
		}
		
		EventType type = event.getType();

		if (Main.DEBUG && !Main.checkDebugEventType(type))
			return;
		
		EventLocation eventLocation = null;
		EventLocation eventLocation2 = null;
		String unit = null;
		String unit2 = null;
		Integer xPos = null;
		Integer xPos2 = null;
		String yPos = null;
		String yPos2 = null;
		
		if (event instanceof OneUnitEvent)
		{
			OneUnitEvent oneUnitEvent = (OneUnitEvent) event;
			eventLocation = oneUnitEvent.getEventLocation();
			unit = oneUnitEvent.getUnit().toString();
			xPos = eventLocation.getColumn();
			yPos = this.getYText(eventLocation);
		}
		
		if (event instanceof TwoPositionEvent)
		{
			TwoPositionEvent twoPositionEvent = (TwoPositionEvent) event;
			eventLocation2 = twoPositionEvent.getEventLocation2();
			xPos2 = eventLocation2.getColumn();	
			yPos2 = this.getYText(eventLocation2);
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
		case UNIT_CLEARED:
			this.add("Clearing " + unit + " at " + xPos + ", " + yPos);
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
		case SKIP_TURN:
			this.add("Turn Skipped");
			break;
		}
	}
	
	private String getYText(EventLocation eventLocation)
	{
		if (eventLocation instanceof EventBase)
		{
			EventBase eventBase = (EventBase) eventLocation;
			if (eventBase.isPlayer1())
				return "Player 1 Base";
			else
				return "Player 2 Base";
		}
		else
		{
			return String.valueOf(((EventCell) eventLocation).getRow());
		}
	}
	
	@Override
	public void add(String text)
	{
		super.add(text, 0);
	}

}
