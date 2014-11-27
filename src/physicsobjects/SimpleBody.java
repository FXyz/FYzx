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

import constraints.Constraint;
import integrators.Integrator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Affine;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class SimpleBody extends Group implements Body<SimpleBody> {

    private static long NEXT_ID = 0;

    private final Object lock = new Object();

    private final double mass;
    private Point3D netForce = Point3D.ZERO;
    private BodyState currentState, previousState;
    private boolean forceAffected, resting;

    private final List<Point3D> forces = new ArrayList<>();
    private final HashMap<Body, Constraint> constraints = new HashMap<>();
    private final List<Body> collidables = new ArrayList<>();

    private final Affine affine = new Affine();

    private final Integrator integrator;

    public SimpleBody(double mass, Point3D position, boolean forceAffected, Integrator integrator) {
        this.setId("Body" + NEXT_ID++);
        this.mass = mass;
        this.previousState = new BodyState(position, Point3D.ZERO);
        this.currentState = new BodyState(position, Point3D.ZERO);
        this.forceAffected = forceAffected;
        this.integrator = integrator;
        this.initAffine();
    }

    @Override
    public SimpleBody getNode() {
        return SimpleBody.this;
    }

    @Override
    public double getMass() {
        return mass;
    }

    @Override
    public List<Point3D> getExternalForces() {
        return forces;
    }

    @Override
    public void addForce(Point3D f) {
        getExternalForces().add(f);
    }

    @Override
    public Point3D getNetForce() {
        forces.stream().forEach(
                f -> {
                    applyForce(f);
                }
        );
        return netForce;
    }

    @Override
    public void applyForce(Point3D f) {
        netForce = netForce.add(f);
    }

    @Override
    public boolean isForceAffected() {
        return forceAffected;
    }

    @Override
    public void setForceAffected(boolean b) {
        forceAffected = b;
    }

    @Override
    public boolean isResting() {
        return resting;
    }

    @Override
    public Affine getAffine() {
        return affine;
    }

    @Override
    public final void initAffine() {
        if (!getTransforms().contains(affine)) {
            getTransforms().add(affine);
        }
    }

    @Override
    public HashMap<Body, Constraint> getConstraints() {
        return constraints;
    }

    @Override
    public void solveConstraints(int interations) {
        getConstraints().values().parallelStream().forEach(Constraint::solve);
    }

    @Override
    public List<Body> getCollidableObjects() {
        return collidables;
    }

    @Override
    public void checkCollisions() {
        throw new UnsupportedOperationException("not implemented yet");
    }    
    
    @Override
    public void setState(BodyState s) {
        synchronized (lock) {
            previousState = new BodyState(currentState.getPosition(), currentState.getVelocity());
            currentState = s;
        }
    }

    @Override
    public BodyState getPreviousState() {
        return previousState;
    }

    @Override
    public BodyState getState() {
        return currentState;
    }

    @Override
    public Integrator getIntegrator() {
        return integrator;
    }

    @Override
    public void clearForces() {
        getExternalForces().clear();
    }

    @Override
    public void applyForces() {
        currentState.setVelocity(currentState.getVelocity().add(getNetForce()));
    }

    @Override
    public void stepPhysics(double t, double dt) {
        integrator.updatePhysics(t, dt);
    }

    @Override
    public void updateUI() {
        affine.setTx(getState().getPosition().getX());
        affine.setTy(getState().getPosition().getY());
        affine.setTz(getState().getPosition().getZ());
    }

}
