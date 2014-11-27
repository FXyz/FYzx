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
package integrators;

import javafx.geometry.Point3D;
import physicsobjects.Body;
import physicsobjects.BodyState;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class RK4Integrator implements Integrator {
    private final Body body;
    
    public RK4Integrator(Body b) {
        this.body = b;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void updatePhysics(double t, double dt) {
        integrate(getBody().getState(), t, dt);
    }

    private void integrate(BodyState state, double t, double dt) {
        Derivative a = evaluate(state, t);
        Derivative b = evaluate(state, t, dt * 0.5f, a);
        Derivative c = evaluate(state, t, dt * 0.5f, b);
        Derivative d = evaluate(state, t, dt, c);

        Point3D derivedPosition = new Point3D(
                1.0 / 6.0 * (a.getVelocity().getX() + 2.0f * (b.getVelocity().getX() + c.getVelocity().getX()) + d.getVelocity().getX()),
                1.0 / 6.0 * (a.getVelocity().getY() + 2.0f * (b.getVelocity().getY() + c.getVelocity().getY()) + d.getVelocity().getY()),
                1.0 / 6.0 * (a.getVelocity().getZ() + 2.0f * (b.getVelocity().getZ() + c.getVelocity().getZ()) + d.getVelocity().getZ())        
        );
        Point3D derivedVelocity = new Point3D(
                1.0 / 6.0 * (a.getAcceleration().getX() + 2.0f * (b.getAcceleration().getX() + c.getAcceleration().getX()) + d.getAcceleration().getX()),
                1.0 / 6.0 * (a.getAcceleration().getY() + 2.0f * (b.getAcceleration().getY() + b.getAcceleration().getY()) + d.getAcceleration().getY()),
                1.0 / 6.0 * (a.getAcceleration().getZ() + 2.0f * (b.getAcceleration().getZ() + b.getAcceleration().getZ()) + d.getAcceleration().getZ())        
        ); 
        
        getBody().setState(new BodyState(state.getPosition().add(derivedPosition.multiply(dt)),state.getVelocity().add(derivedVelocity.multiply(dt))));
    }

    private Derivative evaluate(BodyState initial, double t) {
        return new Derivative(initial.getVelocity(), acceleration(initial, t));
    }

    private Derivative evaluate(BodyState initial, double t, double dt, Derivative d) {
        BodyState state = new BodyState(
                initial.getPosition().add(
                        d.getVelocity().multiply(dt).getX(),
                        d.getVelocity().multiply(dt).getY(),
                        d.getVelocity().multiply(dt).getZ()
                ),
                initial.getVelocity().add(
                        d.getAcceleration().multiply(dt).getX(),
                        d.getAcceleration().multiply(dt).getY(),
                        d.getAcceleration().multiply(dt).getZ()
                )
        );
        return new Derivative(state.getVelocity(), acceleration(initial, t));
    }

    private Point3D acceleration(BodyState state, double t) {
        double k = 10;
        double b = 1;
        return new Point3D(
                -(k * state.getPosition().getX()) - (b * state.getVelocity().getX()),
                -(k * state.getPosition().getY()) - (b * state.getVelocity().getY()),
                -(k * state.getPosition().getZ()) - (b * state.getVelocity().getZ())
        );
    }
    /*==============================================================================
    
     */

    private class Derivative {

        private Point3D velocity, acceleration;

        public Derivative(Point3D v, Point3D a) {
            this.velocity = new Point3D(v.getX(), v.getY(), v.getZ());
            this.acceleration = new Point3D(a.getX(), a.getY(), a.getZ());
        }

        public Point3D getVelocity() {
            return velocity;
        }

        public void setVelocity(Point3D velocity) {
            this.velocity = velocity;
        }

        public Point3D getAcceleration() {
            return acceleration;
        }

        public void setAcceleration(Point3D acceleration) {
            this.acceleration = acceleration;
        }
    }//*************************************************************************
}
