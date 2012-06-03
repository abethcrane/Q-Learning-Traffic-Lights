/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

//Utility class to represent the x and y velocities of an object.
public class Velocity {
    private int x;
    private int y;

    public Velocity(int xSpeed, int ySpeed) {
        this.x = xSpeed;
        this.y = ySpeed;
    }

    public int getXSpeed() {
        return x;
    }

    public int getYSpeed() {
        return y;
    }

    public void setXSpeed(int xSpeed) {
        x = xSpeed;
    }

    public void setYSpeed(int ySpeed) {
        y = ySpeed;
    }
}
