package interfaces;

import interfaces.TrafficLight;
import utils.Coords;

import java.util.List;

/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/
public interface Car
{
    //Updates the velocity of a car given whether the traffic light is immediately in front and is red
    void updateVelocity(TrafficLight trafficLight, RoadMap mapWithCars);

    //Updates the position of the car by incrementing x/y by velocity x/y
    void updatePosition();

    Coords getCoords();

    //flag to remove the car if it has left the road
    boolean hasLeftMap(RoadMap map);

    char getChar();
}
