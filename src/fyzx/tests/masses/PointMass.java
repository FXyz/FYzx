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
package fyzx.tests.masses;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
// PointMass
class PointMass {

    private double lastX, lastY, lastZ; // for calculating position change (velocity)
    private double x, y, z;
    private double accX = 0, accY = 0, accZ;
    private double gravity = 980;
    // every PointMass within this many pixels will be influenced by the cursor
    private double mouseInfluenceSize = 20;
// minimum distance for tearing when user is right clicking
    private double mouseTearSize = 8;
    private double mouseInfluenceScalar = 5;
    private double mass = 2;
    private double damping = 20;

    // An ArrayList for links, so we can have as many links as we want to this PointMass
    List<Link> links = new ArrayList<>();

    boolean pinned = false;
    private double pinX, pinY, pinZ;
    
    private boolean debug;
    

    // PointMass constructor
    public PointMass(double xPos, double yPos, double zPos) {
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
    // motion is applied, and links are drawn here
    public void updatePhysics(double timeStep) { // timeStep should be in elapsed seconds (deltaTime)
        
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

        //System.out.println(getAccX());
        //System.out.println(getAccY());
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
        /* Link Constraints */
        // Links make sure PointMasss connected to this one is at a set distance away
        if(!links.isEmpty()){
            links.stream().forEach((link) -> {
                link.solve();
            });

            /* Boundary Constraints */
            // These if statements keep the PointMasss within the screen
            if (getY() < 1) {
                setY(2 * (1) - getY());
            }
            if (getY() > 800 - 1) {
                setY(2 * (800 - 1) - getY());
            }

            if (getX() > 800 - 1) {
                setX(2 * (800 - 1) - getX());
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
    }

    // attachTo can be used to create links between this PointMass and other PointMasss
    public void attachTo(PointMass P, double restingDist, double stiff) {
        attachTo(P, restingDist, stiff, 30, true);
    }

    public void attachTo(PointMass P, double restingDist, double stiff, boolean drawLink) {
        attachTo(P, restingDist, stiff, 30, drawLink);
    }

    public void attachTo(PointMass P, double restingDist, double stiff, double tearSensitivity) {
        attachTo(P, restingDist, stiff, tearSensitivity, true);
    }

    public void attachTo(PointMass P, double restingDist, double stiff, double tearSensitivity, boolean drawLink) {
        Link lnk = new Link(this, P, restingDist, stiff, tearSensitivity);
        links.add(lnk);
    }

    public void removeLink(Link lnk) {
        links.remove(lnk);
    }

    public void applyForce(double fX, double fY, double fZ) {
        // acceleration = (1/mass) * force
        // or
        // acceleration = force / mass
        this.setAccX(getAccX() + fX / getMass());
        this.setAccY(getAccY() + fY / getMass());
        this.setAccZ(getAccZ() + fZ / getMass());
    }

    public void pinTo(double pX, double pY, double pZ) {
        this.pinned = true;
        this.pinX = pX;
        this.pinY = pY;
        this.pinZ = pZ;
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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
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
    
}
