import javax.swing.*;
import java.awt.*;
import interfaces.Car;

public class CarViewer extends JFrame {
    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(150, 150, 10, 10);
        g.drawString("aa", 200, 200);
        System.out.println("Drawing car!");
    }

    public CarViewer(Car c) {
        super();
    }
}
