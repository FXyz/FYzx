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
package demo2;

import constraints.Constraint;
import integrators.VerletIntegrator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Point3D;
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
public class NanoTimerDemo extends Application {

    private static final Logger log = Logger.getLogger(NanoTimerDemo.class.getName());
    
    private final PerspectiveCamera cam = new PerspectiveCamera();
    private final Group root = new Group();
    private Scene scene;
    private final AnchorPane pane = new AnchorPane();
    private final Group cloth = new Group();
    private final double sceneWidth = 1200, sceneHeight = 800;

    @Override
    public void start(Stage stage) {
        
        createCurtain();
        cam.setTranslateZ(-2000.0);
        
        scene = new Scene(root, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setCamera(cam);
        scene.setFill(Color.BLUEVIOLET);
        
        root.getChildren().addAll(cam, cloth);
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
        
        NanoTimer t = new NanoTimer(particles);
        
        t.start();
    }

    /*==========================================================================
     Cloth curtain
     */
    private final List<Particle> particles = new ArrayList<>();
    private final double mouseInfluenceSize = 20;
    private final double mouseTearSize = 35;
    private double mouseInfluenceScalar = 5;
    private final int curtainHeight = 5;
    private final int curtainWidth = 5;
    private final int yStart = 75;
    private final double spacing = 25;
    private final double stiffnesses = 1;
    
    private void createCurtain() {
        int midWidth = (int) (sceneWidth / 2 - (curtainWidth * spacing) / 2);
        for (int y = 0; y <= curtainHeight; y++) {
            for (int x = 0; x <= curtainWidth; x++) {
                Particle p = new Particle(
                        new Point3D(
                                midWidth + x * spacing,
                                y * spacing + yStart,
                                0
                        )
                );
                p.setIntegrator(new VerletIntegrator(p.getState(), 0.99));
                p.addConstraint((Constraint)() -> {
                    if(p.getState().getPosition().getX() <= 1){
                        p.getState().setPosition(p.getState().getPosition().add(Point3D.ZERO.add(1,0,0)));
                    }
                    if(p.getState().getPosition().getX() >= sceneWidth){
                        p.getState().setPosition(p.getState().getPosition().add(Point3D.ZERO.add(-1,0,0)));
                    }
                    if(p.getState().getPosition().getY() <= 1){
                        p.getState().setPosition(p.getState().getPosition().add(Point3D.ZERO.add(0,1,0)));
                    }
                    if(p.getState().getPosition().getY() >= sceneHeight){
                        p.getState().setPosition(p.getState().getPosition().add(Point3D.ZERO.add(0,-1,0)));
                    }
                    if(p.isPinned()){
                        p.getState().setPosition(p.getState().getPosition());
                    }
                });
                if (x != 0) {
                    //p.attachTo((particles.get(particles.size() - 1)), spacing, stiffnesses);
                }
                if (y != 0) {
                    //p.attachTo((particles.get((y - 1) * (curtainWidth + 1) + x)), spacing, stiffnesses);
                }
                if (x == 0) {
                    p.pinTo(p.getState().getPosition());
                }
                
                particles.add(p);
            }
        }
        cloth.getChildren().addAll(particles);
        
        log.log(Level.INFO, "Created masses");
    }

//******************************************************************************
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
