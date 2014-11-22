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
package fyzx.tests.masses;

import javafx.animation.AnimationTimer;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class ClothSimulator extends AnimationTimer{
    private Cloth cloth;
    private long previousTime = System.currentTimeMillis();
    private long currentTime;
    private final int fixedDeltaTime = 16;
    private final double fixedDeltaTimeSeconds = (double) fixedDeltaTime / 1000.0f;
    private int leftOverDeltaTime;
    private int constraintAccuracy = 3;
    private double mouseInfluenceScalar = 5;
    private double gravity;

    public ClothSimulator(Cloth c) {
        this.cloth = c;
    }
    
    
    @Override
    public void handle(long now) {
     // calculate elapsed time
                currentTime = System.currentTimeMillis();
                long deltaTimeMS = currentTime - previousTime;

                previousTime = currentTime; // reset previous time

                // break up the elapsed time into manageable chunks
                int timeStepAmt = (int) ((double) (deltaTimeMS + leftOverDeltaTime) / (double) fixedDeltaTime);

                // limit the timeStepAmt to prevent potential freezing
                timeStepAmt = Math.min(timeStepAmt, 5);

                // store however much time is leftover for the next frame
                leftOverDeltaTime = (int) deltaTimeMS - (timeStepAmt * fixedDeltaTime);

                // How much to push PointMasses when the user is interacting
                mouseInfluenceScalar = 1.0f / timeStepAmt;

                // update physics
                for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
                    // solve the constraints multiple times
                    // the more it's solved, the more accurate.
                    for (int x = 0; x < constraintAccuracy; x++) {
                        cloth.getMasses().stream().parallel().forEachOrdered((p) -> {
                            p.solveConstraints();
                        });
                        //System.out.println("Solved " + x);
                    }

                    // update each PointMass's position
                    cloth.getMasses().stream().forEachOrdered((p) -> {
                        //p.applyForce(0, gravity);
                        //p.updateInteractions();
                        //System.out.println("Interacted " + p);
                        p.updatePhysics(fixedDeltaTimeSeconds);
                        //System.out.println("SteppedPhysics " + p);                         
                    });
                    cloth.updateMesh();
                    
                }
            }
    
}
