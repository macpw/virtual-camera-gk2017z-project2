package com.mycompany.virtual_camera;

import com.mycompany.virtual_camera.controller.Controller;
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
        SpatialShapesCollection spatialShapesCollection = new SpatialShapesCollection();
        spatialShapesCollection.addSpatialShape(cuboid_0);
        spatialShapesCollection.addSpatialShape(cuboid_1);
        spatialShapesCollection.addSpatialShape(cuboid_2);
        spatialShapesCollection.addSpatialShape(cuboid_3);
        spatialShapesCollection.addSpatialShape(cuboid_4);
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
