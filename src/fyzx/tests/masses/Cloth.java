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
package fyzx.tests.masses;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Cloth extends Group {

    private final List<PointMass> masses = new ArrayList<>();
    private final TriangleMesh mesh = new TriangleMesh();
    private final MeshView view = new MeshView();
    private final PhongMaterial material = new PhongMaterial();
    private int spacing, vertsX, vertsY;
    private int width, height;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double stiffness;
    private double gravity;
    private static final Logger LOG = Logger.getLogger(Cloth.class.getName());
    private ClothSimulator simulator;

    public Cloth(int vertSpacing, int vx, int vy) {
        this.spacing = vertSpacing;
        this.vertsX = vx;
        this.vertsY = vy;
        this.width = vertsX * spacing;
        this.height = vertsY * spacing;

        create();

        this.simulator = new ClothSimulator(this);
        simulator.start();
    }

    private void create() {
        buildMesh(width, height);
        buildLinks();
    }

    private void buildLinks() {
        int index = 0;
        for (int y = 0; y <= height / 64; y++) {
            for (int x = 0; x <= width / 64; x++) {

                // PointMass attachTo parameters: PointMass PointMass, double restingDistance, double stiffness
                // try disabling the next 2 lines (the if statement and attachTo part) to create a hairy effect
                if (x != 0) {
                    masses.get(y * (width / 64) + 1).attachTo((PointMass) (masses.get(masses.size() - 1)), spacing, stiffness);
                }
                // the index for the PointMasss are one dimensions, 
                // so we convert x,y coordinates to 1 dimension using the formula y*width+x  
                if (y != 0) {
                    masses.get(y * (width / 64) + 1).attachTo((PointMass) (masses.get((y - 1) * (width + 1) + x)), spacing, stiffness);
                }

                // we pin the very top PointMasss to where they are
                if (y == 0) {
                    masses.get(y * (width / 64) + 1).pinTo(masses.get(y * (width / 64) + 1).getX(), masses.get(y * (width / 64) + 1).getY(), masses.get(index).getZ());
                }

                index++;
            }
        }
    }

    private void buildMesh(double width, double height) {

        minX = -(float) width / 2f;
        maxX = (float) width / 2f;

        minY = -(float) height / 2f;
        maxY = (float) height / 2f;

        int subDivX = (int) width / 64;
        int subDivY = (int) height / 64;

        final int pointSize = mesh.getPointElementSize();
        final int texCoordSize = mesh.getTexCoordElementSize();
        final int faceSize = mesh.getFaceElementSize();

        int numDivX = subDivX + 1;
        int numVerts = (subDivY + 1) * numDivX;
        float points[] = new float[numVerts * pointSize];
        float texCoords[] = new float[numVerts * texCoordSize];
        int faceCount = subDivX * subDivY * 2;
        int faces[] = new int[faceCount * faceSize];
        float a, b, c;
        // Create points and texCoords
        for (int y = 0; y <= height; y++) {
            float currY = (float) y / subDivY;
            double fy = (1 - currY) * minY + currY * maxY;
            for (int x = 0; x <= width; x++) {
                float currX = (float) x / subDivX;
                double fx = (1 - currX) * minX + currX * maxX;
                int idx = y * numDivX * pointSize + (x * pointSize);

                points[idx + 0] = a = (float) fx; // x
                points[idx + 1] = b = (float) fy; // y
                points[idx + 2] = c = (float) 0;  // z ////////////////////////////////////////////////////////////////////////////////////////////////////

                idx = y * numDivX * texCoordSize + (x * texCoordSize);
                texCoords[idx] = currX;
                texCoords[idx + 1] = currY;

                PointMass av = new PointMass(a, b, c);

                /* PointMass attachTo parameters: PointMass PointMass, double restingDistance, double stiffness
                // try disabling the next 2 lines (the if statement and attachTo part) to create a hairy effect
                if (x != 0) {
                    av.attachTo((PointMass) (masses.get(masses.size() - 1)), spacing, stiffness);
                }
                // the index for the PointMasss are one dimensions, 
                // so we convert x,y coordinates to 1 dimension using the formula y*width+x  
                if (y != 0) {
                    av.attachTo((PointMass) (masses.get((int) ((y - 1) * (width + 1) + x))), spacing, stiffness);
                }

                // we pin the very top PointMasss to where they are
                if (y == 0) {
                    av.pinTo(masses.get(index).getX(), masses.get(index).getY(), masses.get(index).getZ());
                }*/
                masses.add(av);
            }
        }
        // Create faces
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p00 = (int) (y * width + 1+ x);
                int p01 = p00 + 1;
                int p10 = (int) (p00 + width + 1);
                int p11 = p10 + 1;
                int tc00 = (int) (y * width +1 + x);
                int tc01 = tc00 + 1;
                int tc10 = (int) (tc00 + width+1);
                int tc11 = tc10 + 1;

                int idx = (y * subDivX * faceSize + (x * faceSize)) * 2;
                faces[idx + 0] = p00;
                faces[idx + 1] = tc00;
                faces[idx + 2] = p10;
                faces[idx + 3] = tc10;
                faces[idx + 4] = p11;
                faces[idx + 5] = tc11;

                idx += faceSize;
                faces[idx + 0] = p11;
                faces[idx + 1] = tc11;
                faces[idx + 2] = p01;
                faces[idx + 3] = tc01;
                faces[idx + 4] = p00;
                faces[idx + 5] = tc00;
            }
        }
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);

    }
    /*
     public void updateInteractions() {
     // this is where our interaction comes in.
     if (mousePressed) {
     double distanceSquared = distPointToSegmentSquared(oldMouX, oldMouY, mouX, mouY, getX(), getY());
     if (primaryButtonDown) {
     if (distanceSquared < mouseInfluenceSize) { // remember mouseInfluenceSize was squared in setup()
     // To change the velocity of our PointMass, we subtract that change from the lastPosition.
     // When the physics gets integrated (see updatePhysics()), the change is calculated
     // Here, the velocity is set equal to the cursor's velocity
     setLastX(x - (mouX - oldMouX) * mouseInfluenceScalar);
     setLastY(y - (mouY - oldMouY) * mouseInfluenceScalar);
     }
     } else { // if the right mouse button is clicking, we tear the cloth by removing links
     if (distanceSquared < mouseTearSize) {
     links.clear();
     }
     }
     }
     }*/

    public void updateMesh() {
        int index = 0;
        float[] points = mesh.getPoints().toArray(null);
        for (int y = 0; y <= vertsY; y++) {
            for (int x = 0; x <= vertsX; x++) {
                int idx = y * vertsX + 1 * mesh.getPointElementSize() + (x * mesh.getPointElementSize());
                points[idx + 0] = (float) masses.get(index).getX();
                points[idx + 1] = (float) masses.get(index).getY();
                points[idx + 2] = (float) masses.get(index).getZ();
                index++;
            }
        }
        mesh.getPoints().set(0, points, 0, points.length);
    }

    public double getStiffness() {
        return stiffness;
    }

    public void setStiffness(double stiffness) {
        this.stiffness = stiffness;
    }

    public List<PointMass> getMasses() {
        return masses;
    }

    public MeshView getView() {
        return view;
    }

    public PhongMaterial getMaterial() {
        return material;
    }

    public int getSpacing() {
        return spacing;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

}
