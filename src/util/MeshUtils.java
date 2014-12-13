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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javafx.collections.ObservableFloatArray;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class MeshUtils {

    /**
     * Creates a Point3D starting at index provided in array ... No check for
     * AIOB exceptions
     */
    public static final BiFunction<Integer, float[], Point3D> ToPoint3D = (i, a) -> {
        float[] p = new float[]{a[i]};
        System.out.println(Arrays.toString(p) + " :: " + p.length);
        return new Point3D(
            0, 0, 0    
        );
    };
    /**
     * Creates a list of Point3D from provided array
     */
    public static final Function<ObservableFloatArray, List<Point3D>> PointsToList = (ofa) -> {
        List<Point3D> pnts = new ArrayList<>();
        for (int i = 0; i < (ofa.size() - 1) - 3; i++) {
            pnts.add(ToPoint3D.apply(i, ofa.toArray(null)));
        }
        return pnts;
    };

    public static final BiFunction<TriangleMesh, float[], Void> ReplaceAllPoints = (t, a) -> {
        if (a.length != t.getPoints().size()) {
            throw new UnsupportedOperationException("Provided Array has a different size than Mesh");
        } else {
            t.getPoints().setAll(a, 0, a.length);
        }
        return null;
    };

    /*
     faces have  p1, t1, p2, t2, p3, t3
     we want p1, p2, p3
     */
    public static final Function<int[], int[]> CollectPointIndiciesFromFace = (faceValues) -> {
        return new int[]{
            faceValues[0], faceValues[2], faceValues[4]
        };
    };

    /*
     faces have  p1, t1, p2, t2, p3, t3
     we want t1, t2, t3
     */
    public static final Function<int[], int[]> CollectTexIndiciesFromFace = (faceValues) -> {
        return new int[]{
            faceValues[1], faceValues[3], faceValues[5]
        };
    };
    /*
     faces have 6 values 
     p1, t1, p2, t2, p3, t2
     */
    public static final BiFunction<Integer, TriangleMesh, int[]> CollectFaceValues = (index, mesh) -> {
        if (index > ((mesh.getFaces().size() - 1) - mesh.getFaceElementSize())) {
            return null;
        }
        if (index > 0) {
            index = (index * 6);
            return mesh.getFaces().toArray(index, null, 6);
        }
        return mesh.getFaces().toArray(index, null, index + 6);
    };

    @FunctionalInterface
    public interface BoundsCollider<T extends Bounds> {
        
        public void handleCollision(T self, T other);
       
        default void checkCollisions(T self, List<T> others) {
            Collector<T,?,List<T>> col = Collectors.toList();
            others.parallelStream().filter((notMe) -> {
                        return !notMe.equals(self);
                    }
            ).collect(col).stream().filter((colliding)->{
                        return colliding.intersects(self);
                    }
            ).forEach((other)->{
                handleCollision(self, other);
            });
        }       
        
    }

    @FunctionalInterface
    public interface Collider<T extends Node>{
        public T getColliderNode();
    }
}
