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
package timers;

import java.util.List;
import java.util.concurrent.Executors;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import physics.physicsobjects.Body;

/**
 * Based on TimeStep example from http://gafferongames.com/game-physics
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class StepTimer extends ScheduledService<Void> {

    private double t = 0.0;
    private double dt = 0.1;

    private double currentTime = 0.0f, newTime, deltaTime;
    private double accumulator = 0.0f;
    private final List<Body> bodies;

    public StepTimer(List<Body> bods) {
        this.bodies = bods;
        this.setPeriod(Duration.seconds(dt));
        this.setExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                newTime = System.nanoTime();
                deltaTime = newTime - currentTime;
                currentTime = newTime;

                if (deltaTime > 0.25f) {
                    deltaTime = 0.25f;
                }
                accumulator += deltaTime;

                while(accumulator >= dt) {
                    accumulator -= dt;
                    //step here
                    bodies.parallelStream().forEach(b->{b.stepPhysics(t, deltaTime);});
                    t += dt;
                }
                return null;
            }
        };
    }
    
    

    @Override
    protected void succeeded() {
        super.succeeded(); //To change body of generated methods, choose Tools | Templates.
        System.out.println(t + " : time\n" + currentTime + " : ct\n" + deltaTime + " : dt\n" + accumulator + " : acc\n\n");
    }

}
