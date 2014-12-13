/*
 * Copyright (C) 2014 F(Y)zx :
 * Authored by : Jason Pollastrini aka jdub1581, 
 * All rights reserved.
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
package physics.collision;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventTarget;
import javafx.event.EventType;
import util.MeshUtils.Collider;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class CollisionEvent extends Event{

    public static final EventType<CollisionEvent> ANY_COLLISION = new EventType(EventType.ROOT, "ANY_COLLISION");
    public static final EventType<CollisionEvent> COLLIDED_ON_BOUNDS = new EventType(ANY, "COLLIDED_WITH_BOUNDARY");
    public static final EventType<CollisionEvent> COLLIDED_WITH_OTHER = new EventType(ANY, "COLLIDED_WITH_OTHER");
    public static final EventType<CollisionEvent> COLLIDED_WITH_CHILD = new EventType(ANY, "COLLIDED_WITH_CHILD");
    
    private Collider otherCollider;
        
    public CollisionEvent(Collider other){
        this(ANY_COLLISION);
        this.otherCollider = other;
        chain.dispatchEvent(CollisionEvent.this);
    }
    public CollisionEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public CollisionEvent(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }
    
    
    private EventDispatchChain chain = new EventDispatchChain() {

        @Override
        public EventDispatchChain append(EventDispatcher eventDispatcher) {
            return target.buildEventDispatchChain(chain.append(eventDispatcher));
        }

        @Override
        public EventDispatchChain prepend(EventDispatcher eventDispatcher) {
            return target.buildEventDispatchChain(chain.prepend(eventDispatcher));
        }

        @Override
        public Event dispatchEvent(Event event) {
            return event;
        }
    };

    public Collider getOtherCollider() {
        return otherCollider;
    }
    
    
}
