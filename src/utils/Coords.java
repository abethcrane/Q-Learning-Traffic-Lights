package utils;

/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import java.util.LinkedList;

//Utility class to implement the 'coordinates' of an object
public class Coords extends Object {

    private int x;
    private int y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coords(Coords coords) {
        this.x = coords.getX();
        this.y = coords.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Coords left() {
        return new Coords(x - 1, y);
    }

    public Coords right() {
        return new Coords(x + 1, y);
    }

    public Coords up() {
        return new Coords(x, y - 1);
    }

    public Coords down() {
        return new Coords(x, y + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Coords coords = (Coords) obj;
        return (this.x == coords.getX() && this.y == coords.getY());
    }

    @Override
    public int hashCode() {
        return (1000 * x) + y;
    }

    public LinkedList<Coords> neighbours() {
        LinkedList<Coords> n = new LinkedList<Coords>();
        n.add(this.left());
        n.add(this.up());
        n.add(this.right());
        n.add(this.down());
        return n;
    }
}
