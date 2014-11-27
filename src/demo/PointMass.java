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
package demo;

import constraints.Constraint;
import constraints.Link;
import constraints.SpringLink;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
// PointMass
public class PointMass{

    private double lastX = 0, lastY = 0, lastZ = 0; // for calculating position change (velocity)
    private double x = 0, y = 0, z = 0;
    private double accX = 0, accY = 0, accZ = 0;
    private double gravity;
    private double mass = 1;
    private double damping = 20;

    // List for links, so we can have as many links as we want to this PointMass
    private List<Link> links = new ArrayList<>();
    private final HashMap<PointMass, Constraint> constraints = new HashMap<>();
    private boolean pinned = false;
    private double pinX = 0, pinY = 0, pinZ = 0;

    private boolean debug;
    private final Object target;

    // PointMass constructor
    public PointMass(Object target, double xPos, double yPos, double zPos) {
        this.target = target;
        this.x = xPos;
        this.y = yPos;
        this.z = zPos;

        this.lastX = getX();
        this.lastY = getY();
        this.lastZ = getZ();

        this.accX = 0;
        this.accY = 0;
        this.accZ = 0;
    }

    // The update function is used to update the physics of the PointMass.    
    public void updatePhysics(double timeStep) {

        double velX = getX() - getLastX();
        double velY = getY() - getLastY();
        double velZ = getZ() - getLastZ();

        // dampen velocity
        velX *= 0.99;
        velY *= 0.99;
        velZ *= 0.99;

        double timeStepSq = timeStep * timeStep;

        // calculate the next position using Verlet Integration
        double nextX = getX() + velX + getAccX() * 0.5f * timeStepSq;
        double nextY = getY() + velY + getAccY() * 0.5f * timeStepSq;
        double nextZ = getZ() + velZ + getAccZ() * 0.5f * timeStepSq;

        // reset variables
        setLastX(getX());
        setLastY(getY());
        setLastZ(getZ());

        setX(nextX);
        setY(nextY);
        setZ(nextZ);

        setAccX(0);
        setAccY(0);
        setAccZ(0);

        if (!isPinned()) {
            setX(getX());
            setY(getY());
            setZ(getZ());
        }
    }

    /* Constraints */

    public void solveConstraints() {
        /* SpringLink Constraints */
        // Links make sure PointMasss connected to this one is at a set distance away
        // changed from List
        /*if (!links.isEmpty()) {
            links.stream().forEach((l) -> {
                l.solve();
            });
        }*/
        constraints.values().parallelStream().forEach(Constraint::solve);
        /* Boundary Constraints */
        // These if statements keep the PointMasss within the screen
        if (getY() < 1) {
            setY(2 * (1) - getY());
        }
        if (getY() > 1050 - 1) {
            setY(2 * (1050 - 1) - getY());
        }

        if (getX() > 1920 - 1) {
            setX(2 * (1920 - 1) - getX());
        }
        if (getX() < 1) {
            setX(2 * (1) - getX());
        }

        /* Other Constraints */
        // make sure the PointMass stays in its place if it's pinned
        if (pinned) {
            setX(getPinX());
            setY(getPinY());
            setZ(getPinZ());
        }
       
    }


    public void applyForce(double fX, double fY, double fZ) {
        // acceleration = (1/mass) * force
        // or
        // acceleration = force / mass
        this.setAccX(getAccX() + fX / getMass());
        this.setAccY(getAccY() + fY / getMass());
        this.setAccZ(getAccZ() + fZ / getMass());
    }

    // attachTo can be used to create links between this PointMass and other PointMasss
    public final void attachTo(PointMass P, double restingDist, double stiff) {
        attachTo(P, restingDist, stiff, 30, true);
    }

    public final void attachTo(PointMass P, double restingDist, double stiff, boolean drawLink) {
        attachTo(P, restingDist, stiff, 30, drawLink);
    }

    public final void attachTo(PointMass P, double restingDist, double stiff, double tearSensitivity) {
        attachTo(P, restingDist, stiff, tearSensitivity, true);
    }

    public final void attachTo(PointMass P, double restingDist, double stiff, double tearSensitivity, boolean drawLink) {
        SpringLink lnk = new SpringLink(this, P, restingDist, stiff, tearSensitivity);
        
        constraints.put(P, (Constraint) lnk);
    }

    public void pinTo(double pX, double pY, double pZ) {
        this.pinned = true;
        this.pinX = pX;
        this.pinY = pY;
        this.pinZ = pZ;
    }

    public void removeLink(SpringLink lnk) {
        links.remove(lnk);
        
        constraints.values().remove(lnk);
    }

    public double getLastX() {
        return lastX;
    }

    public final void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public final void setLastY(double lastY) {
        this.lastY = lastY;
    }

    public final double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public final double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLastZ() {
        return lastZ;
    }

    public void setLastZ(double lastZ) {
        this.lastZ = lastZ;
    }

    public final double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getAccZ() {
        return accZ;
    }

    public void setAccZ(double accZ) {
        this.accZ = accZ;
    }

    public double getPinZ() {
        return pinZ;
    }

    public void setPinZ(double pinZ) {
        this.pinZ = pinZ;
    }

    public double getAccX() {
        return accX;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getDamping() {
        return damping;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public double getPinX() {
        return pinX;
    }

    public void setPinX(double pinX) {
        this.pinX = pinX;
    }

    public double getPinY() {
        return pinY;
    }

    public void setPinY(double pinY) {
        this.pinY = pinY;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public HashMap<PointMass, Constraint> getConstraints() {
        return constraints;
    }
     
}
