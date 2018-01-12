package com.mycompany.virtual_camera;

import com.mycompany.virtual_camera.controller.Controller;
import com.mycompany.virtual_camera.model.Edge3D;
import com.mycompany.virtual_camera.model.Point3D;
import com.mycompany.virtual_camera.model.ViewportModel;
import com.mycompany.virtual_camera.model.spatial_shape.Cuboid;
import com.mycompany.virtual_camera.model.spatial_shape.SpatialShapesCollection;
import com.mycompany.virtual_camera.view.View;
import com.mycompany.virtual_camera.view.ViewportJPanel;
import java.util.Set;
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
        Cuboid cuboid_1 = new Cuboid(-160, 100, 300, -100, -100, 200);
        SpatialShapesCollection spatialShapesCollection = new SpatialShapesCollection();
        spatialShapesCollection.addSpatialShape(cuboid_0);
        spatialShapesCollection.addSpatialShape(cuboid_1);
        ViewportModel viewportModel = new ViewportModel(600, 400, spatialShapesCollection);
        View view = new View(viewportModel.getViewportWidth(), viewportModel.getViewportHeight());
        ViewportJPanel viewportJPanel = view.getViewportJPanel();
        viewportJPanel.setEdge3DsCollection(viewportModel.getEdge3DsSet());
        viewportModel.addObserver(viewportJPanel);
        Controller.controller(viewportModel, view);
    }
    
    // Test RunMVC
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunMVC());
    }
}
