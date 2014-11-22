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

import static java.lang.Math.min;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class MassParticle extends Sphere {

    private final PhongMaterial material = new PhongMaterial(Color.CHARTREUSE);
    private final PointMass massPoint;
    private boolean mousePressed;
    private boolean primaryButtonDown;
    private final double mouseInfluenceScalar = 5;
    private final double mouseInfluenceSize = 20;
    private final double mouseTearSize = 8;
    public double mouX;
    public double mouY;
    public double oldMouX;
    public double oldMouY;

    public MassParticle(double x, double y, double z) {
        this.massPoint = new PointMass(x, y, z);

        this.setTranslateX(massPoint.getX());
        this.setTranslateY(massPoint.getY());
        this.setTranslateZ(massPoint.getZ());

        this.setMaterial(material);
        this.setRadius(1);
        
        this.setOnMousePressed(e->{
            oldMouX = mouX; oldMouY = mouY;
            mouX = e.getSceneX(); mouY = e.getSceneY();
            mousePressed = true;
            if(e.isPrimaryButtonDown()){
                primaryButtonDown = true;
            }
        });
        this.setOnMouseReleased(e->{
            mousePressed = false;
            primaryButtonDown = true;            
        });
    }

    public PointMass getPointMass() {
        return massPoint;
    }

    public void updatePhysics(double timeStep) {
        massPoint.updatePhysics(timeStep);
        if (!isPinned()) {
            this.setTranslateX(massPoint.getX());
            this.setTranslateY(massPoint.getY());
            this.setTranslateZ(massPoint.getZ());
        }
    }

    /*void updateInteractions() {
        // this is where our interaction comes in.
        if (mousePressed) {
            double distanceSquared = distPointToSegmentSquared(oldMouX, oldMouY, mouX, mouY, getX(), getY());
            if (primaryButtonDown) {
                if (distanceSquared < mouseInfluenceSize) { // remember mouseInfluenceSize was squared in setup()
                    // To change the velocity of our PointMass, we subtract that change from the lastPosition.
                    // When the physics gets integrated (see updatePhysics()), the change is calculated
                    // Here, the velocity is set equal to the cursor's velocity
                    setLastX(getX() - (mouX - oldMouX) * mouseInfluenceScalar);
                    setLastY(getY() - (mouX - oldMouX) * mouseInfluenceScalar);
                }
            } else { // if the right mouse button is clicking, we tear the cloth by removing links
                if (distanceSquared < mouseTearSize) //getLinks().clear();
                {
                    
                }
            }
        }
    }*/

    public void solveConstraints() {
        massPoint.solveConstraints();
    }

    public void attachTo(PointMass P, double restingDist, double stiff) {
        massPoint.attachTo(P, restingDist, stiff);
    }

    public void attachTo(PointMass P, double restingDist, double stiff, boolean drawLink) {
        massPoint.attachTo(P, restingDist, stiff, drawLink);
    }

    public void attachTo(PointMass P, double restingDist, double stiff, double tearSensitivity) {
        massPoint.attachTo(P, restingDist, stiff, tearSensitivity);
    }

    public void attachTo(PointMass P, double restingDist, double stiff, double tearSensitivity, boolean drawLink) {
        massPoint.attachTo(P, restingDist, stiff, tearSensitivity, drawLink);
    }

    public void removeLink(Link lnk) {
        massPoint.removeLink(lnk);
    }

    public void applyForce(double fX, double fY, double fZ) {
        massPoint.applyForce(fX, fY, fZ);
    }

    public void pinTo(double pX, double pY, double pZ) {
        massPoint.pinTo(pX, pY, pZ);
    }

    public double getLastX() {
        return massPoint.getLastX();
    }

    public final void setLastX(double lastX) {
        massPoint.setLastX(lastX);
    }

    public double getLastY() {
        return massPoint.getLastY();
    }

    public final void setLastY(double lastY) {
        massPoint.setLastY(lastY);
    }

    public final double getX() {
        return massPoint.getX();
    }

    public void setX(double x) {
        massPoint.setX(x);
    }

    public final double getY() {
        return massPoint.getY();
    }

    public void setY(double y) {
        massPoint.setY(y);
    }

    public double getLastZ() {
        return massPoint.getLastZ();
    }

    public void setLastZ(double lastZ) {
        massPoint.setLastZ(lastZ);
    }

    public final double getZ() {
        return massPoint.getZ();
    }

    public void setZ(double z) {
        massPoint.setZ(z);
    }

    public double getAccZ() {
        return massPoint.getAccZ();
    }

    public void setAccZ(double accZ) {
        massPoint.setAccZ(accZ);
    }

    public double getPinZ() {
        return massPoint.getPinZ();
    }

    public void setPinZ(double pinZ) {
        massPoint.setPinZ(pinZ);
    }

    public double getAccX() {
        return massPoint.getAccX();
    }

    public void setAccX(double accX) {
        massPoint.setAccX(accX);
    }

    public double getAccY() {
        return massPoint.getAccY();
    }

    public void setAccY(double accY) {
        massPoint.setAccY(accY);
    }

    public double getGravity() {
        return massPoint.getGravity();
    }

    public void setGravity(double gravity) {
        massPoint.setGravity(gravity);
    }

    public void setMass(double mass) {
        this.massPoint.setMass(mass);
    }

    public double getDamping() {
        return massPoint.getDamping();
    }

    public void setDamping(double damping) {
        massPoint.setDamping(damping);
    }

    public List<Link> getLinks() {
        return massPoint.getLinks();
    }

    public void setLinks(List<Link> links) {
        massPoint.setLinks(links);
    }

    public boolean isPinned() {
        return massPoint.isPinned();
    }

    public void setPinned(boolean pinned) {
        massPoint.setPinned(pinned);
    }

    public double getPinX() {
        return massPoint.getPinX();
    }

    public void setPinX(double pinX) {
        massPoint.setPinX(pinX);
    }

    public double getPinY() {
        return massPoint.getPinY();
    }

    public void setPinY(double pinY) {
        massPoint.setPinY(pinY);
    }

    public boolean isDebug() {
        return massPoint.isDebug();
    }

    public void setDebug(boolean debug) {
        massPoint.setDebug(debug);
    }

    public double getMass() {
        return massPoint.getMass();
    }

    public final double distPointToSegmentSquared(double lineX1, double lineY1, double lineX2, double lineY2, double pointX, double pointY) {
        double vx = lineX1 - pointX;
        double vy = lineY1 - pointY;
        double ux = lineX2 - lineX1;
        double uy = lineY2 - lineY1;

        double len = ux * ux + uy * uy;
        double det = (-vx * ux) + (-vy * uy);
        if ((det < 0) || (det > len)) {
            ux = lineX2 - pointX;
            uy = lineY2 - pointY;
            return min(vx * vx + vy * vy, ux * ux + uy * uy);
        }

        det = ux * vy - uy * vx;
        return (det * det) / len;
    }

}
