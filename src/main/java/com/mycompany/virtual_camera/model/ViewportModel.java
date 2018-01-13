package com.mycompany.virtual_camera.model;

import com.mycompany.virtual_camera.model.spatial_shape.SpatialShapesCollection;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Paweł Mac
 */
public class ViewportModel extends Observable {
    
    private static final int IDENTITY_MATRIX          =  0;
    private static final int MOVE_FORWARD_MATRIX      =  1;
    private static final int MOVE_BACKWARD_MATRIX     =  2;
    private static final int MOVE_LEFT_MATRIX         =  3;
    private static final int MOVE_RIGHT_MATRIX        =  4;
    private static final int MOVE_UPWARD_MATRIX       =  5;
    private static final int MOVE_DOWNWARD_MATRIX     =  6;
    private static final int ROTATE_LEFT_MATRIX       =  7;
    private static final int ROTATE_RIGHT_MATRIX      =  8;
    private static final int ROTATE_UPWARD_MATRIX     =  9;
    private static final int ROTATE_DOWNWARD_MATRIX   = 10;
    private static final int ROTATE_TILT_LEFT_MATRIX  = 11;
    private static final int ROTATE_TILT_RIGHT_MATRIX = 12;
    private static final int NUMBER_OF_MATRICES        = 13;
    
    private final RealMatrix[] geometricTransformationMatrices = new RealMatrix[NUMBER_OF_MATRICES];
    private final int viewportWidth;
    private final int viewportHeight;
    private final Set<Point3D> point3DsSet;
    private final Set<Edge3D> edge3DsSet;
    private final List<Face3D> face3DsList;
    private double focalDistance = 200;// distance between observer and viewport
    private double step = 10.0d;
    private double angleInDegrees = 1.0d;
    
    public ViewportModel(int viewportWidth, int viewportHeight, SpatialShapesCollection spatialShapesCollection) {
        this.initGeometricTransformationMatrices();
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.point3DsSet = spatialShapesCollection.getPoint3DsSet();
        this.edge3DsSet = spatialShapesCollection.getEdge3DsSet();
        this.face3DsList = spatialShapesCollection.getFace3DsList();
        this.update(geometricTransformationMatrices[IDENTITY_MATRIX]);
    }
    
    // Getters and Setters
    
    public double getFocalDistance() {
        return focalDistance;
    }
    
    public void setFocalDistance(double focalDistance) {
        this.focalDistance = focalDistance;
        this.update(geometricTransformationMatrices[IDENTITY_MATRIX]);
    }
    
    public double getStep() {
        return step;
    }
    
    public void setStep(double step) {
        this.step = step;
        this.updateMoveMatrices();
    }
    
    public double getAngleInDegrees() {
        return angleInDegrees;
    }
    
    public void setAngleInDegrees(double angleInDegrees) {
        this.angleInDegrees = angleInDegrees;
        this.updateRotationMatrices();
    }
    
    // Getters
    
    public int getViewportWidth() {
        return viewportWidth;
    }
    
    public int getViewportHeight() {
        return viewportHeight;
    }
    
    public Set<Edge3D> getEdge3DsSet() {
        return edge3DsSet;
    }
    
    public List<Face3D> getFace3DsList() {
        return face3DsList;
    }
    
    // Methods
    
