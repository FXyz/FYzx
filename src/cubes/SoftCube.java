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
package cubes;

import particles.PointMass;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.fxyz.shapes.composites.PolyLine3D;
import org.fxyz.shapes.containers.ShapeContainer;
import org.fxyz.shapes.primitives.CubeMesh;
import particles.Link;
import particles.Particle;

/**
 * Cube Structure testing bindings
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class SoftCube extends ShapeContainer<CubeMesh> {

    private final CubeMesh mesh;
    private double size;
    private final Group debugGroup = new Group();
    private final ObservableList<PointMass> masses = FXCollections.observableArrayList();
    private final ObservableList<Particle> corners = FXCollections.observableArrayList();
    private final ObservableList<Link> links = FXCollections.observableArrayList();
    private final ObservableList<PolyLine3D> lines = FXCollections.observableArrayList();

    private double stiffness;
    private double mass;

    public SoftCube(double size) {
        super(new CubeMesh());
        this.mesh = getShape();
        this.size = size;
        this.mesh.setSize(size);
        this.setDiffuseColor(Color.STEELBLUE);
        this.setEmissiveLightingOn(true);
        this.setEmissiveLightingColor(Color.CHARTREUSE);

    }

    private void build() {
        double hh, hw, hd;
        hh = hw = hd = size / 2;
        PointMass[] pma = new PointMass[]{
            new PointMass(this, -hw, -hh, -hd),
            new PointMass(this, hw, -hh, -hd),
            new PointMass(this, -hw, hh, -hd),
            new PointMass(this, hw, hh, -hd),
            new PointMass(this, -hw, -hh, hd),
            new PointMass(this, hw, -hh, hd),
            new PointMass(this, -hw, hh, hd),
            new PointMass(this, hw, hh, hd)
        };
        masses.addAll(Arrays.asList(pma));

        if (debug) {
            loadDebugVisuals();
        }
    }

    //==========================================================================
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(ObservableList<Link> links) {
        this.links.setAll(links);
    }

    public double getStiffness() {
        return stiffness;
    }

    public void setStiffness(double stiffness) {
        this.stiffness = stiffness;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    private boolean debug;

    public boolean debugVisual(boolean b) {
        this.debug = b;

        return debug;
    }

    private void loadDebugVisuals() {
        masses.stream().forEach((pm) -> {
            Particle m = new Particle(pm);
            masses.filtered(mp -> {
                return !mp.equals(pm);
            }).forEach(p -> {
                m.attachTo(p, size, stiffness);
            });
        });
    }

}
