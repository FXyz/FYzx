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
package physics;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.fxyz.geometry.Point3D;
import physics.physicsobjects.Body;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class PhysicsScene extends Scene {

    private static final Group DEFAULT_ROOT = new Group();
    private static final double DEFAULT_WIDTH = 800.0;
    private static final double DEFAULT_HEIGHT = 600.0;
    private static final Paint DEFAULT_FILL = Color.SLATEGRAY;
    private static final boolean DEFAULT_DEPTH_BUFFER = true;
    private static final SceneAntialiasing DEFAULT_MSAA = SceneAntialiasing.BALANCED;

    private PhysicsScene() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FILL, DEFAULT_DEPTH_BUFFER, DEFAULT_MSAA);
    }

    private PhysicsScene(Paint fill) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fill, DEFAULT_DEPTH_BUFFER, DEFAULT_MSAA);
    }

    private PhysicsScene(double width, double height) {
        this(width, height, DEFAULT_FILL, DEFAULT_DEPTH_BUFFER, DEFAULT_MSAA);
    }

    private PhysicsScene(double width, double height, Paint fill) {
        this(width, height, fill, DEFAULT_DEPTH_BUFFER, DEFAULT_MSAA);
    }

    private PhysicsScene(double width, double height, boolean depthBuffer) {
        this(width, height, DEFAULT_FILL, depthBuffer, DEFAULT_MSAA);
    }

    private PhysicsScene(double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        this(width, height, DEFAULT_FILL, depthBuffer, antiAliasing);
    }

    private PhysicsScene(double width, double height, Paint fill, boolean depthBuffer, SceneAntialiasing antiAliasing) {
        super(DEFAULT_ROOT, width, height, depthBuffer, antiAliasing);
        this.setFill(fill);
    }
    /**
     * *************************************************************************
     * Properties *
     * *************************************************************************
     */
    private final ListProperty<Point3D> globalForces = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ObservableList<Point3D> getGlobalForces() {
        return globalForces.get();
    }

    public void addGlobalForce(Point3D value) {
        globalForces.add(value);
    }

    public ListProperty<Point3D> globalForcesProperty() {
        return globalForces;
    }

    //==========================================================================
    private final ListProperty<Body> physicalBodies = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ObservableList<Body> getPhysicalBodies() {
        return physicalBodies.get();
    }

    public void setPhysicalBodies(ObservableList<Body> value) {
        physicalBodies.set(value);
    }

    public ListProperty<Body> physicalBodiesProperty() {
        return physicalBodies;
    }

    //==========================================================================
    private final ListProperty<Body> staticBodies = new SimpleListProperty<Body>(FXCollections.observableArrayList()) {
        {
           // this.bindContentBidirectional(physicalBodies.filtered(isStatic -> {return !isStatic.isForceAffected();}));
        }       
        
    };

    public ObservableList<Body> getStaticBodies() {
        return staticBodies.get();
    }

    public void setStaticBodies(ObservableList<Body> value) {
        staticBodies.set(value);
    }

    public ListProperty<Body> staticBodiesProperty() {
        return staticBodies;
    }
    //==========================================================================
    

}
