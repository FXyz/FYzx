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

import util.MeshUtils.Collider;

/**
 * The Idea is to collect A list of Nodes (this) collided with
 * 
 * @author Jason Pollastrini aka jdub1581
 */
public class CollisionResults {
    private final CollisionType typeOfCollision;
    private final Collider other;

    public CollisionResults(CollisionType type, Collider other) {
        this.typeOfCollision = type;
        this.other = other;
    }

    public CollisionType getTypeOfCollision() {
        return typeOfCollision;
    }

    public <T>Collider getOther() {
        return other;
    }
    
}
