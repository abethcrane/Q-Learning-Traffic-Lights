package interfaces;
/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import interfaces.Road;
import interfaces.Car;
import interfaces.Intersect;
import utils.Coords;
import java.util.List;

//Roadmap Interface - contains a representation of the road and intersections
public interface RoadMap {

    List<Car> carsOn(int i);

    List<Intersect> intersections();

    int nRoads();

    Road road(int i);

    boolean roadAt(Coords c);

    boolean carAt(Coords c);

    boolean intersectAt(Coords c);

    void spawn(int road, int lane);

    void clock();
}
