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
package particle.cloth.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        cam.setFieldOfView(42);
        cam.setNearClip(0.1);
        cam.setFarClip(10000);
        cam.setVerticalFieldOfView(true);
        cam.setTranslateY(-100);

        scene = new Scene(root, 1900, 700, true, SceneAntialiasing.BALANCED);
        scene.setCamera(cam);
        scene.setFill(Color.BLACK);
        scene.setOnMouseMoved(e -> {
            masses.forEach(m -> {
                if (e.getPickResult().getIntersectedNode() != null) {
                    m.applyForce(1/e.getX(), 1/e.getY(), e.getZ() * 2);
                }
            });
        });

        root.getChildren().addAll(cam, cloth);

        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.setMaximized(true);
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
    private final int curtainWidth = 200;
    private final int yStart = 150;
    private final double spacing = 3;
    private final double stiffnesses = 0.9;

    private void createCurtain() {
        int midWidth = (int) (1600 / 2 - (curtainWidth * spacing) / 2);
        for (int y = 0; y <= curtainHeight; y++) {
            for (int x = 0; x <= curtainWidth; x++) {
                Particle p = new Particle(midWidth + x * spacing, y * spacing + yStart, 0);
                p.setMass(2);
                if (x != 0) {
                    p.attachTo((masses.get(masses.size() - 1).getPointMass()), spacing, stiffnesses);
                }
                if (y != 0) {
                    p.attachTo((masses.get((y - 1) * (curtainWidth + 1) + x).getPointMass()), spacing, stiffnesses);
                }

                /*pin the 3 verts in the top 2 corners
                 if (y == 0) {
                 thisParticle.pinTo(thisParticle.getX(), thisParticle.getY(), thisParticle.getZ());
                 }
                 */                
                if (y == 0 && x % 10 < 2) {
                    p.pinTo(p.getX(), p.getY(), p.getZ());
                }

                masses.add(p);
            }
        }
        cloth.getChildren().addAll(masses);

        log.log(Level.INFO, "Created masses");
    }

    /**
     * =========================================================================
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
