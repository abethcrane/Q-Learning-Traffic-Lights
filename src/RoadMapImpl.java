/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.Car;
import interfaces.RoadMap;
import interfaces.Road;
import interfaces.Intersect;
import java.util.List;

public class RoadMapImpl implements RoadMap {
    private int nRoads;
    private Road[] roads;
    private List<Intersect> intersections;
    private List<Car>[] carsOn;

    public RoadMapImpl() {
    }

    public List<Car> carsOn(int i) {
        return carsOn[i];
    }

    public int nRoads() {
        return nRoads;
    }

    public Road road(int i) {
        return roads[i];
    }

    public List<Intersect> intersections() {
        return intersections;
    }

    public void spawn(int road, int lane) {
        Car c = new CarImpl(roads[road], lane, 1);
        carsOn[road].add(c);
    }

    public void clock() {
    }
}
