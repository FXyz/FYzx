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

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Simulator extends Application {

    private final PerspectiveCamera cam = new PerspectiveCamera();
    private final Group root = new Group();
    private final Group cloth = new Group();    
    private double mouX, mouY, oldMouY, oldMouX;

     
    @Override
    public void start(Stage primaryStage) {

        cam.setFieldOfView(42);
        cam.setNearClip(0.1);
        cam.setFarClip(10000);
        cam.setVerticalFieldOfView(true);

        Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        scene.setCamera(cam);
        scene.setFill(Color.BLACK);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

        createCurtain();
        timer.start();
        
        RotateTransition rt = new RotateTransition();
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(359);
        rt.setDuration(Duration.seconds(60));
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setNode(cloth);
        
        rt.play();
    }
    
    /*
        Cloth curtain
    */
    private final List<MassParticle> masses = new ArrayList<>();
    private final double mouseInfluenceSize = 20;
    private final double mouseTearSize = 8;
    private double mouseInfluenceScalar = 5;
    private double gravity = 980;    
    private final int curtainHeight = 40;
    private final int curtainWidth = 100;
    private final int yStart = 50;
    private final double restingDistances = 5;
    private final double stiffnesses = 1;
    private final double curtainTearSensitivity = 50;
    
    private void createCurtain() {
        int midWidth = (int) (800 / 2 - (curtainWidth * restingDistances) / 2);
        for (int y = 0; y <= curtainHeight; y++) {
            for (int x = 0; x <= curtainWidth; x++) {
                MassParticle p = new MassParticle(midWidth + x * restingDistances, y * restingDistances + yStart, 0);
                p.setGravity(gravity);
                if (x != 0) {
                    p.attachTo((masses.get(masses.size() - 1).getPointMass()), restingDistances, stiffnesses);
                }
                if (y != 0) {
                    p.attachTo((masses.get((y - 1) * (curtainWidth + 1) + x).getPointMass()), restingDistances, stiffnesses);
                }
                if (y == 0) {
                    p.pinTo(p.getX(), p.getY(), p.getZ());
                }
                masses.add(p);
            }
        }
        cloth.getChildren().addAll(masses);
        cloth.setOnMouseEntered(e -> {
            mouX = e.getSceneX();
            mouY = e.getSceneY();

        });
        cloth.setOnMouseMoved(e -> {
            oldMouX = mouX;
            oldMouY = mouY;
            mouX = e.getSceneX();
            mouY = e.getSceneY();

            masses.stream().forEach(m -> {
                if (m.getBoundsInParent().intersects(mouX, mouY, 10, 10) && e.isShiftDown()) {
                    m.setLastX(m.getX() * 0.25); // random numbers just for show
                    m.setLastY(m.getY() * 0.25);
                    //m.setLastZ(m.getLastZ() + 0.125);
                }
            });
            e.consume();
        });

        root.getChildren().add(cloth);
    }
    /*
        Animation Timer
    */
    
    private long previousTime = System.currentTimeMillis();
    private long currentTime;
    private final int fixedDeltaTime = 16;
    private final double fixedDeltaTimeSeconds = (double) fixedDeltaTime / 1000.0f;
    private int leftOverDeltaTime;
    private final int constraintAccuracy = 5;
    
    private AnimationTimer timer = new AnimationTimer() {
        long delay;

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
                    masses.stream().parallel().forEachOrdered((p) -> {
                        p.solveConstraints();
                    });
                }

                masses.stream().forEachOrdered((p) -> {
                    p.applyForce(0, gravity, 0);
                    p.updatePhysics(fixedDeltaTimeSeconds);
                });
            }
        }
    };

    /**=========================================================================
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
