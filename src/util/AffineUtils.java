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
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class AffineUtils {
    public static Affine multiply(Affine src, Affine other) {
        Affine tmp = new Affine();
        
        tmp.setMxx(src.getMxx() * other.getMxx());
        tmp.setMxy(src.getMxy() * other.getMxy());
        tmp.setMxz(src.getMxz() * other.getMxz());
        tmp.setMyx(src.getMyx() * other.getMyx());
        tmp.setMyy(src.getMyy() * other.getMyy());
        tmp.setMyz(src.getMyz() * other.getMyz());
        tmp.setMzx(src.getMzx() * other.getMzx());
        tmp.setMzy(src.getMzy() * other.getMzy());
        tmp.setMzz(src.getMzz() * other.getMzz());     
        
        return tmp;
    }
    
    public static Affine multiply(Affine src, Point3D s) {
        Affine tmp = new Affine();
        
        tmp.setMxx(src.getMxx() * s.getX());
        tmp.setMxy(src.getMxy() * s.getY());
        tmp.setMxz(src.getMxz() * s.getZ());
        tmp.setMyx(src.getMyx() * s.getX());
        tmp.setMyy(src.getMyy() * s.getY());
        tmp.setMyz(src.getMyz() * s.getZ());
        tmp.setMzx(src.getMzx() * s.getX());
        tmp.setMzy(src.getMzy() * s.getY());
        tmp.setMzz(src.getMzz() * s.getZ());
        
        return tmp;
    }
    
    public static Point3D getMouseToLocal(Node n, Point4D m){
        try {
            Transform t = n.getLocalToSceneTransform().createInverse();
            return new Point3D(
                    (t.getMxx() * m.getX()) + (t.getMxy() * m.getY()) + (t.getMxz() * m.getZ()) + (t.getTx() * m.getW()),
                    (t.getMyx() * m.getX()) + (t.getMyy() * m.getY()) + (t.getMyz() * m.getZ()) + (t.getTy() * m.getW()),
                    (t.getMzx() * m.getX()) + (t.getMzy() * m.getY()) + (t.getMzz() * m.getZ()) + (t.getTz() * m.getW())
            );
        } catch (NonInvertibleTransformException ex) {
            Logger.getLogger(AffineUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void setAbsoluteValues(Affine mat) {
        mat.setMxx(Math.abs(mat.getMxx()));
        mat.setMxy(Math.abs(mat.getMxy()));
        mat.setMxz(Math.abs(mat.getMxz()));
        mat.setMyx(Math.abs(mat.getMyx()));
        mat.setMyy(Math.abs(mat.getMyy()));
        mat.setMyz(Math.abs(mat.getMyz()));
        mat.setMzx(Math.abs(mat.getMzx()));
        mat.setMzy(Math.abs(mat.getMzy()));
        mat.setMzz(Math.abs(mat.getMzz()));
    }
    
    public static void setAnglesZYX(Affine mat, double aX, double aY, double aZ) {
        double cx = Math.cos(Math.toRadians(aX));
        double cY = Math.cos(Math.toRadians(aY));
        double cZ = Math.cos(Math.toRadians(aZ));
        double sX = Math.sin(Math.toRadians(aX));
        double sY = Math.sin(Math.toRadians(aY));
        double sZ = Math.sin(Math.toRadians(aZ));
        double cc = cx * cZ;
        double cs = cx * sZ;
        double sc = sX * cZ;
        double ss = sX * sZ;
        
        mat.setMxx(cY * cZ); mat.setMyx(sY * sc - cs); mat.setMzx(sY * cc + ss);
        mat.setMxy(cY * sZ); mat.setMyy(sY * ss + cc); mat.setMzy(sY * cs - sc);
        mat.setMxz(-sY);     mat.setMyz(cY * sX);      mat.setMzz(cY * cx);
    }

}
