/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/
public interface Car {
    //Updates the velocity of a car given whether the traffic light 
    //is immediately in front and is red
    void updateVelocity(TrafficLight trafficLight, RoadMap mapWithCars);

    //Updates the position of the car by incrementing x/y by velocity
    void updatePosition();

    Coords getCoords();

    //the direction the car would like to be going.
    //note that this is nonzero even if stopped at a light
    Velocity getDirection();

    //whether the car is on the given map
    boolean hasLeftMap(RoadMap map);

    char getChar();

    boolean stopped();
}
