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
package particles;

import static java.lang.Math.sqrt;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
// The Link class is used for handling distance constraints between PointMasses.
public class Link {

    private final double restingDistance;
    private final double stiffness;
    private final double tearSensitivity;

    private final PointMass p1;
    private final PointMass p2;

    public Link(PointMass which1, PointMass which2, double restingDist, double stiff, double tearSensitivity) {
        this.p1 = which1; 
        this.p2 = which2;

        this.restingDistance = restingDist;
        this.stiffness = stiff;
        this.tearSensitivity = tearSensitivity;
    }

    // Solve the link constraint
    public void solve() {
        // calculate the distance between the two PointMasss
        double diffX = p1.getX() - p2.getX();
        double diffY = p1.getY() - p2.getY();
        double diffZ = p1.getZ() - p2.getZ();
        double d = sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

        double difference = (restingDistance - d) / d;

        if (d > tearSensitivity) {
            //p1.removeLink(this);
        }

        double im1 = 1 / p1.getMass();
        double im2 = 1 / p2.getMass();
        double scalarP1 = (im1 / (im1 + im2)) * stiffness;
        double scalarP2 = stiffness - scalarP1;

        p1.setX(p1.getX() + diffX * scalarP1 * difference);
        p1.setY(p1.getY() + diffY * scalarP1 * difference);
        p1.setZ(p1.getZ() + diffZ * scalarP1 * difference);
        
        p2.setX(p2.getX() - diffX * scalarP2 * difference);
        p2.setY(p2.getY() - diffY * scalarP2 * difference);
        p2.setZ(p2.getZ() + diffZ * scalarP2 * difference);
    }

}
