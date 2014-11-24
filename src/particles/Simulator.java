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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
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
        
        PointLight light = new PointLight(Color.AQUA);
        light.setTranslateZ(-300);
        light.setTranslateY(-150);

        scene = new Scene(root, 1024, 668, true, SceneAntialiasing.BALANCED);
        scene.setCamera(cam);
        scene.setFill(Color.BLACK);

        root.getChildren().addAll(cam, light, cloth);
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();

        timer.start();

    }

    /*==========================================================================
     Cloth curtain
     */
    private final List<Particle> masses = new ArrayList<>();
    private final double mouseInfluenceSize = 20;
    private final double mouseTearSize = 35;
    private double mouseInfluenceScalar = 5;
    private final double gravity = 300;
    private final int curtainHeight = 75;
    private final int curtainWidth = 150;
    private final int yStart = 75;
    private final double spacing = 5;
    private final double stiffnesses = 1;
    private final double curtainTearSensitivity = 50;

    private void createCurtain() {
        int midWidth = (int) (1024 / 2 - (curtainWidth * spacing) / 2);
        for (int y = 0; y <= curtainHeight; y++) {
            for (int x = 0; x <= curtainWidth; x++) {
                Particle thisParticle = new Particle(midWidth + x * spacing, y * spacing + yStart, 0);

                if (x != 0) {
                    thisParticle.attachTo((masses.get(masses.size() - 1).getPointMass()), spacing, stiffnesses);
                }
                if (y != 0) {
                    thisParticle.attachTo((masses.get((y - 1) * (curtainWidth + 1) + x).getPointMass()), spacing, stiffnesses);
                }

                //pin the 3 verts in the top 2 corners
                if (y == 0 && x == 0) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 0 && x <= 10) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 1 && x == 0) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 0 && x%15<2) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 0 && x == curtainWidth) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }
                if (y == 1 && x == curtainWidth) {
                    thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                }

                masses.add(thisParticle);
            }
        }
        cloth.getChildren().addAll(masses);
        
        cloth.setOnMousePressed(e->{
            
        });
        log.log(Level.INFO, "Created masses");
    }
    /*
     Animation Timer
     */

    private long previousTime = System.currentTimeMillis();
    private long currentTime;
    private final int fixedDeltaTime = 16;
    private final double fixedDeltaTimeSeconds = (double) fixedDeltaTime / 1000.0f;
    private int leftOverDeltaTime;
    private final int constraintAccuracy = 4;

    private final AnimationTimer timer = new AnimationTimer() {
        
        @Override
        public void handle(long now) {
            
            // calculate elapsed time
            currentTime = System.currentTimeMillis();
            long deltaTimeMS = currentTime - previousTime;
            previousTime = currentTime;
            int timeStepAmt = (int) ((double) (deltaTimeMS + leftOverDeltaTime) / (double) fixedDeltaTime);
            timeStepAmt = Math.min(timeStepAmt, 5);
            leftOverDeltaTime = (int) deltaTimeMS - (timeStepAmt * fixedDeltaTime);
            mouseInfluenceScalar = 1.0f / timeStepAmt;
          
            for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
                for (int x = 0; x < constraintAccuracy; x++) {                    
                    masses.parallelStream().forEach(Particle::solveConstraints);
                    
                }
                //log.log(Level.INFO, "Done Solving Constraints");
                masses.parallelStream().forEach(p->{
                    p.applyForce(0, gravity, 0); 
                    p.updateInteractions(); 
                    p.updatePhysics(fixedDeltaTimeSeconds); 
                });
                if(iteration==timeStepAmt){
                    masses.stream().forEach(Particle::updateUI);
                }
            }     
            log.log(Level.INFO, "Updated Physics and UI {0}", (System.currentTimeMillis()-currentTime));
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
