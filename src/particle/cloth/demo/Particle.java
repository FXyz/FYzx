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
package particle.cloth.demo;

import static java.lang.Math.min;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Particle extends Sphere {

    private final PhongMaterial material = new PhongMaterial(Color.CHARTREUSE);
    private PointMass massPoint;
    private final double mouseInfluenceScalar = 10;
    private final double mouseInfluenceSize = 35;
    private final double mouseTearSize = 10;
    public double mouX;
    public double mouY;
    public double oldMouX;
    public double oldMouY;
    private final Affine affine = new Affine();

    public Particle() {
        super(0.75);
        this.setMaterial(material);        
        this.getTransforms().add(affine);
        this.setOnMouseEntered(e -> {
            oldMouX = mouX;
            oldMouY = mouY;
            mouX = e.getSceneX();
            mouY = e.getSceneY();
        });
        this.setOnMousePressed(e -> {
            //System.out.println(getPointMass().getConstraints().size());
            double distanceSquared = distPointToSegmentSquared(oldMouX, oldMouY, mouX, mouY, getX(), getY());
            if (e.isPrimaryButtonDown()) {
                if (distanceSquared < mouseInfluenceSize) { 
                    setLastX(getX() - (mouX - oldMouX) * mouseInfluenceScalar);
                    setLastY(getY() - (mouX - oldMouX) * mouseInfluenceScalar);
                    setLastZ(getZ() - (mouX - oldMouX) * mouseInfluenceScalar);
                }
            }
            if(e.isSecondaryButtonDown()){
                massPoint.getConstraints().clear();
                ((Group)getParent()).getChildren().remove(Particle.this);
            }
        });
    }

    public Particle(PointMass massPoint) {
        this();
        this.massPoint = massPoint;
        this.affine.setTx(massPoint.getX());
        this.affine.setTy(massPoint.getY());
        this.affine.setTz(massPoint.getZ());
    }

    public Particle(double x, double y, double z) {
        this();
        this.massPoint = new PointMass(this, x, y, z);
        this.affine.setTx(massPoint.getX());
        this.affine.setTy(massPoint.getY());
        this.affine.setTz(massPoint.getZ());
    }

    public final PointMass getPointMass() {
        return massPoint;
    }

    public void updateUI() {
        if (!isPinned()) {
            this.affine.setTx(massPoint.getX());
            this.affine.setTy(massPoint.getY());
            this.affine.setTz(massPoint.getZ());
        }
    }

    public void updatePhysics(double timeStep) {
        massPoint.updatePhysics(timeStep);
//        updateUI();
    }

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

    public void removeLink(SpringLink lnk) {
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

    public final void setLastZ(double lastZ) {
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
