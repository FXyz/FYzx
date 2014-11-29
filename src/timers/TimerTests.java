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

import integrators.RK4Integrator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import physicsobjects.Body;
import physicsobjects.PhysicalState;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class TimerTests extends Application {
    private PhongMaterial mat = new PhongMaterial(Color.CHARTREUSE);
    @Override
    public void start(Stage stage) {
        List<Body> bodies = new ArrayList<>();
        IntStream.range(0,10).forEach(i->{
            Body sb;
            sb = new Body(1, Point3D.ZERO.add(i * 10 + 50, i + 25, 0), true) {
                private final Sphere s = new Sphere(5);
                {              
                    s.setMaterial(mat);
                    getChildren().add(s);
                }
                @Override
                public Node getNode() {
                    return s;
                }

                @Override
                public void updateUI() {
                    getAffine().appendTranslation(getState().getPosition().getX(), getState().getPosition().getY(), getState().getPosition().getZ());
                }
            };
            sb.setIntegrator(new RK4Integrator(sb.getState()));
            if(i % 2 == 0){
                sb.setForceAffected(false);
            }
            bodies.add(sb);
        });
        
        Group bods = new Group();
        bods.setTranslateZ(500);
        bods.getChildren().addAll(bodies);
        
        Group root = new Group(bods);
        
        NanoTimer t = new NanoTimer(bodies);        
        
        Scene scene = new Scene(root, 800,600, true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());
        scene.setFill(Color.BLUEVIOLET);
        scene.setOnKeyPressed(e->{
            switch(e.getCode()){                
                case W: 
                    PhysicalState s = bodies.get(0).getState();
                    s.setPosition(s.getPosition().add(1,0,0));
                case S:
                case A:
                case D:
            }
        });
        
        stage.setScene(scene);
        stage.show();
        
        t.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
