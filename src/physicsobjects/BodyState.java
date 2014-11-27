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
package physicsobjects;

import javafx.geometry.Point3D;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class BodyState {
    
    private Point3D velocity, position;
    
    public BodyState(Point3D p, Point3D v){
        
        this.velocity = new Point3D(v.getX(),v.getY(),v.getZ());
        this.position = new Point3D(p.getX(),p.getY(),p.getZ());
    }


    public Point3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point3D v) {
        this.velocity = v;
    }

    public Point3D getPosition() {
        return position;
    }

    public void setPosition(Point3D p) {
        this.position = p;
    }
    
}
