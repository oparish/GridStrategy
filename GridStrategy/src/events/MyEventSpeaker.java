package events;

import java.util.ArrayList;

public class MyEventSpeaker 
{
	private ArrayList<MyEventListener> listeners = new ArrayList<MyEventListener>();
	
	 public synchronized void addEventListener(MyEventListener listener)
	 {
		 listeners.add(listener);
	 }
	 
	 public synchronized void removeEventListener(MyEventListener listener)
	 {
		listeners.remove(listener);
	 }

	  public synchronized void fireEvent(MyEvent event) 
	  {	  
	    for (MyEventListener listener : listeners)
	    {
	    	listener.receiveEvent(event);
	    }
	  }
}
