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
package util;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class RayCaster {
    private Point3D origin, direction;
    private double near, far;
    private final List<Node> intersected = new ArrayList<>();
    
    public RayCaster(Point3D o, Point3D d) {
        this.origin = o;
        this.direction = d;
        this.near = 0.1;
        this.far = 10000;
    }

    public RayCaster(Point3D origin, Point3D direction, double near, double far) {
        this.origin = origin;
        this.direction = direction;
        this.near = near;
        this.far = far;
    }
    
    public void castRay(List<Node> nodes){
        nodes.forEach(n->{
            Affine a = new Affine(n.getLocalToSceneTransform());
            Point3D dir = origin.subtract(Point3D.ZERO.add(a.getTx(),a.getTy(), a.getTz()));
            System.out.println(dir + "\n" + n.getBoundsInParent());
            //a.transform(dir);
            if(n.getBoundsInParent().contains(dir)){
                intersected.add(n);
            }
        });
        
        
    }

    public Point3D getOrigin() {
        return origin;
    }

    public Point3D getDirection() {
        return direction;
    }

    public void setDirection(Point3D direction) {
        this.direction = direction;
    }

    public double getNear() {
        return near;
    }

    public void setNear(double near) {
        this.near = near;
    }

    public double getFar() {
        return far;
    }

    public void setFar(double far) {
        this.far = far;
    }
    
    
    private class RayResults{
        
    }
}
