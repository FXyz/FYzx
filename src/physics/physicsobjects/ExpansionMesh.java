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
package physics.physicsobjects;

import java.util.Arrays;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class ExpansionMesh extends MeshView {

    private static final Logger LOG = Logger.getLogger(ExpansionMesh.class.getName());
    private TriangleMesh mesh = new TriangleMesh();

    public ExpansionMesh(double size) {
        this.setWidth(size);
        this.setHeight(size);
        this.setDepth(size);
        setMesh(mesh);
        createMesh(size, size, size);
    }

    public void computeExpand(double distance, int face) throws Exception {
        float[] points = mesh.getPoints().toArray(null);
        System.err.println(Arrays.toString(points));
        System.err.println("Computing: ");
        switch (face) {
            case 1://0,1,2,3
                //0
                points[0] += 200;
                points[1] += 200;
                points[2] += 200;
                //1
                points[3] += 200;
                points[4] += 200;
                points[5] += 200;
                //2
                points[6] += 200;
                points[7] += 200;
                points[8] += 200;
                //3
                points[9] += 200;
                points[10] += 200;
                points[11] += 200;
                break;
            case 2://9,11,13,15
                //9
                points[27] += distance;
                //11
                points[33] += distance;
                //13
                points[39] += distance;

                points[45] += distance;
                break;
            case 3://4,5,6,7
                points[14] += distance;

                points[17] += distance;

                points[20] += distance;

                points[23] += distance;
                break;
            case 4://8,10,12,14
                points[24] -= distance;

                points[30] -= distance;

                points[36] -= distance;

                points[42] -= distance;
                break;
            case 5://16,17,20,21
                points[49] -= distance;

                points[52] -= distance;

                points[61] -= distance;

                points[64] -= distance;
                break;
            case 6://18,19,22,23
                points[55] += distance;

                points[58] += distance;

                points[67] += distance;

                points[70] += distance;
                break;
        }
        //mesh.getPoints().setAll(points, 0, points.length);
        System.err.println("Setting new Values: ");
        //setMesh(mesh);
        System.out.println("\n" + Arrays.toString(points));
    }

    private void createMesh(double width, double height, double depth) {

        if (mesh == null) {
            mesh = new TriangleMesh();
        }
        float hw = (float) (width / 2),
                hh = (float) (height / 2),
                hd = (float) (depth / 2);

        //create points
        mesh.getPoints().addAll(
                // face 1     
                hw, hh, hd,
                hw, hh, -hd,
                hw, -hh, hd,
                hw, -hh, -hd,
                //face 2
                -hw, hh, hd,
                -hw, hh, -hd,
                -hw, -hh, hd,
                -hw, -hh, -hd,
                // face 3
                hw, hh, hd,
                hw, hh, -hd,
                hw, -hh, hd,
                hw, -hh, -hd,
                // face 4
                -hw, hh, hd,
                -hw, hh, -hd,
                -hw, -hh, hd,
                -hw, -hh, -hd,
                // face 5
                hw, hh, hd,
                hw, hh, -hd,
                hw, -hh, hd,
                hw, -hh, -hd,
                // face 6
                -hw, hh, hd,
                -hw, hh, -hd,
                -hw, -hh, hd,
                -hw, -hh, -hd
        );

        mesh.getTexCoords().addAll(
                0, 0
        );

        mesh.getFaces().addAll(
                3, 0, 1, 0, 0, 0, /*face 1*/ 0, 0, 2, 0, 3, 0,
                13, 0, 9, 0, 11, 0, 11, 0, 15, 0, 13, 0,
                4, 0, 5, 0, 7, 0, 7, 0, 6, 0, 4, 0,
                8, 0, 12, 0, 14, 0, 14, 0, 10, 0, 8, 0,
                21, 0, 20, 0, 16, 0, 16, 0, 17, 0, 21, 0,
                19, 0, 18, 0, 22, 0, 22, 0, 23, 0, 19, 0
        );
    }

    private final DoubleProperty distanceFromCenter = new SimpleDoubleProperty(25);

    public double getDistanceFromCenter() {
        return distanceFromCenter.get();
    }

    public void setDistanceFromCenter(double value) {
        distanceFromCenter.set(value);
    }

    public DoubleProperty distanceFromCenterProperty() {
        return distanceFromCenter;
    }

    private final DoubleProperty depth = new SimpleDoubleProperty(25);

    public double getDepth() {
        return depth.get();
    }

    public void setDepth(double value) {
        depth.set(value);
    }

    public DoubleProperty depthProperty() {
        return depth;
    }

    private final DoubleProperty height = new SimpleDoubleProperty(25);

    public double getHeight() {
        return height.get();
    }

    public void setHeight(double value) {
        height.set(value);
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    private final DoubleProperty width = new SimpleDoubleProperty(25);

    public double getWidth() {
        return width.get();
    }

    public void setWidth(double value) {
        width.set(value);
    }

    public DoubleProperty widthProperty() {
        return width;
    }

}
