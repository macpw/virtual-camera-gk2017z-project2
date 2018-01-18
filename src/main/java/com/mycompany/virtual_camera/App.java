package com.mycompany.virtual_camera;

import javax.swing.SwingUtilities;

/**
 *
 * @author Paweł Mac
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello App ;]");
        SwingUtilities.invokeLater(new RunMVC());
    }
}
