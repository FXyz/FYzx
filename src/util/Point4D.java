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
package util;

import javafx.geometry.Point3D;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Point4D {

    public static final Point4D// Homogenious      public static final Vector3D// Homogenious           
            ONE = new Point4D(1, 1, 1, 1),
            ZERO = new Point4D(0, 0, 0, 0),
            NAN = new Point4D(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

    public double x;
    public double y;
    public double z;
    public double w;

    public Point4D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public Point4D(Point4D source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
        this.w = source.w;
    }

    public Point4D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Point4D(double... values) {
        if (values.length != size()) {
            throw new IllegalArgumentException();
        }
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
        this.w = values[3];
    }

    public double angle(Point4D v) {
        double mag2 = (x * x) + (y * y) + (z * z) + (w * w);
        double vmag2 = (v.x * v.x) + (v.y * v.y) + (v.z * v.z) + (v.w * v.w);
        double dot = (x * v.x) + (y * v.y) + (z * v.z) + (w * v.w);
        return Math.acos(dot / Math.sqrt(mag2 * vmag2));
    }

    public Point4D add(double constant) {
        double nx = x + constant,
               ny = y + constant,
               nz = z + constant,
               nw = w + constant;
        return new Point4D(nx, ny, nz, nw);
    }

    public Point4D add(double dx, double dy, double dz, double dw) {
        double nx = x + dx,
                ny = y + dy,
                nz = z + dz,
                nw = z + dw;
        return new Point4D(nx, ny, nz, nw);
    }

    public Point4D add(Point4D v) {
        double nx = x + v.x,
                ny = y + v.y,
                nz = z + v.z,
                nw = w + v.w;
        return new Point4D(nx, ny, nz, nw);
    }

    public Point4D addMultiply(double dx, double dy, double dz, double dw, double factor) {
        double nx = x + dx * factor,
                ny = y + dy * factor,
                nz = z + dz * factor,
                nw = w + dw * factor;
        return new Point4D(nx, ny, nz, nw);
    }

    public Point4D addMultiply(Point4D v, double factor) {
        double nx = x + v.x * factor,
                ny = y + v.y * factor,
                nz = z + v.z * factor,
                nw = w + v.w * factor;
        return new Point4D(nx, ny, nz, nw);
    }

    public Point4D addProduct(Point4D a, Point4D b) {
        x += a.x * b.x;
        y += a.y * b.y;
        z += a.z * b.z;
        w += a.w * b.w;
        return new Point4D(x, y, z, w);
    }

    public Point4D addProduct(Point4D a, Point4D b, double factor) {
        x += a.x * b.x * factor;
        y += a.y * b.y * factor;
        z += a.z * b.z * factor;
        w += a.w * b.w * factor;
        return new Point4D(x, y, z, w);
    }

    public double magnitudeSquared() {
        return (x * x) + (y * y) + (z * z) + (w * w);
    }

    public double distanceSquared(Point3D v) {
        double dx = x - v.getX(), dy = y - v.getY(), dz = z - v.getZ();
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public double distance(Point3D v) {
        return Math.sqrt(distanceSquared(v));
    }

    public double inverseMagnitude() {
        return 1.0 / Math.sqrt(magnitudeSquared());
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public void normalize() {
        double d = magnitude();
        if (d == 0.0) {
            x = 0;
            y = 0;
            z = 0;
        }
        x /= d;
        y /= d;
        z /= d;        
    }

    public void setAs(Point4D a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public Point4D multiply(double d) {
        return new Point4D(
                x * d,
                y * d,
                z * d,
                w * d
        );
    }

    public Point4D subtractMultiple(Point4D v, double factor) {        
        return new Point4D(
                x - v.x * factor,
                y - v.y * factor,
                z - v.z * factor,
                w - v.w * factor
        );
    }

    public Point4D subtract(Point4D v) {
        double nx  = x - v.x,
               ny  = y - v.y,
               nz  = z - v.z,
               nw  = w - v.w;
        return new Point4D(nx, ny, nz, nw);
    }

    public Point4D subMultiple(Point4D v, double factor) {
        return addMultiply(v, -factor);
    }

    public double dotProduct(Point4D a) {
        return (x * a.x) + (y * a.y) + (z * a.z) + (w * a.w);
    }

    public double dotProduct(double[] data, int offset) {
        return x * data[offset + 0] + y * data[offset + 1] + z * data[offset + 2] + w * data[offset + 3];
    }
    
    public final int size() {
        return 4;
    }

    public double getSum() {
        return x + y + z + w;
    }

    public double getProduct() {
        return x * y * z * w;
    }

    public Point4D scaleAdd(double factor, double constant) {
        x = (x * factor) + constant;
        y = (y * factor) + constant;
        z = (z * factor) + constant;
        w = (w * factor) + constant;
        return new Point4D(x, y, z, w);
    }

    public Point4D scaleAdd(double factor, Point4D constant) {
        x = (x * factor) + constant.x;
        y = (y * factor) + constant.y;
        z = (z * factor) + constant.z;
        w = (w * factor) + constant.w;
        return new Point4D(x, y, z, w);
    }

    public void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Point4D negate() {
        return new Point4D(-x, -y, -z, -w);
    }

    public void getElements(double[] data, int offset) {
        data[offset] = x;
        data[offset + 1] = y;
        data[offset + 2] = z;
        data[offset + 3] = w;
    }

    public double[] toDoubleArray() {
        return new double[]{x, y, z, w};
    }

    public Point4D toNormal() {
        double d = this.magnitude();
        return (d == 0) ? ZERO : new Point4D(x / d, y / d, z / d, w / d);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    
    public double getW(){
        return w;
    }

    public boolean equals(Point4D v) {
        return (x == v.x) && (y == v.y) && (z == v.z) && (w == v.w);
    }

    @Override
    public String toString() {
        return "Vector4D: {X: " + x + ", Y: " + y + ", Z: " + z + ", W: " + w + "}";
    }
}
