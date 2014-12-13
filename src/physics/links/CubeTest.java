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
package physics.links;

import java.util.Collections;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class CubeTest extends Application {
    
    @Override
    public void start(Stage stage) {
        SpringCube c = new SpringCube(100);
        c.setTranslateZ(2000);
        
        Group root = new Group();
        root.getChildren().add(c);
        
        Scene scene = new Scene(new StackPane(root), 300, 250, true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());
        scene.setFill(Color.INDIGO);
        scene.setOnMouseClicked(e->{
            c.getChildren().forEach(n->{
                System.out.println(n);
            });
            
        });
        
        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        
        NanoTimer timer = new NanoTimer(Collections.singletonList(c));
        timer.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
