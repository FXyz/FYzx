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

import java.util.logging.Logger;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Transform;

/**
 * Simple Line class whose start and stop points are bindable and length will
 * grow dynamically
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class SpringLine extends MeshView {
    private static final Logger log = Logger.getLogger(SpringLine.class.getName());
    private Point3D start, end;
    private final TriangleMesh mesh = new TriangleMesh();
    private final PhongMaterial mat = new PhongMaterial();
    /**
     * Orientation is which axis the line should be built from. example left to
     * right, up and down, forward and back;
     */
    public enum Orientation {
        X_AXIS,
        Y_AXIS,
        Z_AXIS,
        NONE;

        private Orientation() {
        }
    }

    public SpringLine(Color c) {        
        mat.setDiffuseColor(c);
        this.setMaterial(mat);
        this.setMesh(mesh);    
        this.length.bind(lengthBinding);
    }

    public void buildMesh(Point3D start, Point3D end, double w2) {
        this.start = start; this.end = end;
        //log.log(Level.INFO, "\nDistance from start to end point: {0}", start.distance(end));
        Transform t = this.getLocalToSceneTransform().clone();
        //create startPoints ... default is square     
        Point3D p0 = start.subtract(w2, w2, 0), //top left point
                p1 = start.add(w2, 0, 0).subtract(0, w2, 0), //top Right
                p2 = start.subtract(w2, 0, 0).add(0, w2, 0), //bottom left
                p3 = start.add(w2, w2, 0);                     //bottom Right
        t.transform(p0);t.transform(p1);t.transform(p2);t.transform(p3);
        Point3D p4 = end.subtract(w2, w2, 0), //top left point
                p5 = end.add(w2, 0, 0).subtract(0, w2, 0), //top Right
                p6 = end.subtract(w2, 0, 0).add(0, w2, 0), //bottom left
                p7 = end.add(w2, w2, 0);                       //bottom Right
        t.transform(p4);t.transform(p5);t.transform(p6);t.transform(p7);
        
        mesh.getPoints().setAll(
                //start points
                (float) p0.getX(), (float) p0.getY(), (float) p0.getZ(),//0 top left point
                (float) p1.getX(), (float) p1.getY(), (float) p1.getZ(),//1 top Right
                (float) p2.getX(), (float) p2.getY(), (float) p2.getZ(),//2 bottom left
                (float) p3.getX(), (float) p3.getY(), (float) p3.getZ(),//3 bottom Right
                //endpoints
                (float) p4.getX(), (float) p4.getY(), (float) p4.getZ(),//4 top left point
                (float) p5.getX(), (float) p5.getY(), (float) p5.getZ(),//5 top Right
                (float) p6.getX(), (float) p6.getY(), (float) p6.getZ(),//6 bottom left
                (float) p7.getX(), (float) p7.getY(), (float) p7.getZ() //7 bottom Right                    
        );

        mesh.getTexCoords().setAll(
                0, 0
        );

        mesh.getFaces().setAll(
                0, 0, 2, 0, 1, 0,
                2, 0, 3, 0, 1, 0,
                4, 0, 5, 0, 6, 0,
                6, 0, 5, 0, 7, 0,
                0, 0, 1, 0, 4, 0,
                4, 0, 1, 0, 5, 0,
                2, 0, 6, 0, 3, 0,
                3, 0, 6, 0, 7, 0,
                0, 0, 4, 0, 2, 0,
                2, 0, 4, 0, 6, 0,
                1, 0, 3, 0, 5, 0,
                5, 0, 3, 0, 7, 0
        );
    }
    public void recomputeMesh(){
    
    }
    public TriangleMesh getTriMesh() {
        return mesh;
    }

    public PhongMaterial getMat() {
        return mat;
    }
    
    private final DoubleBinding lengthBinding = new DoubleBinding() {
        @Override
        protected double computeValue() {
            if(start == null || end == null){
                return 0;
            }else{
                return start.distance(end);
            }
        }
    };
    
    private final DoubleProperty length = new SimpleDoubleProperty(){

        @Override
        protected void invalidated() {
            if(start != null && end != null){
                buildMesh(start, end, 2);
            }
        }
        
    };

    public double getLength() {
        return length.get();
    }

    public void setLength(double value) {
        length.set(value);
    }

    public DoubleProperty lengthProperty() {
        return length;
    }

}
