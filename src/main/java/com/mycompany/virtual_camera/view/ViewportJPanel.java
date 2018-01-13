package com.mycompany.virtual_camera.view;

import com.mycompany.virtual_camera.model.Edge3D;
import com.mycompany.virtual_camera.model.Face3D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import javax.swing.JPanel;

/**
 *
 * @author Paweł Mac
 */
public final class ViewportJPanel extends JPanel implements Observer {
    
    private Collection<Edge3D> edge3DsCollection;
    private Collection<Face3D> face3DsCollection;
    private Queue<Face3D> face3DsQueue;
    
    public ViewportJPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.white);
    }
    
    // Setter
    
    public void setEdge3DsCollection(Collection<Edge3D> edge3DsCollection) {
        this.edge3DsCollection = edge3DsCollection;
    }
    
    public void setFace3DsCollection(Collection<Face3D> face3DsCollection) {
        this.face3DsCollection = face3DsCollection;
    }
    
    public void setFace3DsQueue(Queue<Face3D> face3DsQueue) {
        this.face3DsQueue = face3DsQueue;
    }
    
    // Methods
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        if (face3DsCollection != null) {
            for (Face3D face3D : face3DsCollection) {
                if (face3D.isInFrontOfViewport()) {
                    g2D.draw(face3D.getPath2D());
                }
            }
        } else {
            System.out.println("face3DsCollection == null");
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        this.repaint();
    }
}
