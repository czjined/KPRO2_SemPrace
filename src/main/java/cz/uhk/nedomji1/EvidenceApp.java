package cz.uhk.nedomji1;

import cz.uhk.nedomji1.gui.MainFrame;

import javax.swing.*;


public class EvidenceApp {

    public static void main (String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }


}
