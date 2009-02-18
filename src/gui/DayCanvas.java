package gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import subsystem.Event;
import subsystem.Period;
import subsystem.SimpleTime;

/**
 * A DayCanvas is a canvas which shows you the events for the current day.
 * 
 * @author dsb3573 (Dana Burkart)
 * @author rxm6930 (Robert Middleton) 
 * 
 */
@SuppressWarnings("serial")
public class DayCanvas extends JPanel implements MouseListener, 
MouseMotionListener, MouseWheelListener, ComponentListener{
	private boolean showTimeField;
	private int offset = 50;
	private CakeGUI parent;
	private SimpleTime start, end;
	private boolean draggingAnEvent = false, dragScrolling = false;
	private Point dragStart, dragEnd;
	private EventShape draggedEvent = null;//, selectedEvent = null;
	private boolean rightButtonPressed = false;

	private ArrayList<EventShape> events;
	
	private Period today;
	private boolean inWeek;

	/**
	 * Constructor.  Assumes that you want to show the time
	 * 
	 * @param parent The CakeGUI that is this DayCanvas' parent.
	 */
	public DayCanvas(CakeGUI parent) {
		setDay(Period.parse(parent.currentYear + "." + parent.currentMonth + "." +
				parent.currentDay + ":00.00-" + parent.currentYear + "." +
				parent.currentMonth + "." + parent.currentDay + ":24.00"));
		inWeek = false;
		initialize(true, parent);
	}

	/**
	 * Constructor
	 * 
	 * @param showTime true if you want to show the time, else false
	 * @param parent The CakeGUI which is this view's parent
	 */
	public DayCanvas(boolean showTime, CakeGUI parent) {
		setDay(Period.parse(parent.currentYear + "." + parent.currentMonth + "." +
				parent.currentDay + ":00.00-" + parent.currentYear + "." +
				parent.currentMonth + "." + parent.currentDay + ":24.00"));
		inWeek = false;
		initialize(showTime, parent);
	}
	
	public DayCanvas(boolean showTime, CakeGUI parent, Period p){
		setDay( new Period(p) );
		inWeek = true;
		//System.out.println("Initializing to "+p.format());
		initialize(showTime,parent);
	}

	/**
	 * Called by the constructors to initialize the dayCanvas
	 * 
	 * @param showTime true if you want to show the time, false otherwise.
	 * @param parent the CakeGUI which is this DayCanvas' parent
	 */
	private void initialize(boolean showTime, CakeGUI parent) {
		events = new ArrayList<EventShape>();
		this.start = new SimpleTime(6, 0);
		this.end = new SimpleTime(18, 0);
		this.showTimeField = showTime;
		this.parent = parent;
		this.updateDataSet();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addComponentListener(this);
		this.setFocusable(true);
		
	}

	/**
	 * Paint this component.
	 * 
	 */
	public void paint(Graphics g) {
		//System.out.println("paint");
		this.paintComponent(g);
		Graphics2D g2;
		g2 = (Graphics2D) g;

		this.setBackground(Color.white);
		drawTimeSlots(g2);
		drawEvents(g2);
	}
	
	/**
	 * Paint this component.
	 * 
	 */
	public void paintComponent(Graphics g){
		//this.paint(g);
		super.paintComponent(g);
		//System.out.println("paintComp");
		Graphics2D g2;
		g2 = (Graphics2D) g;

		this.setBackground(Color.white);
		drawTimeSlots(g2);
		drawEvents(g2);
	}

	/**
	 * Render this component
	 * 
	 */
	public void render() {
		updateDataSet();
		updateBounds();
		if (parent.selectedEvent != null) {
			int i;
			for (i = 0; i < events.size(); i++) {
				if (parent.selectedEvent.event.getUID() == events.get(i).event.getUID()) {
					parent.selectedEvent = events.get(i);
					parent.selectedEvent.selected = true;
					parent.selectedEvent.redrawImage();
					break;
				}
			}
			if (i == events.size()) parent.selectedEvent = null;
		}
		
		repaint();
	}

	/**
	 * Draws the time slots on the canvas
	 * 
	 * @param g2 the Graphics2D that should be used to draw.
	 */
	private void drawTimeSlots(Graphics2D g2) {
		int offset = 0;
		if (showTimeField) offset = this.offset;
		if (inWeek && (today.start.date.format().equals(parent.getCurrentDate().format()))) {
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(offset, 0, this.getWidth(), this.getHeight());
		}
		g2.setColor(Color.GRAY);
		int hours = Math.abs(start.hour - end.hour)+1;
		if (showTimeField) g2.drawLine(offset, 0, offset, this.getHeight());
		for (int i = 0; i < hours; i++) {
			int in = i*(this.getHeight()/hours);
			
			g2.drawLine(offset, in, this.getWidth(), in);
			if (showTimeField){
				if( start.hour + i > 12 ){
					g2.drawString(start.hour-12+i + ":00", offset/4, 
						in + (this.getHeight()/hours)/2);
				}else{				
				g2.drawString(start.hour+i + ":00", offset/4, 
						in + (this.getHeight()/hours)/2);
				}
			}
		}
		g2.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight());
	}

	/**
	 * Draw the events on the day view
	 * 
	 * @param g2 the Graphics2D to use to draw.
	 */
	private void drawEvents(Graphics2D g2) {
		for (int i = 0; i < events.size(); i++) {
			events.get(i).DrawOn(g2);
		}
	}

	/**
	 * Get the number of conflicts out of this event shape
	 * 
	 * @param e The EventShape to get the out conflicts of.
	 * @return an array list of EventShapes which conflict with this EventShape
	 * 
	 * TODO: May have to account for minutes
	 */
	private ArrayList<EventShape> conflictsOut(EventShape e) {
		ArrayList<EventShape> c = new ArrayList<EventShape>();

		int etime = e.event.getPeriod().start.time.hour * 100 + 
		e.event.getPeriod().start.time.minutes;

		for (int i = 0; i < events.size(); i++) {
			int ftimea = events.get(i).event.getPeriod().start.time.hour * 100 +
			events.get(i).event.getPeriod().start.time.minutes;
			int ftimeb = events.get(i).event.getPeriod().end.time.hour * 100 +
			events.get(i).event.getPeriod().end.time.minutes;
			if (etime >= ftimea && etime < ftimeb && !e.event.getTitle().equals("")) c.add(events.get(i));
		}

		return c;
	}

	/**
	 * Gets the total number of conflicts for the specified event shape
	 * 
	 * @param e The EventShape to get the total number of conflicts for.
	 * @return The total number of conflicts on this event in an array list.
	 * 
	 * TODO: May have to account for minutes
	 */
	private ArrayList<EventShape> conflictsTotal(EventShape e) {
		ArrayList<EventShape> c = new ArrayList<EventShape>();

		int etimea = e.event.getPeriod().start.time.hour * 100 +
		e.event.getPeriod().start.time.minutes;
		int etimeb = e.event.getPeriod().end.time.hour * 100 +
		e.event.getPeriod().end.time.minutes;

		for (int i = 0; i < events.size(); i++) {
			int ftimea = events.get(i).event.getPeriod().start.time.hour * 100 +
			events.get(i).event.getPeriod().start.time.minutes;
			int ftimeb = events.get(i).event.getPeriod().end.time.hour * 100 +
			events.get(i).event.getPeriod().end.time.minutes;
			if ((etimea >= ftimea && etimea < ftimeb) ||
					(ftimea >= etimea && ftimea < etimeb)) c.add(events.get(i));
		}

		return c;
	}

	/**
	 * Gets a new data set to render.
	 * 
	 */
	private void updateDataSet() {
		//if()
		events.clear();// = new ArrayList<EventShape>();
		ArrayList<Event> temp = new ArrayList<Event>();
		try {
			if( !inWeek ){
			setDay(Period.parse(parent.currentYear + "." + parent.currentMonth + "." +
					parent.currentDay + ":00.00-" + parent.currentYear + "." +
					parent.currentMonth + "." + parent.currentDay + ":24.00"));
			}
			temp = (ArrayList<Event>) parent.getEvents(getDay());
			
			//System.err.println(p.format());
			
			if( events.size() != 0){
				parent.selectedEvent = events.get(0);
				parent.selectedEvent.selected = true;
				parent.selectedEvent.redrawImage();
			}
		} catch(Exception e) {
			System.err.println("Error Occured when attempting to get new events:\n" + e.getMessage() + "\nStack Trace:\n" );
			e.printStackTrace();
			System.err.println();
		}

		try {
			for (int i=0; i < temp.size(); i++) {
				events.add(new EventShape(temp.get(i)));
			}
		} catch (NullPointerException s) {
			System.out.println("In updateDataSet()");
			System.out.println(s.getMessage());
		}
	}

	/**
	 * Update the bounds of the events
	 * 
	 * TODO: Account for minutes
	 */
	private void updateBounds() {
		try {
			int offset = 0;
			if (showTimeField) offset = this.offset;
			int hours = Math.abs(start.hour - end.hour)+1;

			for (int i = 0; i < events.size(); i++){ 
				events.get(i).setBounds(new Rectangle(0,0,0,0));
			}

			//-- Pass 1
			//System.out.println("Start of pass 1");
			for (int i = 0; i < events.size(); i++) {
				Event e = events.get(i).event;
				int conflicts = this.conflictsOut(events.get(i)).size();
				events.get(i).setBounds(new Rectangle(
						0, 0, 
						(this.getWidth()-offset)/conflicts, 
						(int) ((double)(this.getHeight()/hours)*((double)(e.getPeriod().end.time.hour - e.getPeriod().start.time.hour)+(double)(e.getPeriod().end.time.minutes/60)))));
				//System.out.println(events.get(i));
				events.get(i).weight = conflictsTotal(events.get(i)).size();
			}
			sortEventsByWeight(events);
			//System.out.println("End of pass 1");

			//-- Pass 2
			for (int i = 0; i < events.size(); i++) {
				EventShape e = events.get(i);
				ArrayList<EventShape> conflicts = conflictsTotal(e);
				int conflictsO = conflictsOut(e).size();
				int reAgain = 0;

				int tWidth = 0;
				int n = 0, m = 0, k = 0;
				for (int j = 0; j < conflicts.size(); j++) {
					if (sameSlot(e, conflicts.get(j)) != true) {
						if (e.weight <= conflicts.get(j).weight) {
							tWidth += conflicts.get(j).getBounds().width;	
							m++;
						} else {
							if ((conflictsO-1) < (conflicts.size()-conflictsO)) {
								reAgain = (this.getWidth()-offset+1)/(conflicts.size()-conflictsO+1);
							}
						}
					} else if (sameSlot(e, conflicts.get(j))) {
						n++;
						if (e.event.getUID() == conflicts.get(j).event.getUID()) {
							k = j;
						}
					}
				}
				int w = (this.getWidth()-(offset+1)-tWidth)/n;
				int x = 1+offset+tWidth+(w*(k-m));
				int y = (e.event.getPeriod().start.time.hour - start.hour)*(this.getHeight()/hours);
				if (reAgain != 0) w = reAgain;
				e.setBounds(new Rectangle(x, y, w, e.getBounds().height));
			}
		} catch (Exception s) {
				System.out.println(s.getMessage());
		}
	}

	/**
	 * Checks to see if the two paramaters take up the same spot
	 * 
	 * @param a The first EventShape to check
	 * @param b The second EventShape to check
	 * @return true if they both conflict, false otherwise
	 */
	private boolean sameSlot(EventShape a, EventShape b) {
		return (a.event.getPeriod().start.time.hour == b.event.getPeriod().start.time.hour);
	}

	/**
	 * Sorts the events by their weight(the total number of conflicts
	 * 
	 * @param a The array list of events to sort by weight.
	 * 
	 * TODO: Account for minutes
	 */
	private void sortEventsByWeight(ArrayList<EventShape> a) {
		for (int i = 0; i < a.size()-1; i++) {
			for (int j = 0; j < a.size()-1-i; j++) {
				if (a.get(j+1).weight > a.get(j).weight) {
					EventShape e = a.get(j);
					a.set(j, a.get(j+1));
					a.set(j+1, e);
				} else if (a.get(j+1).weight == a.get(j).weight) {
					if (a.get(j+1).event.getPeriod().start.time.hour < a.get(j).event.getPeriod().start.time.hour) {
						EventShape e = a.get(j);
						a.set(j, a.get(j+1));
						a.set(j+1, e);
					} else if (a.get(j+1).event.getPeriod().start.time.hour == a.get(j).event.getPeriod().start.time.hour)
						if (a.get(j+1).event.getUID() < a.get(j).event.getUID()) {
							EventShape e = a.get(j);
							a.set(j, a.get(j+1));
							a.set(j+1, e);
						} 
				}
			}
		}
	}

	/**
	 * Called when the mouse is clicked
	 * 
	 */
	public void mouseClicked(MouseEvent e) {
		select(e);
		
		parent.highlightToday(parent.currentDay = today.start.date.day);
		parent.updateMonth(parent.currentMonth = today.start.date.month);
		parent.updateYear(parent.currentYear = today.start.date.year);
		
		if (e.getClickCount() == 2) {
			parent.viewChanger.show(parent.center, parent.DAY);
			parent.dayView.updateDay();
		}
		
		
	}

	/**
	 * Called when the mouse is pressed
	 * 
	 */
	public void mousePressed(MouseEvent e) {
		dragStart = e.getPoint();
		if (e.getButton() == MouseEvent.BUTTON1) {
				draggingAnEvent = true;
				select(e);
				draggedEvent = parent.selectedEvent;
		} else if (e.getButton() == MouseEvent.BUTTON3) rightButtonPressed = true;
		this.repaint();
	}

	/**
	 * Called when the mouse is released.
	 * 
	 */
	public void mouseReleased(MouseEvent e) {
		dragEnd = e.getPoint();
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (draggingAnEvent && draggedEvent != null) {
				int dy = dragEnd.y - dragStart.y;

				int hourSize = this.getHeight()/Math.abs(start.hour - end.hour)+1;
				dy = (int) Math.floor(dy/hourSize);
				if (dy != 0) {
					try {
						draggedEvent.event.getPeriod().start.time.hour += dy;
						draggedEvent.event.getPeriod().end.time.hour += dy;
						parent.updateEvent(draggedEvent.event);
						updateDataSet();
						update();
					} catch (Exception x) {
						System.err.println(x.getMessage());
					}
				}
				this.updateBounds();
				select(e);
			}
			draggingAnEvent = false;
			draggedEvent = null;
		} else if (e.getButton() == MouseEvent.BUTTON3) rightButtonPressed = false;
	}

	/**Called when the mouse wheel moves
	 * 
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (inWeek) {
			parent.weekView.Scroll(e, getDay().start.date.day);
		}
		zoomScroll(e);
	}

	/**Called when the mouse is dragged.  ignored
	 * 
	 */
	public void mouseDragged(MouseEvent e) {
		/*Point p = e.getPoint();
		int hours = this.start.hour - this.end.hour;
		
		//int ticks = (int) Math.floor((int)(((double)(this.getHeight()/hours)) / Math.abs(p.y - this.dragStart.y)));
		int ticks = (int)Math.floor((this.getHeight()/hours) / (p.y - this.dragStart.y)) % hours;
		start.hour += ticks;
		end.hour += ticks;
		this.repaint();
		dragStart = p;*/
	}
	
	/**Called when the mouse is moved.  ignored.
	 * 
	 */
	public void mouseMoved(MouseEvent e) {
		EventShape inEvent = null;
		
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).contains(e.getPoint())) {
				inEvent = events.get(i);
				break;
			}
		}
		
		if (inEvent != null) this.setToolTipText(inEvent.event.getDescription());
		else this.setToolTipText(null);
	}

	/**Called when the mouse entered an object.  ignored.
	 * 
	 */
	public void mouseEntered(MouseEvent arg0) {}
	
	/**Called when the mouse exits an object.  ignored.
	 * 
	 */
	public void mouseExited(MouseEvent arg0) {}

	/**Called when the component is resized.  Makes a call to render()
	 * 
	 */
	public void componentResized(ComponentEvent arg0) {
		render();
	}

	/**Called when the component is shown.  Makes a call to render()
	 * 
	 */
	public void componentShown(ComponentEvent arg0) {
		render();
	}

	/**Called when the component is hidden.  ignored.
	 * 
	 */
	public void componentHidden(ComponentEvent arg0) {}
	
	/**Called when a component is moved.  ignored.
	 * 
	 */
	public void componentMoved(ComponentEvent arg0) {}
	
	/**
	 * zooms in on DayCanvas
	 * 
	 * @param e the MouseWheelEvent 
	 */
	public void zoomScroll(MouseWheelEvent e) {
		int notches = e.getWheelRotation();

		if (notches < 0) {
			if (e.getModifiersEx() != (MouseWheelEvent.WHEEL_UNIT_SCROLL | 
					MouseWheelEvent.CTRL_DOWN_MASK) && start.hour > 0) {
				start.hour -= 1;
				end.hour -= 1;
			} else if (e.getModifiersEx() == (MouseWheelEvent.WHEEL_UNIT_SCROLL | 
					MouseWheelEvent.CTRL_DOWN_MASK)) {
				if (start.hour < 12) start.hour += 1;
				if (end.hour > 12) end.hour -= 1;
			}
		} else {
			if (e.getModifiersEx() != (MouseWheelEvent.WHEEL_UNIT_SCROLL | 
					MouseWheelEvent.CTRL_DOWN_MASK) && end.hour < 24) {
				end.hour += 1;
				start.hour += 1;
			} else if (e.getModifiersEx() == (MouseWheelEvent.WHEEL_UNIT_SCROLL | 
					MouseWheelEvent.CTRL_DOWN_MASK)) {
				if (start.hour > 0) start.hour -= 1;
				if (end.hour < 24) end.hour += 1;
			}
		}
		this.render();
	}
	
	/**
	 * selects an event on DayCanvas.
	 * 
	 * @param e The MouseEvent to be detected.
	 */
	public void select(MouseEvent e) {
		for(int i = 0; i < events.size(); i++) {
			EventShape ev = events.get(i);
			if (ev.contains(e.getPoint())) {
				ev.selected = true;
				ev.redrawImage();
				parent.selectedEvent = ev;
				update();
			}
		}

		if (parent.selectedEvent == null) {
			parent.switchTopRightCard("Add Event");
		}
		
		//parent.selectedEvent.redrawImage();
		this.repaint();
	}
	
	/**
	 * deselects an event in the DayCanvas.
	 */
	public void deselect() {
		if (parent.selectedEvent != null) {
			parent.selectedEvent.selected = false;
			parent.selectedEvent.redrawImage();
			parent.selectedEvent = null;
		}
		parent.weekView.updateWeek();
		repaint();
	}

	/**
	 * Sets which day is to be displayed on the DayCanvas.
	 * 
	 * @param today The day to be displayed. 
	 */
	public void setDay(Period today) {
		this.today = today;
		
	}

	/**
	 * returns the day that is appeared on the DayCanvas.
	 * 
	 * @return the day that is currently displayed on the DayCanvas.
	 */
	public Period getDay() {
		return today;
	}
	
	/**
	 * updates the DayCanvas so events are displayed.
	 */
	public void update() {
		if (parent.selectedEvent != null) {
			parent.updateEv.fillIn(parent.selectedEvent.event);
			parent.switchTopRightCard("Update Event");
		} else {
			parent.switchTopRightCard("New Event");
		}
	}
}
