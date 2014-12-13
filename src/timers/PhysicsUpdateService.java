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
package timers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 * This sample implementation is using Nano Time Should create the
 * BackoffStrategy .?.
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class PhysicsUpdateService extends ScheduledService<Void> {
    private static final Logger logger = Logger.getLogger(PhysicsUpdateService.class.getName());

    private double targetFPS = 60; // most common frame rate
    //private final long maxFPS = 120;   // is this needed?
    private double OPTIMAL_TIME = (1000000000 / targetFPS);
    private double frame = 0;
    private double
            startTime = 0,
            now = 0,
            timePerUpdate = 0,
            lastUpdateTime = System.nanoTime(),
            lastFpsTime = 0;
            
    public double delta;

    private final List<particle.cloth.demo.Particle> physicsBodies = new ArrayList<>();

    public PhysicsUpdateService(long targetFPS) {
        this.setPeriod(Duration.millis(10));
        //this.physicsBodies.addAll(bodies);
    }

    
    @Override
    public final void start() {
        super.start(); 
        if(startTime <= 0){
            startTime = (System.nanoTime());
        }        
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                now = System.nanoTime();
                timePerUpdate = now - lastUpdateTime;
                lastUpdateTime = now;
                delta = timePerUpdate / ((float) OPTIMAL_TIME);

                // update the frame counter
                lastFpsTime += timePerUpdate;
                frame++;
                // update our FPS counter if a second has passed since
                // we last recorded
                if (lastFpsTime >= 1000000000) {                    
                    
                    lastFpsTime = 0;
                    frame = 0;
                }
                //calculate time on one frame, update on the next
                if (frame % 2 == 0) {
                    /*update properties
                    Platform.runLater(()->{
                        currentTime.set(now);
                        lastFrameTime.set(lastLoopTime);
                        deltaTime.set(delta);
                        elapsedTime.set(now - getStartTime());
                    
                    });
                    */
                    

                } else {
                    
                }
                setDelay(Duration.millis((lastUpdateTime - System.nanoTime() + OPTIMAL_TIME) / 1000000));
                return null;
            }

        };
    }
    
    @Override
    protected void failed() {
        super.failed();

    }
    /*
     Made this final so it cannot accidently be overriden or having call to super ommited 
     @succeeded is called on fxThread so good place to update the UI.
     */
    @Override
    final protected void succeeded() {
        super.succeeded();
        //updateUI();   
        
        
    }

}
