/*
 * Cake Calendar
 * Copyright (C) 2009  Hashem Assayari, Dana Burkart, Vladimir Hadzhiyski, 
 * Jack Zhang
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package subsystem;

import java.util.ArrayList;

/**
 * A EventTreeNode represents a subtree in the EventTree class.
 * 
 * @author Dana Burkart
 */
public class EventTreeNode {
    public static final int ROOT = 4;           // this node is the root
    public static final int YEAR = 3;           // this node is a year
    public static final int MONTH = 2;          // this node is a month
    public static final int DAY = 1;            // this node is a day
    public static final int CONTAINER = 0;      // exactly like day, but with no value
    
    private int type;
    private int value;
        
    public ArrayList<EventTreeNode> children;
    public ArrayList<Event> events;
    
    public EventTreeNode(int type, int value) {
        this.type = type;
        this.value = value;
        
        if (this.type < EventTreeNode.MONTH) {
            children = new ArrayList<EventTreeNode>();
        } else {
            events = new ArrayList<Event>();
        }
    }
    
    /**
     * Returns all of the events contained in this subtree.
     * 
     * @return all of the events in this subtree
     */
    public ArrayList<Event> getEvents() {
        ArrayList<Event> evs = new ArrayList<Event>();
        
        if (this.type < EventTreeNode.MONTH) {
            return this.events;
        }
            
        EventTreeNode[] c = (EventTreeNode[])children.toArray();
        for (int i = 0; i < c.length; i++) {
            evs.addAll(c[i].getEvents());
        }
         
        return evs;
    }
    
    /**
     * Adds an event into this subtree
     * 
     * @param e event to add
     * @return the newly added event
     */
    public Event addEvent(Event e) {
        if (this.type < EventTreeNode.MONTH) {
            if (this.type != EventTreeNode.CONTAINER)
                e.setUID(e.getUID()+(value*100)+this.events.size());
            events.add(e);
            return e;
        } else if (this.type == EventTreeNode.MONTH) {
            EventTreeNode n = this.getChild(e.getPeriod().start.date.day);
            e.setUID(e.getUID() + (value * 10000));
            e = n.addEvent(e);
            return e;
        } else if (this.type == EventTreeNode.YEAR) {
            EventTreeNode n = this.getChild(e.getPeriod().start.date.month);
            e.setUID(e.getUID() + (value * 1000000));
            e = n.addEvent(e);
            return e;
        }
        return e;
    }
    
    /**
     * Gets the type of this subtree
     * 
     * @return type of the subtree
     */
    public int getType() {
        return type;
    }
    
    /**
     * Gets the value at the root node of this subtree, depends upon the type
     * of subtree.
     * 
     * @return value of the root of the subtree
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets a child whose root has the specified value. Creates and adds a new
     * Node if the Node we are looking for does not exist.
     * 
     * @param val value we are looking for
     * @return Node found, or new Node
     */
    private EventTreeNode getChild(int val) {
        if (this.type > EventTreeNode.DAY) {
            EventTreeNode[] n = (EventTreeNode[])children.toArray();

            for (int i = 0; i < n.length; i++) {
                if (n[i].getValue() == val) 
                    return n[i];
            }

            //-- if we get to this point, we must not have a child with that
            //-- value, so lets make a new child
            EventTreeNode k = new EventTreeNode(this.type-1, val);
            children.add(k);
            return k;
        } 
        //-- we should never get here because getChild() should only be called
        //-- on EventTreeNodes that are months are years
        return null;
    }
}
