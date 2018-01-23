package com.mycompany.virtual_camera;

import com.mycompany.virtual_camera.controller.Controller;
import com.mycompany.virtual_camera.model.Point3D;
import com.mycompany.virtual_camera.model.ViewportModel;
import com.mycompany.virtual_camera.model.spatial_shape.Cuboid;
import com.mycompany.virtual_camera.model.spatial_shape.SpatialShapesCollection;
import com.mycompany.virtual_camera.view.View;
import com.mycompany.virtual_camera.view.ViewportJPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Paweł Mac
 */
public class RunMVC implements Runnable {
    
    public RunMVC() {
    }
    
    @Override
    public void run() {
        Cuboid cuboid_0 = new Cuboid(-100, 0, 250, 200, 250, 300);
        
        Cuboid cuboid_1 = new Cuboid(1000, 0, 2000, 1000, 1000, 1000);
        Cuboid cuboid_2 = new Cuboid(3000, 0, 2000,  600, 2000,  700);
        Cuboid cuboid_3 = new Cuboid(1000, 0, 4000, 1000, 3000, 1000);
        Cuboid cuboid_4 = new Cuboid(3000, 0, 4000,  600, 4000,  900);
        
        Cuboid cuboid_5 = new Cuboid(-1000, 0, 1000, 500, 500, 500);
        Cuboid cuboid_6 = new Cuboid(-600, 500, 1100, -300, 300, 300);
        Cuboid cuboid_7 = new Cuboid(-850, 800, 1350, 200, 200, -200);
        Cuboid cuboid_8 = new Cuboid(-800, 1100, 1200, 100, -100, 100);
        
        Cuboid cuboid_9 = new Cuboid(-1200, 200, 1200, -200, -200, -200);
        
        SpatialShapesCollection spatialShapesCollection = new SpatialShapesCollection();
        
        spatialShapesCollection.addSpatialShape(cuboid_0);
        
        spatialShapesCollection.addSpatialShape(cuboid_1);
        spatialShapesCollection.addSpatialShape(cuboid_2);
        spatialShapesCollection.addSpatialShape(cuboid_3);
        spatialShapesCollection.addSpatialShape(cuboid_4);
        
        spatialShapesCollection.addSpatialShape(cuboid_5);
        spatialShapesCollection.addSpatialShape(cuboid_6);
        spatialShapesCollection.addSpatialShape(cuboid_7);
        spatialShapesCollection.addSpatialShape(cuboid_8);
        
        spatialShapesCollection.addSpatialShape(cuboid_9);
        
        ViewportModel viewportModel = new ViewportModel(600, 400, spatialShapesCollection);
        View view = new View(viewportModel.getViewportWidth(), viewportModel.getViewportHeight());
        ViewportJPanel viewportJPanel = view.getViewportJPanel();
        viewportJPanel.setEdge3DsCollection(viewportModel.getEdge3DsSet());
        viewportJPanel.setFace3DsCollection(viewportModel.getFace3DsList());
        viewportJPanel.setFace3DsQueue(viewportModel.getFace3DsQueue());
        viewportModel.addObserver(viewportJPanel);
        Controller.controller(viewportModel, view);
    }
    
    // Test RunMVC
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunMVC());
    }
}
