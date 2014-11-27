/*
 * Copyright (C) 2014 F(Y)zx :: Jason Pollastrini aka jdub1581, 
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

import constraints.Constraint;
import integrators.Integrator;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 * @param <T>
 */
public interface Body<T extends Node> {
    
    public T getNode();
    
    public double getMass();
    
    public boolean isForceAffected();
    public void setForceAffected(boolean b);
    public Point3D getNetForce();
    public void addForce(Point3D f);
    public List<Point3D> getExternalForces();    
    public void applyForce(Point3D f);    
    public void applyForces();
    public void clearForces();  
    
    public boolean isResting();
    
    public Affine getAffine();
    public void initAffine();
    
    public HashMap<? super Body,? super Constraint> getConstraints();
    public void solveConstraints(int interations);
    
    public List<Body> getCollidableObjects();
    public void checkCollisions();
    
    public void setState(BodyState s);
    public BodyState getState();
    public BodyState getPreviousState();
    
    public Integrator getIntegrator();
    
    public void stepPhysics(double t, double dt);
    public void updateUI();
}
