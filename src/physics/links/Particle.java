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
package physics.links;

import java.util.HashMap;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import physics.constraints.Constraint;
import physics.integrators.Integrator;
import physics.integrators.VerletIntegrator;
import physics.physicsobjects.Body;
import physics.physicsobjects.PhysicalState;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Particle extends Body<Sphere> {

    private final Sphere sphere;
    private Point3D pinPos;
    private boolean pinned;

    public Particle(Point3D position) {
        this(2.5, 5.0, position, true);
    }

    public Particle(double radius, Point3D position) {
        this(radius, 5.0, position, true);
    }

    public Particle(Point3D position, boolean forceAffected) {
        this(2.5, 5.0, position, forceAffected);
    }

    public Particle(double radius, Point3D position, boolean forceAffected) {
        this(radius, 5.0, position, forceAffected);
    }

    public Particle(Point3D position, double mass, boolean forceAffected) {
        this(2.5, mass, position, forceAffected);
    }

    public Particle(double radius, double mass, Point3D position, boolean forceAffected) {
        super(mass, position, forceAffected);
        
        this.setIntegrator(new VerletIntegrator(getState(), 1));
        
        this.sphere = new Sphere(radius);
        this.sphere.setMaterial(new PhongMaterial(Color.CHARTREUSE));
        
        this.getChildren().add(sphere);
        
        this.getAffine().setTx(getState().getPosition().getX());
        this.getAffine().setTy(getState().getPosition().getY());
        this.getAffine().setTz(getState().getPosition().getZ());

    }

    @Override
    public Sphere getNode() {
        return sphere;
    }

    @Override
    public void updateUI() {
        if (!isPinned()) {
            getAffine().appendTranslation(
                    getState().getPosition().getX(),
                    getState().getPosition().getY(),
                    getState().getPosition().getZ()
            );
        }
    }
    
    /*
     Delegate Methods
     */

    public final void setRadius(double value) {
        sphere.setRadius(value);
    }

    public final double getRadius() {
        return sphere.getRadius();
    }

    public final DoubleProperty radiusProperty() {
        return sphere.radiusProperty();
    }

    public int getDivisions() {
        return sphere.getDivisions();
    }

    public final void setMaterial(Material value) {
        sphere.setMaterial(value);
    }

    public final Material getMaterial() {
        return sphere.getMaterial();
    }

    public final ObjectProperty<Material> materialProperty() {
        return sphere.materialProperty();
    }

    public final void setDrawMode(DrawMode value) {
        sphere.setDrawMode(value);
    }

    public final DrawMode getDrawMode() {
        return sphere.getDrawMode();
    }

    public final ObjectProperty<DrawMode> drawModeProperty() {
        return sphere.drawModeProperty();
    }

    public final void setCullFace(CullFace value) {
        sphere.setCullFace(value);
    }

    public final CullFace getCullFace() {
        return sphere.getCullFace();
    }

    public final ObjectProperty<CullFace> cullFaceProperty() {
        return sphere.cullFaceProperty();
    }

    public Point3D getPinPos() {
        return pinPos;
    }

    public void pinTo(Point3D pinPos) {
        this.pinPos = pinPos;
        setPinned(true);
    }

    public void unPin() {
        setPinned(false);
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public final Affine getAffine() {
        return super.getAffine(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearForces() {
        super.clearForces(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setForceAffected(boolean b) {
        super.setForceAffected(b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isForceAffected() {
        return super.isForceAffected(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point3D getNetForce() {
        return super.getNetForce(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addForce(Point3D f) {
        super.addForce(f); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public final void setIntegrator(Integrator i) {
        super.setIntegrator(i); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integrator getIntegrator() {
        return super.getIntegrator(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void checkCollisions() {
        super.checkCollisions(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Body> getCollidableObjects() {
        return super.getCollidableObjects(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void solveConstraints() {
        super.solveConstraints(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addConstraint(Constraint c) {
        super.addConstraint(c); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap<Body, Constraint> getConstraints() {
        return super.getConstraints(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public final PhysicalState getState() {
        return super.getState(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getMass() {
        return super.getMass(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMass(double m) {
        super.setMass(m); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isResting() {
        return super.isResting(); //To change body of generated methods, choose Tools | Templates.
    }

}
