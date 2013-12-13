/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe.control;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 *
 * @author chibayuuki
 */
public class TagDataChangeEvent extends Event{
    
    public static final EventType TAG_DATA_CHANGE_EVENT = EventType.ROOT;
    
    public TagDataChangeEvent(EventType<? extends Event> et) {
        super(et);
    }

    public TagDataChangeEvent(Object o, EventTarget et, EventType<? extends Event> et1) {
        super(o, et, et1);
    }
    
}
