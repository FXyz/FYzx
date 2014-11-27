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
package util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class MouseTracker {

    private double mouX, mouY, lastMX, lastMY, deltaMX, deltaMY;
    private Scene scene;
    private EventHandler updatePositionHandler;

    public MouseTracker() {
    }

    public MouseTracker(Scene scene) {
        this.scene = scene;
        initialize();
    }

    public final void initialize() {
        updatePositionHandler = (EventHandler<MouseEvent>) (MouseEvent e) -> {
            lastMX = mouX;
            lastMY = mouY;
            mouX = e.getSceneX();
            mouY = e.getSceneY();
            deltaMX = mouX - lastMX;
            deltaMY = mouY - lastMY;
            if(e.isMiddleButtonDown())
                System.out.println("getPosition(): " + getPosition() + "\ngetCamLocalPosition(): " + getCamLocalPosition(scene.getCamera()));
        };
        scene.addEventHandler(MouseEvent.ANY, updatePositionHandler);
    }

    public double getX() {
        return mouX;
    }

    public double getY() {
        return mouY;
    }

    public double getLastX() {
        return lastMX;
    }

    public double getLastY() {
        return lastMY;
    }

    public double getDeltaX() {
        return deltaMX;
    }

    public double getDeltaY() {
        return deltaMY;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void removeMouseTracker() {
        scene.removeEventHandler(MouseEvent.ANY, updatePositionHandler);
    }

    public Point2D getPosition() {
        return Point2D.ZERO.add(getX(), getY());
    }

    public Point2D getDelta() {
        return Point2D.ZERO.add(getDeltaX(), getDeltaY());
    }

    public Point3D getCamLocalPosition(Camera cam) {
        try {
            Affine t = new Affine();
            t.setToTransform(cam.getLocalToSceneTransform().createInverse());
            t = AffineUtils.multiply(
                    t,
                    Point3D.ZERO.add(getX(), getY(), 1.0)
            );
            return Point3D.ZERO.add(t.getTx(), t.getTy(), t.getTz());
        } catch (NonInvertibleTransformException ex) {
            Logger.getLogger(MouseTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Could not compute your request");
    }
}
