package com.mycompany.virtual_camera.model;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Paweł Mac
 */
public class Face3D {
    
    private final List<Point3D> point3DsList = new ArrayList<>();
    private final Point3D centerOfMass = new Point3D();
    private final Path2D path2D = new Path2D.Double();
    private boolean inFrontOfViewport;
    private Color color;
    
    public Face3D(Point3D[] point3Ds, Color color) {
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
        this.centerOfMass.setCoordinates(x, y, z);
        this.color = color;
    }
    
    public Face3D(Collection<Point3D> point3Ds, Color color) {
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
        this.centerOfMass.setCoordinates(x, y, z);
        this.color = color;
    }
    
    // Getters
    
    public List<Point3D> getPoint3DsList() {
        return point3DsList;
    }
    
    public Point3D getCenterOfMass() {
        return centerOfMass;
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
    
    void calculateCenterOfMass() {
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
        this.centerOfMass.setCoordinates(x, y, z);
    }
}
