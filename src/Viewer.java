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
    private boolean output = false;

    private static final int u = 10;
    /* FIXME */ private static final int n = 40;

    public Viewer() {
        super();
        this.setSize(400, 400);
        this.setVisible(true);
    }

    public void view(RoadMap m, List<Car> c, List<TrafficLight> l) {
        this.m = m;
        this.c = c;
        this.l = l;
        output = true;
        this.repaint(0);
    }

    public void paint(Graphics g) {
        if (!output) {
            return;
        }
        String apology = "should be less  flickery soon";
        // ^ to this end repaint(int, int, int, int) could be of
        // much help. i'm assuming java just takes fucking forever to
        // draw ~2000 pixels.
        g.drawString("Hello, world!", n*u/4, n*u/4);
        g.drawString(apology, n*u/32, n*u/4+2*u);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; ++j) {
                Coords k = new Coords(i, j);
                if (k != null) if (m.roadAt(k)) {
                    g.fillRect(u*i, u*j, u, u);
                }
            }
        }
        
        g.setColor(Color.blue);
        for (Car i : c) {
            Coords j = i.getCoords();
            int x = j.getX(), y = j.getY();
            g.fillRect(u*x, u*y, u, u);
        }

        for (TrafficLight i : l) {
            //int x = i.getCoords.getX(), y = i.getCoords().getY();
            Coords j = i.getCoords();
            int x = j.getX(), y = j.getY();
            g.setColor(!i.horizontalGreen() ? Color.green : Color.red);
            g.fillRect(u*x, u*y, u, u);
        }
    }
}
