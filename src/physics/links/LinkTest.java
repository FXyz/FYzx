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

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxyz.cameras.CameraTransformer;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class LinkTest extends Application {
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private PerspectiveCamera camera;
    private final CameraTransformer cameraTransform = new CameraTransformer();
    private Group root, sphs;

    @Override
    public void start(Stage stage) throws NonInvertibleTransformException {
        camera = new PerspectiveCamera(true);
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().addAll(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setFieldOfView(42);
        camera.setTranslateZ(-2500);
        
        root = new Group();
        sphs = new Group();

        AmbientLight light = new AmbientLight(Color.CHARTREUSE);
        
        cameraTransform.getChildren().add(light);
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());
                
        root.getChildren().add(cameraTransform);
        testVecs(25);

        Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());
        scene.setFill(Color.BLACK);
        
        scene.setCamera(camera);
        
        //First person shooter keyboard movement
        scene.setOnKeyPressed(event -> {
            double change = 10.0;
            //Add shift modifier to simulate "Running Speed"
            if(event.isShiftDown()) { change = 50.0; }
            //What key did the user press?
            KeyCode keycode = event.getCode();
            //Step 2c: Add Zoom controls
            if(keycode == KeyCode.W) { camera.setTranslateZ(camera.getTranslateZ() + change); }
            if(keycode == KeyCode.S) { camera.setTranslateZ(camera.getTranslateZ() - change); }
            //Step 2d: Add Strafe controls
            if(keycode == KeyCode.A) { camera.setTranslateX(camera.getTranslateX() - change); }
            if(keycode == KeyCode.D) { camera.setTranslateX(camera.getTranslateX() + change); }
           
        });
        
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();            
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            
            double modifier = 10.0;
            double modifierFactor = 0.1;
            
            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 50.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraTransform.ry.setAngle(((cameraTransform.ry.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // +
                cameraTransform.rx.setAngle(((cameraTransform.rx.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // - 
                
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraTransform.t.setX(cameraTransform.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.6); // -
                cameraTransform.t.setY(cameraTransform.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.6); // -
            }
        });
        
        stage.setScene(scene);
        stage.show();

        RotateTransition t = new RotateTransition();
        t.setCycleCount(Animation.INDEFINITE);
        t.setDuration(Duration.seconds(10));
        t.setToAngle(359);
        t.setNode(sphs);
        t.setAxis(Point3D.ZERO.add(1, 1, 1));

        //t.play();
    }

    private void testVecs(double radius) throws NonInvertibleTransformException {
        Sphere sp1 = new Sphere(5);
        Sphere sp2 = new Sphere(10);
        sp1.setTranslateX(50);
        sp1.setTranslateY(50);
        sp2.setTranslateX(400);
        sp2.setTranslateY(400);
        root.getChildren().addAll(sp1, sp2);
        for (int i = 0; i < 12 - 1; i++) {
            double theta = i + (1.0 / 32) * i * 2 * Math.PI, pshi = i * (1.0 / 32) * i * 2 * Math.PI;
            
            System.out.println("Theta: " + theta);

            Point3D p1 = Point3D.ZERO.add(20, 0, 10),
                    p2 = Point3D.ZERO.add(100, 40, 20),
                    fwd = p2.subtract(p1).normalize(),
                    rgt = fwd.crossProduct(Point3D.ZERO.add(0, -1, 0)).normalize(),
                    up = rgt.crossProduct(fwd).normalize();

            double mxx = rgt.getX(), mxy = up.getX(), mxz = fwd.getX(), 
                    myx = rgt.getY(), myy = up.getY(), myz = fwd.getY(),
                    mzx = rgt.getZ(), mzy = up.getZ(), mzz = fwd.getZ(), 
                    
                    tx = mxx * p1.getX() + mxy * p1.getY() + mxz * p1.getZ(),
                    ty = myx * p1.getX() + myy * p1.getY() + myz * p1.getZ(),
                    tz = mzx * p1.getX() + mzy * p1.getY() + mzz * p1.getZ();
            
            Affine a = new Affine();
            a.setToTransform(mxx, myx, mzx, tx, mxy, myy, mzy, ty, mxz, myz, mzz, tz);

            Affine b = a.createInverse();

            Sphere s = new Sphere(3);
            s.setTranslateX(radius * Math.sin(theta) * Math.cos(-theta));
            s.setTranslateY(radius * Math.sin(theta) * Math.sin(-theta));
            s.setTranslateZ(radius * Math.cos(-theta));           
            //s.getTransforms().add(a);
            
            sphs.getChildren().add(s);
            System.out.println("fwd: " + s.getTranslateX() + ", up: " + s.getTranslateY() + ", rt: " + s.getTranslateZ() + " :: Angle: " + Math.cos(rgt.multiply(theta).distance(p1)));
        }
        sphs.setTranslateX(400);
        sphs.setTranslateY(300);
        //sphs.setRotate(90);
        sphs.setRotationAxis(Point3D.ZERO.add(1, 1, 0));
        root.getChildren().add(sphs);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
