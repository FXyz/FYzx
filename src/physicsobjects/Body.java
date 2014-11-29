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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.util.Callback;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 * @param <T>
 */
public abstract class Body<T extends Node> extends Group {
    //==========================================================================

    private static long NEXT_ID = 0;

    public abstract T getNode();
    public abstract void updateUI();

    public static final Callback<Body, Point3D> positionCallback = (t)->{
        return new Point3D(t.getAffine().getTx(), t.getAffine().getTy(), t.getAffine().getTz());
    };
    //==========================================================================
    private double mass;
    private Point3D netForce = Point3D.ZERO;
    private final PhysicalState state;
    private boolean forceAffected, resting;
    private final List<Point3D> forces = new ArrayList<>();
    private final HashMap<Body,Constraint> constraints = new HashMap<>();
    private final List<Body> collidables = new ArrayList<>();
    private final Affine affine = new Affine();
    private Integrator integrator;
    
    /**
     * 
     * @param mass relative mass of body
     * @param position starting position in world coordinates
     * @param forceAffected whether or not forces will affect this body
     */
    public Body(double mass, Point3D position, boolean forceAffected) {
        this.setId(this.getClass().getSimpleName() + NEXT_ID++);
        this.mass = mass;
        
        this.state = new PhysicalState(this);
        this.state.setPosition(position);    
        this.state.setPrevPosition(position);
        
        this.forceAffected = forceAffected;
        this.initAffine();
    }

    public boolean isResting() {
        return resting;
    }
    
    public void setMass(double m){
        this.mass = m;
    }
    public double getMass() {
        return mass;
    }

    public PhysicalState getState() {
        return state;
    }
    
    /*==========================================================================
        Constraints
    */
    public HashMap<Body, Constraint> getConstraints() {
        return constraints;
    }
    public void addConstraint(Constraint c){
        constraints.put(this, c);
    }
    /**
     * The more it is solved the more accurate it becomes.. 
     * 4 is a good starting point
     * @param interations number of times to solve each constraint
     */
    public void solveConstraints() {
        synchronized(this){
            getConstraints().values().parallelStream().forEach(Constraint::solve);
        }
    }

    /*==========================================================================
        Collidable Objects in scene will probably remove ...
    */
    public List<Body> getCollidableObjects() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void checkCollisions() {
        throw new UnsupportedOperationException("not implemented yet");
    }
    
    /*==========================================================================
        Integration
    */
    public Integrator getIntegrator() {
        return integrator;
    }    

    public void setIntegrator(Integrator i) {
        this.integrator = i;
    }

    /*==========================================================================
        Forces
    */
    public void addForce(Point3D f) {
        forces.add(f);
    }

    public Point3D getNetForce() {
        forces.stream().forEach(
                f -> {
                    applyForce(f);
                }
        );
        return netForce;
    }

    private void applyForce(Point3D f) {
        netForce = netForce.add(f);
    }

    public boolean isForceAffected() {
        return forceAffected;
    }

    public void setForceAffected(boolean b) {
        forceAffected = b;
    }

    public final List<Point3D> getExternalForces() {
        return forces;
    }

    public void clearForces() {
        forces.clear();
        netForce = Point3D.ZERO;
    }
    
    /*==========================================================================
        Affine Transform used for Node orientation and positioning
    */
    public Affine getAffine() {
        return affine;
    }

    private void initAffine() {
        if (!getTransforms().contains(affine)) {
            getTransforms().add(affine);
            updateUI();
        }
    }
    
    /*==========================================================================
        Physics Update
    */
    
    public final void stepPhysics(double t, double dt) {
        integrator.updatePhysics(t, dt);
    }

}
