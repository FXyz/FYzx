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

import com.sun.javafx.Utils;
import java.util.function.BiFunction;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import physics.constraints.Constraint;
import physics.physicsobjects.PhysicalState;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Spring extends Group implements Constraint{
    private final PhysicalState state1, state2;
    private SpringLine l;
    
    public Spring(PhysicalState p1, PhysicalState p2) {
        this.state1 = p1; this.state2 = p2;
        l = new SpringLine(Color.CHARTREUSE);
        this.restingDistance.set(p1.getPosition().distance(p2.getPosition()));
        this.getChildren().add(l);
        //updateUI();
    }
    
    public final void updateUI(){
        this.l.buildMesh(state1.getPosition(), state2.getPosition(), 2);
    }
    
    private final BiFunction<PhysicalState, PhysicalState, Void> solver = (p1,p2)->{
        // calculate the distance between the two PointMasss
        Point3D diff = p1.getPosition().subtract(p2.getPosition());
        
        double d = diff.magnitude();

        double difference = (getRestingDistance() - d) / d;

        double im1 = 1 / p1.getBody().getMass();
        double im2 = 1 / p2.getBody().getMass();
        double scalarP1 = (im1 / (im1 + im2)) * getStrength();
        double scalarP2 = getStrength() - scalarP1;

        p1.setPosition(
                new Point3D(
                        Utils.clamp(0.1,(p1.getPosition().getX() + diff.getX() * scalarP1 * difference),1),
                        Utils.clamp(0.1,(p1.getPosition().getY() + diff.getY() * scalarP1 * difference),1),
                        Utils.clamp(0.1,(p1.getPosition().getZ() + diff.getZ() * scalarP1 * difference),1)
                ).normalize()
        );
        p2.setPosition(
                new Point3D(
                        Utils.clamp(0.1,(p2.getPosition().getX() - diff.getX() * scalarP2 * difference),1),
                        Utils.clamp(0.1,(p2.getPosition().getY() - diff.getY() * scalarP2 * difference),1),
                        Utils.clamp(0.1,(p2.getPosition().getZ() - diff.getZ() * scalarP2 * difference),1)
                ).normalize()
        );
        //updateUI();
        return null;
    };

    public BiFunction<PhysicalState, PhysicalState, Void> getSolver() {
        return solver;
    }
    
    
    private final DoubleProperty strength = new SimpleDoubleProperty(0.5);

    public double getStrength() {
        return strength.get();
    }

    public void setStrength(double value) {
        strength.set(value);
    }

    public DoubleProperty strengthProperty() {
        return strength;
    }
    
    
    private final DoubleProperty restingDistance = new SimpleDoubleProperty(1);

    public double getRestingDistance() {
        return restingDistance.get();
    }

    public void setRestingDistance(double value) {
        restingDistance.set(value);
    }

    public DoubleProperty restingDistanceProperty() {
        return restingDistance;
    }

    @Override
    public void solve() {
        solver.apply(state1, state2);
    }
    
    
}
