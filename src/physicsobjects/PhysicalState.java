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
 * State values should be automatically handled updating its values when
 * position is changed Holds position and velocity data at a given point in
 * simulation when setting position or prevPosition velocity, acceleration, and
 * momentum are updated automatically
 *
 * may try using properties later for binding ...
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class PhysicalState {
    private boolean isInitialSetupDone = false;
    private final Body body;
    private Point3D 
            acceleration = Point3D.ZERO, prevAcceleration= Point3D.ZERO,
            momentum= Point3D.ZERO, prevMomentum= Point3D.ZERO,
            velocity= Point3D.ZERO, prevVelocity= Point3D.ZERO,
            position= null, prevPosition= null;

    /**
     *
     * @param b
     */
    public PhysicalState(Body b) {
        this.body = b;
        this.setPosition(Body.positionCallback.call(body));
    }

    /*==========================================================================
        
     */
    /**
     *
     * @return
     */
    public Point3D getAcceleration() {
        return acceleration;
    }

    /**
     *
     * @param acceleration
     */
    public void setAcceleration(Point3D acceleration) {
        this.acceleration = acceleration;
    }

    /**
     *
     * @return
     */
    public Point3D getPrevAcceleration() {
        return prevAcceleration;
    }

    /**
     *
     * @param prevAcceleration
     */
    public void setPrevAcceleration(Point3D prevAcceleration) {
        this.prevAcceleration = prevAcceleration;
    }

    /**
     *
     * @return
     */
    public Point3D getMomentum() {
        return momentum;
    }

    /**
     *
     * @param momentum
     */
    public void setMomentum(Point3D momentum) {        
        this.momentum = momentum;
    }

    /**
     *
     * @return
     */
    public Point3D getPrevMomentum() {
        return prevMomentum;
    }

    /**
     *
     * @param prevMomentum
     */
    public void setPrevMomentum(Point3D prevMomentum) {
        this.prevMomentum = prevMomentum;
    }

    /**
     *
     * @return
     */
    public Point3D getVelocity() {
        return velocity;
    }
    /**
     *
     * @return
     */
    public Point3D getCurrentVelocity() {
        return getPosition().subtract(getPrevPosition());
    }

    /**
     *
     * @param velocity
     */
    public void setVelocity(Point3D velocity) {        
        this.velocity = velocity;
    }
    
    /**
     *
     * @return
     */
    public Point3D getPrevVelocity() {
        return prevVelocity;
    }

    /**
     *
     * @param prevVelocity
     */
    public void setPrevVelocity(Point3D prevVelocity) {
        this.prevVelocity = prevVelocity;
    }

    /**
     *
     * @return
     */
    public Point3D getPosition() {
        return position;
    }

    /**
     *
     * @param position
     */
    public final void setPosition(Point3D position) {
        this.position = position;                
    }

    /**
     *
     * @return
     */
    public Point3D getPrevPosition() {
        return prevPosition;
    }

    /**
     *
     * @param prevPosition
     */
    public void setPrevPosition(Point3D prevPosition) {
        this.prevPosition = prevPosition;
    }

    public final Body getBody() {
        return body;
    }

    /*==========================================================================
     for use in VerletIntegrator
     updateState recalculates velocity, acceleration, momentum ahead of the integrator
     so the integrator can simply apply the forula to the position  
     */
    public void updateState(double dt) {
        // update velocity
        setVelocity(
                getPosition().subtract(getPrevPosition()).multiply(dt)
        );
        // update acceleration
        setAcceleration(
                new Point3D(
                        (getVelocity().getX() - getPrevVelocity().getX()) / dt,
                        (getVelocity().getY() - getPrevVelocity().getY()) / dt,
                        (getVelocity().getZ() - getPrevVelocity().getZ()) / dt
                )
        );
        // update momentum
        setMomentum(getVelocity().multiply(body.getMass()).multiply(dt));

    }

}
