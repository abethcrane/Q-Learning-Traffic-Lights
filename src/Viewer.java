/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

// Will probably have this thing implement an interface as soon as I
// have time / know the syntax to do it. (Extending JFrame complicates
// things.)

import interfaces.*;
import utils.*;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;

public class Viewer extends JFrame {
    private RoadMap m;
    private List<Car> c;
    private List<TrafficLight> l;
    private int init = 0;

    private static final int u = 10;
    private static final int w = 3; // we require 2*w < u
    /* FIXME */ private static final int n = 40;

    private static final Color backgroundColor = new Color(0x7e7e7e);
    private static final Color roadColor = new Color(0x212323);
    private static final int darkish = 0x3f;

    public Viewer() {
        super();
        this.setSize(u*n, u*n);
        this.setVisible(true);
    }

    public void view(RoadMap m, List<Car> c, List<TrafficLight> l) {
        this.m = m;
        this.c = c;
        this.l = l;
        if (init == 0) init = 1;
        this.repaint(0);
    }

    private void stillLearning(Graphics g) {
        g.drawString("Still learning...", u*n/3, u*n/3);
    }
    
    private void firstPicture(Graphics g) {
        g.drawString("Learned! Enjoy the show!", u*n/3, u*n/3);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, u*n, u*n);
    }

    private void displayMap(Graphics g) {
        g.setColor(roadColor);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; ++j) {
                Coords k = new Coords(i, j);
                if (k != null) if (m.roadAt(k)) {
                    g.fillRect(u*i, u*j, u, u);
                }
            }
        }
        
        for (Car i : c) {
            int x = i.getCoords().getX(), y = i.getCoords().getY();
            int dx = i.getDirection().getXSpeed();
            int dy = i.getDirection().getYSpeed();
            g.setColor(
                //x == y ? Color.red :
                    //l.get(0).horizontalGreen() ? new Color(0,darkish,0)
                    //: new Color(0, 0, darkish) :
                //dy == 0 ? new Color(0, y < 20 ? (int)(255*Math.random()) : darkish, 0) :
                //dx == 0 ? new Color(0, 0, x < 20 ? (int)(255*Math.random()) : darkish) :
                dy == 0 ? Color.green :
                dx == 0 ? Color.blue :
                    Color.magenta
            );
            g.fillRect(u*x, u*y, u, u);
        }

        for (TrafficLight i : l) {
            int x = i.getCoords().getX(), y = i.getCoords().getY();
            g.setColor(i.horizontalGreen() ? Color.green : Color.red);
            g.fillRect(u*x+w, u*y, u-2*w, w);
            g.fillRect(u*x+w, u*(y+1)-w, u-2*w, w);
            g.setColor(!i.horizontalGreen() ? Color.green : Color.red);
            g.fillRect(u*x, u*y+w, w, u-2*w);
            g.fillRect(u*(x+1)-w, u*y+w, w, u-2*w);
        }

    }

    public void paint(Graphics g) {
        if (init == 0) {
            stillLearning(g);
        } else if (init == 1) {
            firstPicture(g);
            init = 2;
        } else {
            displayMap(g);
        }
    }
}
