package com.mycompany.virtual_camera.model;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Paweł Mac
 */
public class Face3D {
    
    private final List<Point3D> point3DsList = new ArrayList<>();
    private final Point3D centroid = new Point3D();// geometric center, center of mass, barycenter
    private final Path2D path2D = new Path2D.Double();
    private boolean inFrontOfViewport;
    private Color color;
    
    public Face3D(Point3D[] point3Ds, Color color) throws IllegalArgumentException {
        if (point3Ds.length < 3) {
            throw new IllegalArgumentException("Array must contain at least 3 points.");
        }
        double xSum = 0;
        double ySum = 0;
        double zSum = 0;
        for (int i = 0; i < point3Ds.length; i++) {
            this.point3DsList.add(point3Ds[i]);
            xSum += point3Ds[i].getX();
            ySum += point3Ds[i].getY();
            zSum += point3Ds[i].getZ();
        }
        double x = xSum / point3Ds.length;
        double y = ySum / point3Ds.length;
        double z = zSum / point3Ds.length;
        this.centroid.setCoordinates(x, y, z);
        this.color = color;
    }
    
    public Face3D(Collection<Point3D> point3Ds, Color color) throws IllegalArgumentException {
        if (point3Ds.size() < 3) {
            throw new IllegalArgumentException("Collection must contain at least 3 points.");
        }
        double xSum = 0;
        double ySum = 0;
        double zSum = 0;
        for (Point3D point3D : point3Ds) {
            this.point3DsList.add(point3D);
            xSum += point3D.getX();
            ySum += point3D.getY();
            zSum += point3D.getZ();
        }
        double x = xSum / point3Ds.size();
        double y = ySum / point3Ds.size();
        double z = zSum / point3Ds.size();
        this.centroid.setCoordinates(x, y, z);
        this.color = color;
    }
    
    // Getters
    
    public List<Point3D> getPoint3DsList() {
        return point3DsList;
    }
    
    public Point3D getCentroid() {
        return centroid;
    }
    
    public Path2D getPath2D() {
        return path2D;
    }
    
    // Getters and Setters
    
    public boolean isInFrontOfViewport() {
        return inFrontOfViewport;
    }
    
