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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import physics.constraints.Constraint;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class SpringCube extends Group {
    private static final Logger log = Logger.getLogger(SpringCube.class.getName());

    private final Particle[] corners = new Particle[8];
    private final Spring[] links = new Spring[12];
    private final PhongMaterial matC = new PhongMaterial(Color.BLUEVIOLET);
    
    private final List<Constraint> constraints = new ArrayList<>();

    public SpringCube(double size) {
        setSize(size);
        buildCorners(size);
        buildLinks();
        attachLinks();
        
        setRotationAxis(Point3D.ZERO.add(1,1,0));
        setRotate(30);        
    }
    
    public void solveConstraints(){
        Arrays.stream(corners).forEach(e->{
            e.solveConstraints();
        });
    }
    
    public void updatePhysics(double dt){
        Arrays.stream(links).forEach(l->{
            //l.solve();
        });
        Arrays.stream(corners).forEach(e->{
            e.stepPhysics(1, dt);
        });
    }
    public void updateUI(){
        Arrays.stream(links).forEach(l->{
            l.updateUI();
        });
        Arrays.stream(corners).forEach(e->{
            e.updateUI();
        });
    }
    
    /*==========================================================================
    
    */

    private void buildCorners(double size) {
        double hw, hh, hd;
        hw = hh = hd = size * 0.5;                
        corners[0] = new Particle((size * 0.25)/2, Point3D.ZERO.add(-hw, -hh, -hd));
        corners[1] = new Particle((size * 0.25)/2, Point3D.ZERO.add(hw, -hh, -hd)); 
        corners[2] = new Particle((size * 0.25)/2, Point3D.ZERO.add(-hw, hh, -hd));
        corners[3] = new Particle((size * 0.25)/2, Point3D.ZERO.add(hw, hh, -hd));
        corners[4] = new Particle((size * 0.25)/2, Point3D.ZERO.add(-hw, -hh, hd));
        corners[5] = new Particle((size * 0.25)/2, Point3D.ZERO.add(hw, -hh, hd));
        corners[6] = new Particle((size * 0.25)/2, Point3D.ZERO.add(-hw, hh, hd));
        corners[7] = new Particle((size * 0.25)/2, Point3D.ZERO.add(hw, hh, hd));
        Arrays.stream(corners).forEach(c -> {
            c.setMaterial(matC);
        });
        //log.log(Level.INFO, "\nDistance from corner 0 to corner 1: {0}", corners[0].getState().getPosition().distance(corners[1].getState().getPosition()));
        getChildren().addAll(corners);
    }
    
    private void buildLinks(){
        links[0] = new Spring(corners[0].getState(), corners[1].getState());
        links[1] = new Spring(corners[0].getState(), corners[2].getState());
        links[4] = new Spring(corners[0].getState(), corners[4].getState());
        links[3] = new Spring(corners[1].getState(), corners[3].getState());
        links[2] = new Spring(corners[2].getState(), corners[3].getState());
        links[5] = new Spring(corners[4].getState(), corners[5].getState());
        links[6] = new Spring(corners[5].getState(), corners[1].getState());
        links[7] = new Spring(corners[5].getState(), corners[7].getState());
        links[11]= new Spring(corners[6].getState(), corners[2].getState());
        links[10]= new Spring(corners[6].getState(), corners[4].getState());
        links[8] = new Spring(corners[7].getState(), corners[3].getState());
        links[9] = new Spring(corners[7].getState(), corners[6].getState());
        
        Arrays.stream(links).forEach(s->{
            s.updateUI();
        });
        
        getChildren().addAll(links);
    }
    
    private void attachLinks(){
        corners[0].addConstraint(links[0]);
        corners[0].addConstraint(links[1]);
        corners[0].addConstraint(links[4]);
        corners[1].addConstraint(links[3]);
        corners[2].addConstraint(links[2]);
        corners[4].addConstraint(links[5]);
        corners[5].addConstraint(links[6]);
        corners[5].addConstraint(links[7]);
        corners[6].addConstraint(links[11]);
        corners[6].addConstraint(links[10]);
        corners[7].addConstraint(links[8]);
        corners[7].addConstraint(links[9]);
    }
    
    /**=========================================================================
     * 
     */
    
    
    
    /**=========================================================================
     * 
     */
    private final DoubleProperty size = new SimpleDoubleProperty(25);

    public double getSize() {
        return size.get();
    }

    public final void setSize(double value) {
        size.set(value);
    }

    public DoubleProperty sizeProperty() {
        return size;
    }
    
    
}
