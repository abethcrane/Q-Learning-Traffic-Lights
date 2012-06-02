import javax.swing.*;
import java.awt.*;
import interfaces.RoadMap;

public class MapViewer extends JFrame {
    static int i = 0;
    public void paint(Graphics g) {
        //g.setColor(Color.red);
        g.fillOval(40*(++i), 40, 12, 12);
        //g.setColor(Color.green);
        g.fillOval(40*(i-1), 40, 12, 12);
        System.out.println("Drawing map!");
    }

    public MapViewer(RoadMap r) {
        super();
    }
}
