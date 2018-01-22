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
            double p1x = f1.getCentroid().getX();
            double p1y = f1.getCentroid().getY();
            double p1z = f1.getCentroid().getZ();
            double length1 = Math.sqrt(p1x*p1x+p1y*p1y+p1z*p1z);
            double p2x = f2.getCentroid().getX();
            double p2y = f2.getCentroid().getY();
            double p2z = f2.getCentroid().getZ();
            double length2 = Math.sqrt(p2x*p2x+p2y*p2y+p2z*p2z);
            return Double.compare(length1, length2);
        }
    }
}