    public void setInFrontOfViewport(boolean inFrontOfViewport) {
        this.inFrontOfViewport = inFrontOfViewport;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    // Methods
    
    void update() {
        // update: path2D, inFrontOfViewport, centroid
        Iterator<Point3D> iterator = this.point3DsList.iterator();
        Point3D first = iterator.next();
        this.path2D.reset();
        this.path2D.moveTo(first.getPoint2D().getX(), first.getPoint2D().getY());
        int inFrontOfViewportCounter = 0;
        if (first.isInFrontOfViewport()) {
            inFrontOfViewportCounter++;
        }
        double xSum = first.getX();
        double ySum = first.getY();
        double zSum = first.getZ();
        while (iterator.hasNext()) {
            Point3D next = iterator.next();
            this.path2D.lineTo(next.getPoint2D().getX(), next.getPoint2D().getY());
            if (next.isInFrontOfViewport()) {
                inFrontOfViewportCounter++;
            }
            xSum += next.getX();
            ySum += next.getY();
            zSum += next.getZ();
        }
        this.path2D.closePath();
        this.inFrontOfViewport = inFrontOfViewportCounter == this.point3DsList.size();
        double x = xSum / this.point3DsList.size();
        double y = ySum / this.point3DsList.size();
        double z = zSum / this.point3DsList.size();
        this.centroid.setCoordinates(x, y, z);
    }
    
    void calculateCentroid() {
        double xSum = 0;
        double ySum = 0;
        double zSum = 0;
        for (Point3D point3D : point3DsList) {
            xSum += point3D.getX();
            ySum += point3D.getY();
            zSum += point3D.getZ();
        }
        double x = xSum / point3DsList.size();
        double y = ySum / point3DsList.size();
        double z = zSum / point3DsList.size();
        this.centroid.setCoordinates(x, y, z);
    }
    
    public static class DistanceFromCentroidComparator implements Comparator<Face3D> {
        
        @Override
        public int compare(Face3D f1, Face3D f2) {
            double f1px = f1.getCentroid().getX();
            double f1py = f1.getCentroid().getY();
            double f1pz = f1.getCentroid().getZ();
            double distance1 = Math.sqrt(f1px*f1px+f1py*f1py+f1pz*f1pz);
            double f2px = f2.getCentroid().getX();
            double f2py = f2.getCentroid().getY();
            double f2pz = f2.getCentroid().getZ();
            double distance2 = Math.sqrt(f2px*f2px+f2py*f2py+f2pz*f2pz);
            return Double.compare(distance1, distance2);
        }
    }
    
    // draft version
    public static class SomeComparator implements Comparator<Face3D> {
        
        boolean print;
        
        public SomeComparator() {
        }
        
        public SomeComparator(boolean print) {
            this.print = print;
        }
        
        @Override
        public int compare(Face3D f1, Face3D f2) {
            // distance comparison
            double f1px = f1.getCentroid().getX();
            double f1py = f1.getCentroid().getY();
            double f1pz = f1.getCentroid().getZ();
            double distance1 = Math.sqrt(f1px*f1px+f1py*f1py+f1pz*f1pz);
            double f2px = f2.getCentroid().getX();
            double f2py = f2.getCentroid().getY();
            double f2pz = f2.getCentroid().getZ();
            double distance2 = Math.sqrt(f2px*f2px+f2py*f2py+f2pz*f2pz);
            int cmpDistance = Double.compare(distance1, distance2);
            
            // plane equation 
            // Ax + By + Cz + D = 0, normal vector n=[A,B,C]
            
            // The plane equation of the first polygon
            double p0x = f1.getPoint3DsList().get(0).getX();
            double p0y = f1.getPoint3DsList().get(0).getY();
            double p0z = f1.getPoint3DsList().get(0).getZ();
            
            double p1x = f1.getPoint3DsList().get(1).getX();
            double p1y = f1.getPoint3DsList().get(1).getY();
            double p1z = f1.getPoint3DsList().get(1).getZ();
            
            double p2x = f1.getPoint3DsList().get(2).getX();
            double p2y = f1.getPoint3DsList().get(2).getY();
            double p2z = f1.getPoint3DsList().get(2).getZ();
            
            double vx = p0x - p1x;
            double vy = p0y - p1y;
            double vz = p0z - p1z;
            
            double ux = p2x - p1x;
            double uy = p2y - p1y;
            double uz = p2z - p1z;
            
            // plane normal vector n=[A, B, C]
            double A1 = vy*uz - vz*uy;
            double B1 = vz*ux - vx*uz;
            double C1 = vx*uy - vy*ux;
            // normalizing vector
            double nLength = Math.sqrt(A1*A1+B1*B1+C1*C1);
            A1 = A1 != 0 ? (A1/nLength) : 0;
            B1 = B1 != 0 ? (B1/nLength) : 0;
            C1 = C1 != 0 ? (C1/nLength) : 0;
            double D1 = -(A1*p1x+B1*p1y+C1*p1z);
            if (D1 == 0) {
                D1 = 0;
            }
            
            // The plane equation of the second polygon
            p0x = f2.getPoint3DsList().get(0).getX();
            p0y = f2.getPoint3DsList().get(0).getY();
            p0z = f2.getPoint3DsList().get(0).getZ();
            
            p1x = f2.getPoint3DsList().get(1).getX();
            p1y = f2.getPoint3DsList().get(1).getY();
            p1z = f2.getPoint3DsList().get(1).getZ();
            
            p2x = f2.getPoint3DsList().get(2).getX();
            p2y = f2.getPoint3DsList().get(2).getY();
            p2z = f2.getPoint3DsList().get(2).getZ();
            
            vx = p0x - p1x;
            vy = p0y - p1y;
            vz = p0z - p1z;
            
            ux = p2x - p1x;
            uy = p2y - p1y;
            uz = p2z - p1z;
            
            // plane normal vector n=[A, B, C]
            double A2 = vy*uz - vz*uy;
            double B2 = vz*ux - vx*uz;
            double C2 = vx*uy - vy*ux;
            // normalizing vector
            nLength = Math.sqrt(A2*A2+B2*B2+C2*C2);
            A2 = A2 != 0 ? (A2/nLength) : 0;
            B2 = B2 != 0 ? (B2/nLength) : 0;
            C2 = C2 != 0 ? (C2/nLength) : 0;
            double D2 = -(A2*p1x+B2*p1y+C2*p1z);
            if (D2 == 0) {
                D2 = 0;
            }
            
            // insert point into plane equation 
            double resultPlane1PointFromPolygon2 = A1*f2px + B1*f2py + C1*f2pz + D1;
            double resultPlane2PointFromPolygon1 = A2*f1px + B2*f1py + C2*f1pz + D2;
            
            int cmpPlane2;
            if (D2 < 0 && resultPlane2PointFromPolygon1 < 0 || D2 >= 0 && resultPlane2PointFromPolygon1 > 0 ) {
                cmpPlane2 = -1;
            } else if (D2 < 0 && resultPlane2PointFromPolygon1 > 0 || D2 >= 0 && resultPlane2PointFromPolygon1 < 0 ) {
                cmpPlane2 = 1;
            } else {
                cmpPlane2 = 0;
            }
            
            int cmpPlane1;
            if (D1 < 0 && resultPlane1PointFromPolygon2 > 0 || D1 >= 0 && resultPlane1PointFromPolygon2 < 0 ) {
                cmpPlane1 = -1;
            } else if (D1 < 0 && resultPlane1PointFromPolygon2 < 0 || D1 >= 0 && resultPlane1PointFromPolygon2 > 0 ) {
                cmpPlane1 = 1;
            } else {
                cmpPlane1 = 0;
            }
            
            int cmpCentroidZ;
            if (f1pz < f2pz) {
                cmpCentroidZ = -1;
            } else if (f1pz > f2pz) {
                cmpCentroidZ = 1;
            } else {
                cmpCentroidZ = 0;
            }
            
            if (print) {
                System.out.println("cmpDistance="+cmpDistance+"; distance1="+distance1+"; distance2="+distance2);
                System.out.println("cmpPlane1="+cmpPlane1+"; A1="+A1+", B1="+B1+", C1="+C1+", D1="+D1);
                System.out.println("cmpPlane2="+cmpPlane2+"; A1="+A2+", B1="+B2+", C1="+C2+", D2="+D2);
                System.out.println("cmpCentroidZ="+cmpCentroidZ+"; f1pz="+f1pz+", f2pz="+f2pz);
            }
            
            //if (cmpPlane1 == cmpPlane2 && cmpPlane1 == cmpCentroidZ && cmpPlane1 != 0) {
            //    return cmpPlane1;
            //}
            
            return cmpDistance;
        }
    }
}