    private void initGeometricTransformationMatrices() {
        /****************
         *   identity   *
         *    matrix    *
         * ⌈1, 0, 0, 0⌉ *
         * |0, 1, 0, 0| *
         * |0, 0, 1, 0| *
         * ⌊0, 0, 0, 1⌋ *
         ****************/
        geometricTransformationMatrices[IDENTITY_MATRIX] = MatrixUtils.createRealIdentityMatrix(4);
        
        /*****************
         *  translation  *
         *     matrix    *
         * ⌈1, 0, 0, Tx⌉ *
         * |0, 1, 0, Ty| *
         * |0, 0, 1, Tz| *
         * ⌊0, 0, 0,  1⌋ *
         *****************/
        geometricTransformationMatrices[MOVE_FORWARD_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, -step},
            {0, 0, 0, 1},
        });
        geometricTransformationMatrices[MOVE_BACKWARD_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, +step},
            {0, 0, 0, 1},
        });
        geometricTransformationMatrices[MOVE_LEFT_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, +step},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1},
        });
        geometricTransformationMatrices[MOVE_RIGHT_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, -step},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1},
        });
        geometricTransformationMatrices[MOVE_UPWARD_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, -step},
            {0, 0, 1, 0},
            {0, 0, 0, 1},
        });
        geometricTransformationMatrices[MOVE_DOWNWARD_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, +step},
            {0, 0, 1, 0},
            {0, 0, 0, 1},
        });
        
        /*************************************************************************************
         * rotation matrix matrices                                                          *
         *   rotate around OX axis      rotate around OY axis        rotate around OZ axis   *
         * ⌈1,       0,       0,  0⌉  ⌈ cos(θy), 0, sin(θy),  0⌉   ⌈cos(θz),-sin(θz), 0,  0⌉ *
         * |0, cos(θx),-sin(θx),  0|  |       0, 1,       0,  0|   |sin(θz), cos(θz), 0,  0| *
         * |0, sin(θx), cos(θx),  0|  |-sin(θy), 0, cos(θy),  0|   |      0,       0, 1,  0| *
         * ⌊0,       0,       0,  1⌋  ⌊       0, 0,       0,  1⌋   ⌊      0,       0, 0,  1⌋ *
         *************************************************************************************/
        double angleInRadians = Math.toRadians(angleInDegrees);
        
        geometricTransformationMatrices[ROTATE_LEFT_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            { Math.cos(angleInRadians), 0, Math.sin(angleInRadians), 0},
            {                        0, 1,                        0, 0},
            {-Math.sin(angleInRadians), 0, Math.cos(angleInRadians), 0},
            {                        0, 0,                        0, 1},
        });
        geometricTransformationMatrices[ROTATE_RIGHT_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            { Math.cos(-angleInRadians), 0, Math.sin(-angleInRadians), 0},
            {                         0, 1,                         0, 0},
            {-Math.sin(-angleInRadians), 0, Math.cos(-angleInRadians), 0},
            {                         0, 0,                         0, 1},
        });
        geometricTransformationMatrices[ROTATE_UPWARD_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            { 1,                        0,                         0, 0},
            { 0, Math.cos(angleInRadians), -Math.sin(angleInRadians), 0},
            { 0, Math.sin(angleInRadians),  Math.cos(angleInRadians), 0},
            { 0,                        0,                         0, 1},
        });
        geometricTransformationMatrices[ROTATE_DOWNWARD_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            { 1,                         0,                          0, 0},
            { 0, Math.cos(-angleInRadians), -Math.sin(-angleInRadians), 0},
            { 0, Math.sin(-angleInRadians),  Math.cos(-angleInRadians), 0},
            { 0,                         0,                          0, 1},
        });
        geometricTransformationMatrices[ROTATE_TILT_LEFT_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            { Math.cos(-angleInRadians), -Math.sin(-angleInRadians), 0, 0},
            { Math.sin(-angleInRadians),  Math.cos(-angleInRadians), 0, 0},
            {                         0,                          0, 1, 0},
            {                         0,                          0, 0, 1},
        });
        geometricTransformationMatrices[ROTATE_TILT_RIGHT_MATRIX] = MatrixUtils.createRealMatrix(new double[][]{
            { Math.cos(angleInRadians), -Math.sin(angleInRadians), 0, 0},
            { Math.sin(angleInRadians),  Math.cos(angleInRadians), 0, 0},
            {                        0,                         0, 1, 0},
            {                        0,                         0, 0, 1},
        });
    }
    
    private void updateMoveMatrices() {
        geometricTransformationMatrices[MOVE_FORWARD_MATRIX] .setEntry(2, 3, -step);
        geometricTransformationMatrices[MOVE_BACKWARD_MATRIX].setEntry(2, 3, +step);
        geometricTransformationMatrices[MOVE_LEFT_MATRIX]    .setEntry(0, 3, +step);
        geometricTransformationMatrices[MOVE_RIGHT_MATRIX]   .setEntry(0, 3, -step);
        geometricTransformationMatrices[MOVE_UPWARD_MATRIX]  .setEntry(1, 3, -step);
        geometricTransformationMatrices[MOVE_DOWNWARD_MATRIX].setEntry(1, 3, +step);
    }
    
    private void updateRotationMatrices() {
        double angleInRadians = Math.toRadians(angleInDegrees);
        double cosPositiveAngle = Math.cos(angleInRadians);
        double sinPositiveAngle = Math.sin(angleInRadians);
        double cosNegativeAngle = Math.cos(-angleInRadians);
        double sinNegativeAngle = Math.sin(-angleInRadians);
        geometricTransformationMatrices[ROTATE_LEFT_MATRIX].setEntry(0, 0,  cosPositiveAngle);
        geometricTransformationMatrices[ROTATE_LEFT_MATRIX].setEntry(0, 2,  sinPositiveAngle);
        geometricTransformationMatrices[ROTATE_LEFT_MATRIX].setEntry(2, 0, -sinPositiveAngle);
        geometricTransformationMatrices[ROTATE_LEFT_MATRIX].setEntry(2, 2,  cosPositiveAngle);
        geometricTransformationMatrices[ROTATE_RIGHT_MATRIX].setEntry(0, 0,  cosNegativeAngle);
        geometricTransformationMatrices[ROTATE_RIGHT_MATRIX].setEntry(0, 2,  sinNegativeAngle);
        geometricTransformationMatrices[ROTATE_RIGHT_MATRIX].setEntry(2, 0, -sinNegativeAngle);
        geometricTransformationMatrices[ROTATE_RIGHT_MATRIX].setEntry(2, 2,  cosNegativeAngle);
        geometricTransformationMatrices[ROTATE_UPWARD_MATRIX].setEntry(1, 1,  cosPositiveAngle);
        geometricTransformationMatrices[ROTATE_UPWARD_MATRIX].setEntry(1, 2, -sinPositiveAngle);
        geometricTransformationMatrices[ROTATE_UPWARD_MATRIX].setEntry(2, 1,  sinPositiveAngle);
        geometricTransformationMatrices[ROTATE_UPWARD_MATRIX].setEntry(2, 2,  cosPositiveAngle);
        geometricTransformationMatrices[ROTATE_DOWNWARD_MATRIX].setEntry(1, 1,  cosNegativeAngle);
        geometricTransformationMatrices[ROTATE_DOWNWARD_MATRIX].setEntry(1, 2, -sinNegativeAngle);
        geometricTransformationMatrices[ROTATE_DOWNWARD_MATRIX].setEntry(2, 1,  sinNegativeAngle);
        geometricTransformationMatrices[ROTATE_DOWNWARD_MATRIX].setEntry(2, 2,  cosNegativeAngle);
        geometricTransformationMatrices[ROTATE_TILT_LEFT_MATRIX].setEntry(0, 0,  cosNegativeAngle);
        geometricTransformationMatrices[ROTATE_TILT_LEFT_MATRIX].setEntry(0, 1, -sinNegativeAngle);
        geometricTransformationMatrices[ROTATE_TILT_LEFT_MATRIX].setEntry(1, 0,  sinNegativeAngle);
        geometricTransformationMatrices[ROTATE_TILT_LEFT_MATRIX].setEntry(1, 1,  cosNegativeAngle);
        geometricTransformationMatrices[ROTATE_TILT_RIGHT_MATRIX].setEntry(0, 0,  cosPositiveAngle);
        geometricTransformationMatrices[ROTATE_TILT_RIGHT_MATRIX].setEntry(0, 1, -sinPositiveAngle);
        geometricTransformationMatrices[ROTATE_TILT_RIGHT_MATRIX].setEntry(1, 0,  sinPositiveAngle);
        geometricTransformationMatrices[ROTATE_TILT_RIGHT_MATRIX].setEntry(1, 1,  cosPositiveAngle);
    }
    
    private void calculatePoint2D(Point3D point3D) {
        double x = (point3D.getX() * focalDistance) / point3D.getZ();
        double y = (point3D.getY() * focalDistance) / point3D.getZ();
        x =  x + (viewportWidth  / 2.0d);
        y = -y + (viewportHeight / 2.0d);
        point3D.getPoint2D().setLocation(x, y);
        if (point3D.getZ() >= focalDistance) {
            point3D.setInFrontOfViewport(true);
        } else {
            point3D.setInFrontOfViewport(false);
        }
    }
    
    private void updatePoint3DsSet(RealMatrix transformationMatrix) {
        for (Point3D point3D : point3DsSet) {
            RealMatrix coordinates = point3D.getCoordinates();
            point3D.setCoordinates(transformationMatrix.multiply(coordinates));
            this.calculatePoint2D(point3D);
        }
    }
    
    private void updateEdge3DsSet() {
        for (Edge3D edge3D : edge3DsSet) {
            edge3D.updateMockPoints();
            Point3D firstMock = edge3D.getFirstMock();
            Point3D secondMock = edge3D.getSecondMock();
            if ( (!firstMock.isInFrontOfViewport() || !secondMock.isInFrontOfViewport()) && 
                !(!firstMock.isInFrontOfViewport() && !secondMock.isInFrontOfViewport())) {
                if (!firstMock.isInFrontOfViewport()) {
                    Point3D calculatedFirstMockPoint3D = this.calculateMockPoint3D(firstMock, secondMock);
                    firstMock.setCoordinates(calculatedFirstMockPoint3D.getCoordinates());
                    this.calculatePoint2D(firstMock);
                }
                if (!secondMock.isInFrontOfViewport()) {
                    Point3D calculatedSecondMockPoint3D = this.calculateMockPoint3D(secondMock, firstMock);
                    secondMock.setCoordinates(calculatedSecondMockPoint3D.getCoordinates());
                    this.calculatePoint2D(secondMock);
                }
            }
            Point3D first = edge3D.getFirst();
            Point3D second = edge3D.getSecond();
            Line2D line2D = edge3D.getLine2D();
            if (first.isInFrontOfViewport() && second.isInFrontOfViewport()) {
                line2D.setLine(first.getPoint2D(), second.getPoint2D());
            } else {
                line2D.setLine(firstMock.getPoint2D(), secondMock.getPoint2D());
            }
        }
    }
    
    // calculate line and plane intersection
    private Point3D calculateMockPoint3D(Point3D firstPoint3D, Point3D secondPoint3D) {
        //point
        double px = firstPoint3D.getX();
        double py = firstPoint3D.getY();
        double pz = firstPoint3D.getZ();
        // vector from first to second
        double vx = secondPoint3D.getX() - firstPoint3D.getX();
        double vy = secondPoint3D.getY() - firstPoint3D.getY();
        double vz = secondPoint3D.getZ() - firstPoint3D.getZ();
        /******************************************
         * plane's equation: Ax + By + Cz + D = 0 *
         ******************************************/
        // in this situation: A == 0; B == 0;
        double planeFactorC = 1;
        double planeFactorD = -focalDistance;
        /*******************************
         * line's parametric equation: *
         * x = x0 + t*vx               *
         * y = y0 + t*vy               *
         * z = z0 + t*vz               *
         *******************************/
        //////////////////////////////////////////////////////////////
        // plane's equation and line's parametric equation together //
        // calculate t                                              //
        // A(px + t*vx) + B(py + t*vy) + C(pz + t*vz) + D = 0       //
        // Apx + At*vx  + Bpy + Bt*vy  + Cpz + Ct*vz  + D = 0       //
        // t(Avx  + Bvy + Cvz) + Apx + Bpy + Cpz + D = 0            //
        // t = -(Apx + Bpy + Cpz + D) / (Avx  + Bvy + Cvz)          //
        // then point P(x, y, z) is                                 //
        // P(px + t*vx, py + t*vy, pz + t*vz)                       //
        //////////////////////////////////////////////////////////////
        double sum_Apx_Bpy_Cpz_D = planeFactorC*pz + planeFactorD;
        double sum_Avx_Bvy_Cvz   = planeFactorC*vz;
        double t = (-sum_Apx_Bpy_Cpz_D)/sum_Avx_Bvy_Cvz;
        Point3D mockPoint3D = new Point3D(px + t*vx, py + t*vy, pz + t*vz);
        return mockPoint3D;
    }
    
    private void updateFace3DsList() {
        for (Face3D face3D : face3DsList) {
            int inFrontOfViewportCounter = 0;
            Path2D path2D = face3D.getPath2D();
            path2D.reset();
            Iterator<Point3D> iterator = face3D.getPoint3DsList().iterator();
            Point3D first = iterator.next();
            if (first.isInFrontOfViewport()) {
                inFrontOfViewportCounter++;
            }
            path2D.moveTo(first.getPoint2D().getX(), first.getPoint2D().getY());
            double xSum = first.getX();
            double ySum = first.getY();
            double zSum = first.getZ();
            while (iterator.hasNext()) {
                Point3D next = iterator.next();
                if (next.isInFrontOfViewport()) {
                    inFrontOfViewportCounter++;
                }
                path2D.lineTo(next.getPoint2D().getX(), next.getPoint2D().getY());
                xSum += next.getX();
                ySum += next.getY();
                zSum += next.getZ();
            }
            path2D.closePath();
            if (inFrontOfViewportCounter == face3D.getPoint3DsList().size()) {
                face3D.setInFrontOfViewport(true);
            } else {
                face3D.setInFrontOfViewport(false);
            }
            double x = xSum / face3D.getPoint3DsList().size();
            double y = ySum / face3D.getPoint3DsList().size();
            double z = zSum / face3D.getPoint3DsList().size();
            face3D.getCenterOfMass().setCoordinates(x, y, z);
        }
    }
    
    private void update(RealMatrix transformationMatrix) {
        this.updatePoint3DsSet(transformationMatrix);
        this.updateEdge3DsSet();
        this.updateFace3DsList();
        this.setChanged();
        this.notifyObservers();
    }
    
    // motions
    public void moveForward() {
        this.update(geometricTransformationMatrices[MOVE_FORWARD_MATRIX]);
    }
    public void moveBackward() {
        this.update(geometricTransformationMatrices[MOVE_BACKWARD_MATRIX]);
    }
    public void moveLeft() {
        this.update(geometricTransformationMatrices[MOVE_LEFT_MATRIX]);
    }
    public void moveRight() {
        this.update(geometricTransformationMatrices[MOVE_RIGHT_MATRIX]);
    }
    public void moveUpward() {
        this.update(geometricTransformationMatrices[MOVE_UPWARD_MATRIX]);
    }
    public void moveDownward() {
        this.update(geometricTransformationMatrices[MOVE_DOWNWARD_MATRIX]);
    }
    // rotations
    public void rotateLeft() {
        this.update(geometricTransformationMatrices[ROTATE_LEFT_MATRIX]);
    }
    public void rotateRight() {
        this.update(geometricTransformationMatrices[ROTATE_RIGHT_MATRIX]);
    }
    public void rotateUpward() {
        this.update(geometricTransformationMatrices[ROTATE_UPWARD_MATRIX]);
    }
    public void rotateDownward() {
        this.update(geometricTransformationMatrices[ROTATE_DOWNWARD_MATRIX]);
    }
    public void rotateTiltLeft() {
        this.update(geometricTransformationMatrices[ROTATE_TILT_LEFT_MATRIX]);
    }
    public void rotateTiltRight() {
        this.update(geometricTransformationMatrices[ROTATE_TILT_RIGHT_MATRIX]);
    }
}
