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
package physics.integrators;

import javafx.geometry.Point3D;
import physics.physicsobjects.Body;
import physics.physicsobjects.PhysicalState;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class VerletIntegrator implements Integrator {

    private final PhysicalState state;
    private double damping = 1.0;

    public VerletIntegrator(PhysicalState state) {
        this.state = state;
    }

    public VerletIntegrator(PhysicalState s, double damp) {
        this.state = s;
        this.damping = damp;
    }

    @Override
    public void updatePhysics(double t, double dt) {
        // call recalculates velocity, acceleration, and momentum

        Point3D v = state.getPosition().subtract(state.getPrevPosition()).multiply(damping);
        double dtSq = dt * dt;

        // calculate the next position using Verlet Integration
        Point3D nextStatePos = new Point3D(
                state.getPosition().getX() + v.getX() + state.getBody().getNetForce().getX() * 0.5f * dtSq,
                state.getPosition().getY() + v.getY() + state.getBody().getNetForce().getY() * 0.5f * dtSq,
                state.getPosition().getZ() + v.getZ() + state.getBody().getNetForce().getZ() * 0.5f * dtSq
        );

        state.setPrevPosition(state.getPosition());
        state.setPosition(nextStatePos);
        
        state.getBody().clearForces();
    }

    public Body getBody() {
        return state.getBody();
    }

    public double getDamping() {
        return damping;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

}
