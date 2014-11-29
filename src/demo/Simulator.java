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
package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Simulator extends Application {

    private static final Logger log = Logger.getLogger(Simulator.class.getName());

    private final PerspectiveCamera cam = new PerspectiveCamera();
    private final Group root = new Group();
    private Scene scene;
    private final AnchorPane pane = new AnchorPane();
    private final Group cloth = new Group();

    @Override
    public void start(Stage stage) {
        log.setLevel(Level.ALL);

        createCurtain();
        

        scene = new Scene(root, 1200, 700, true, SceneAntialiasing.BALANCED);
        scene.setCamera(cam);
        scene.setFill(Color.DARKBLUE);

        root.getChildren().addAll(cam, cloth);
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
        
        NanoTimer t = new NanoTimer(masses);
        t.start();
        //timer.start();

    }

    /*==========================================================================
     Cloth curtain
     */
    private final List<Particle> masses = new ArrayList<>();
    private final double mouseInfluenceSize = 20;
    private final double mouseTearSize = 35;
    private double mouseInfluenceScalar = 5;
    private final double gravity = 300;
    private final int curtainHeight = 100;
    private final int curtainWidth = 300;
    private final int yStart = 75;
    private final double spacing =3.25;
    private final double stiffnesses = 1.15;

    private void createCurtain() {
        int midWidth = (int) (1200 / 2 - (curtainWidth * spacing) / 2);
        for (int y = 0; y <= curtainHeight; y++) {
            for (int x = 0; x <= curtainWidth; x++) {
                Particle thisParticle = new Particle(midWidth + x * spacing, y * spacing + yStart, 0);

                if (x != 0) {
                    thisParticle.attachTo((masses.get(masses.size() - 1).getPointMass()), spacing, stiffnesses);
                }
                if (y != 0) {
                    thisParticle.attachTo((masses.get((y - 1) * (curtainWidth + 1) + x).getPointMass()), spacing, stiffnesses);
                }

                /*pin the 3 verts in the top 2 corners
                if (y == 0) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                */if (y == 0 && x % 15 < 2) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }

                masses.add(thisParticle);
            }
        }
        cloth.getChildren().addAll(masses);
        
        log.log(Level.INFO, "Created masses");
    }
    /*
     Animation Timer
     */

    private long previousTime = System.currentTimeMillis();
    private long currentTime;
    private final int fixedDeltaTime = 16; // in Milliseconds
    private final double fixedDeltaTimeSeconds = (double) fixedDeltaTime / 1000.0f;
    private int leftOverDeltaTime;
    private final int constraintAccuracy = 5;

    private final AnimationTimer timer = new AnimationTimer() {

        @Override
        public void handle(long now) {

            // calculate elapsed time
            currentTime = System.currentTimeMillis();
            long delta = currentTime - previousTime;
            previousTime = currentTime;
            
            int timeStepAmt = (int) ((double) (delta + leftOverDeltaTime) / (double) fixedDeltaTime);
            timeStepAmt = Math.min(timeStepAmt, 5);
            leftOverDeltaTime = (int) delta - (timeStepAmt * fixedDeltaTime);
            mouseInfluenceScalar = 1.0f / timeStepAmt;

            for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
                for (int x = 0; x < constraintAccuracy; x++) {
                    masses.parallelStream().forEach(Particle::solveConstraints);
                }
                //log.log(Level.INFO, "Done Solving Constraints");
                masses.parallelStream().forEach(p -> {
                    p.applyForce(Math.random(), gravity, 12 * Math.random());
                    //p.updateInteractions(); 
                    p.updatePhysics(fixedDeltaTimeSeconds);
                });
                if (iteration == timeStepAmt) {
                    masses.forEach(Particle::updateUI);
                }
                //log.log(Level.INFO, "Iteration {0}", (iteration));
            }

            //log.log(Level.INFO, "Updated Physics and UI {0}", (System.currentTimeMillis()-currentTime));
            //log.log(Level.INFO, "TimeStepAmount {0}", (timeStepAmt));
        }
    };

    /**
     * =========================================================================
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
