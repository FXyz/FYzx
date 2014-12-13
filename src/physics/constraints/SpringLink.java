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
package physics.constraints;

import javafx.geometry.Point3D;
import physics.physicsobjects.Body;
import physics.physicsobjects.PhysicalState;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class SpringLink implements Link{
    private final double restingDistance;
    private final double stiffness;

    private final PhysicalState p1;
    private final PhysicalState p2;

    public SpringLink(PhysicalState s1, PhysicalState s2, double restingDist, double stiff) {
        this.p1 = s1; 
        this.p2 = s2;

        this.restingDistance = restingDist;
        this.stiffness = stiff;
    }

    // Solve the link constraint
    @Override
    public void solve() {
        // calculate the distance between the two PointMasss
        Point3D diff = p1.getPosition().subtract(p2.getPosition());
        
        double d = diff.magnitude();

        double difference = (restingDistance - d) / d;

        double im1 = 1 / getBody1().getMass();
        double im2 = 1 / getBody2().getMass();
        double scalarP1 = (im1 / (im1 + im2)) * stiffness;
        double scalarP2 = stiffness - scalarP1;

        p1.setPosition(
                new Point3D(
                        (p1.getPosition().getX() + diff.getX() * scalarP1 * difference),
                        (p1.getPosition().getY() + diff.getY() * scalarP1 * difference),
                        (p1.getPosition().getZ() + diff.getZ() * scalarP1 * difference)
                )
        );
        p2.setPosition(
                new Point3D(
                        (p2.getPosition().getX() - diff.getX() * scalarP2 * difference),
                        (p2.getPosition().getY() - diff.getY() * scalarP2 * difference),
                        (p2.getPosition().getZ() - diff.getZ() * scalarP2 * difference)
                )
        );
    }

    @Override
    public Body getBody1() {
        return p1.getBody();
    }

    @Override
    public Body getBody2() {
        return p2.getBody();
    }

    @Override
    public void remove() {
    }
}
