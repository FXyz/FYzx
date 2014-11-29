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
package demo2;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Point3D;
import javafx.util.Duration;
import physicsobjects.Body;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class NanoTimer extends ScheduledService<Void> {

    private final long ONE_NANO = 1000000000L;
    private final double ONE_NANO_INV = 1f / 1000000000L;

    private long startTime, previousTime;
    private double frameRate, deltaTime;
    private final NanoThreadFactory tf = new NanoThreadFactory();
    private final List<? extends Body> bodies;

    private int cAcc = 3;

    public NanoTimer(List<? extends Body> bds) {
        super();
        this.bodies = bds;
        this.setPeriod(Duration.millis(16));
        this.setExecutor(Executors.newCachedThreadPool(tf));
    }

    public double getTimeAsSeconds() {
        return getTime() * ONE_NANO_INV;
    }

    public long getTime() {
        return System.nanoTime() - startTime;
    }

    public long getOneSecondAsNano() {
        return ONE_NANO;
    }

    public double getFrameRate() {
        return frameRate;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    private void updateTimer() {
        deltaTime = (getTime() - previousTime) * (1.0f / ONE_NANO);
        frameRate = 1.0f / deltaTime;
        previousTime = getTime();

    }

    @Override
    public void start() {
        super.start();
        if (startTime <= 0) {
            startTime = System.nanoTime();
        }
    }

    @Override
    public void reset() {
        super.reset();
        startTime = System.nanoTime();
        previousTime = getTime();
    }
    private boolean init = true;

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateTimer();
                for (int iteration = 1; iteration <= 4; iteration++) {
                    
                    for (int i = 0; i < cAcc; i++) {
                        bodies.parallelStream().forEach(Body::solveConstraints);
                    }
                    bodies.parallelStream().forEach(sb -> {                        
                        
                        sb.addForce(Point3D.ZERO.add(0,0,0));
                        
                        sb.stepPhysics(getTime(), getDeltaTime());
                        sb.clearForces();
                    });
                }
                return null;
            }
        };
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        bodies.forEach(sb -> {
            sb.updateUI();
        });

    }

    @Override
    protected void failed() {
        getException().printStackTrace(System.err);

    }

    @Override
    public String toString() {
        return "ElapsedTime: " + getTime() + "\nTime in seconds: " + getTimeAsSeconds()
                + "\nFrame Rate: " + getFrameRate()
                + "\nDeltaTime: " + getDeltaTime();
    }

    /*==========================================================================
    
     */
    private class NanoThreadFactory implements ThreadFactory {

        public NanoThreadFactory() {
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "NanoTimerThread");
            t.setDaemon(true);
            return t;
        }

    }
    /*==========================================================================
    
     */
}//=============================================================================
