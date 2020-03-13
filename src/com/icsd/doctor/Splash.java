package com.icsd.doctor;
import java.awt.Color;

//import java.awt.Dimension;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Splash extends JFrame {
 
    private JLabel imglabel;
    private ImageIcon img;
    private static JProgressBar pbar;
    Thread t = null;
 
    public Splash() 
    {
      //  super("Splash");
        setSize(404, 310);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);//for center of screen
        
      setUndecorated(true);//hide title bar
      setLayout(null);
//        img = new ImageIcon(getClass().getResource("splashS.jpg"));
        imglabel = new JLabel(new ImageIcon("Images/splash.jpg"));
        imglabel.setBounds(0, 0, 404, 310);        
       
        add(imglabel);
        pbar = new JProgressBar();
        pbar.setMinimum(0);
        pbar.setMaximum(100);
      pbar.setStringPainted(true);
        pbar.setForeground(Color.decode("#cb202d"));

        add(pbar);
        
        pbar.setBounds(0, 280, 404, 30);
 
        Thread t = new Thread()
        {
 
            public void run() {
                int i = 0;
                while (i <= 100) {
                    pbar.setValue(i);
                    try {
                        sleep(90);
                    } catch (InterruptedException ex) {
                      
                    }
                    i++;
                }
            }
        };
        t.start();
    }
}