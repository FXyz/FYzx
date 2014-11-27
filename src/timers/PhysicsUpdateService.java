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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This sample implementation is using Nano Time Should create the
 * BackoffStrategy .?.
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class PhysicsUpdateService extends ScheduledService<Void> {
    private static final Logger logger = Logger.getLogger(PhysicsUpdateService.class.getName());

    private final long targetFPS = 60; // most common frame rate
    //private final long maxFPS = 120;   // is this needed?
    private final long OPTIMAL_TIME = (long) (1000000000 / targetFPS);
    private long frame = 0;
    private long now = 0,
            updateLength = 0,
            lastLoopTime = System.nanoTime(),
            lastFpsTime = 0;
    public float delta;
    private boolean showStats;

    private final List physicsBodies = new ArrayList<>();

    public PhysicsUpdateService(long targetFPS) {
        this.setPeriod(Duration.millis(10));
        //this.physicsBodies.addAll(bodies);
    }

    //apply half of Forces or other things before solving constraints
    private void preConstraintSolving(double deltaTime) {
        //physicsBodies.parallelStream().forEach(null);
    }

    // solve constraints

    private void solveConstraints(float deltaTime) {
        //physicsBodies.parallelStream().forEach(null);
    }

    //apply other half of Forces or other things after solving constraints

    private void postConstraintSolving(double deltaTime) {
        //physicsBodies.parallelStream().forEach(null);
    }

    // step the Physics

    private void stepPhysics(double deltaTime) {
        //physicsBodies.parallelStream().forEach(null);
    }
    /* handled on fx thread:
     update Nodes Positions / Transforms 
     */

    private void updateUI() {
        //physicsBodies.forEach(null);
    }
    
    @Override
    public final void start() {
        super.start(); 
        if(getStartTime() <= 0){
            startTime.set(System.nanoTime());
        }        
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                now = System.nanoTime();
                updateLength = now - lastLoopTime;
                lastLoopTime = now;
                delta = updateLength / ((float) OPTIMAL_TIME);

                // update the frame counter
                lastFpsTime += updateLength;
                frame++;
                // update our FPS counter if a second has passed since
                // we last recorded
                if (lastFpsTime >= 1000000000) {                    
                    
                    lastFpsTime = 0;
                    frame = 0;
                }
                //calculate time on one frame, update on the next
                if (frame % 2 == 0) {
                    //update properties
                    Platform.runLater(()->{
                        currentTime.set(now);
                        lastFrameTime.set(lastLoopTime);
                        deltaTime.set(delta);
                        elapsedTime.set(now - getStartTime());
                    });
                    
                    setDelay(Duration.millis((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000));

                } else {
                    preConstraintSolving(delta);
                    solveConstraints(delta);
                    postConstraintSolving(delta);
                    stepPhysics(delta);
                }
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
        updateUI();   
        
        if (showStats) {
            logger.log(
                    Level.FINE,
                    "\n******************************\n"
                    + "Start Time in Seconds : {0}\n"
                    + "Current Time seconds: {1}\n"
                    + "Elapsed Time: {2}\n"
                    + "Delta Time: {3}"
                            + "\n******************************\n", 
                    new Object[]{
                        startTime.divide(1000000).divide(1000).get(),
                        currentTime.divide(1000000).divide(1000).get(),
                        elapsedTime.divide(1000000).divide(1000).get(),
                        deltaTime.get()
                    }
            );
        }
    }
    /*==========================================================================
     *   Time Properties
     *///========================================================================
    private final ReadOnlyLongWrapper framesPerSecond = new ReadOnlyLongWrapper(this, "framesPerSecond");

    public final long getFPS() {
        return framesPerSecond.get();
    }

    public ReadOnlyLongProperty fpsProperty() {
        return framesPerSecond.getReadOnlyProperty();
    }
    //==========================================================================
    private final ReadOnlyLongWrapper currentTime = new ReadOnlyLongWrapper(this, "currentTime");

    public long getCurrentTime() {
        return currentTime.get();
    }

    public ReadOnlyLongProperty currentTimeProperty() {
        return currentTime.getReadOnlyProperty();
    }
    //==========================================================================
    private final ReadOnlyLongWrapper elapsedTime = new ReadOnlyLongWrapper(this, "elapsedTime");

    public long getElapsedTime() {
        return elapsedTime.get();
    }

    public ReadOnlyLongProperty elapsedTimeProperty() {
        return elapsedTime.getReadOnlyProperty();
    }
    //==========================================================================
    private final ReadOnlyFloatWrapper deltaTime = new ReadOnlyFloatWrapper(this, "deltaTime");

    public float getDeltaTime() {
        return deltaTime.get();
    }

    public ReadOnlyFloatProperty deltaTimeProperty() {
        return deltaTime.getReadOnlyProperty();
    }
    //==========================================================================
    private final ReadOnlyLongWrapper startTime = new ReadOnlyLongWrapper(this, "startTime");

    public long getStartTime() {
        return startTime.get();
    }
    //==========================================================================
    private final ReadOnlyLongWrapper lastFrameTime = new ReadOnlyLongWrapper(this, "lastFrameTime");

    public long getLastFrameTime() {
        return lastFrameTime.get();
    }

    public ReadOnlyLongProperty lastFrameTimeProperty() {
        return lastFrameTime.getReadOnlyProperty();
    }
// =============================================================================
    /*
     create a simple stage with labels showing stats.
     */
    private Stage stage = new Stage();

    public void showStats(boolean b) {
        showStats = b;        
    }

}
