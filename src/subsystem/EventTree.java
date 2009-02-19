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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package subsystem;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Dana Burkart
 */
public class EventTree {
    public class Node {
        public int type;
        public int value;
        
        public Set<Node> children;
        public ArrayList<Event> events;
    }
    
    Set<Node> years;
    
    public Event add(Event e) {
        
        return e;
    }
}
