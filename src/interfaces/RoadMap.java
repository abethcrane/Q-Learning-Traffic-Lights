package interfaces;
/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import utils.Coords;
import utils.Velocity;

import java.util.List;

//Roadmap Interface - contains a representation of the road and intersections
public interface RoadMap
{
    //Prints a representation of the map to screen
    void print(List<Car> cars, List<TrafficLight> trafficLights);

    //returns a list of the coordinates of all road entrances
    List<Coords> getRoadEntrances();

    //returns the velocity in x/y directions based on where the road entrance is
    Velocity getStartingVelocity(Coords roadEntrance);

    //Return a roadMap that is identical to the current one
    RoadMap copyMap();

    //add cars from the given list onto the map
    void addCars(List<Car> cars);

    //returns true if the next unoccupied square in the direction of
    //'direction' from the position 'start' is a traffic light
    boolean nextNonCarSquareIsTrafficLight(Coords start, Velocity direction, TrafficLight trafficLight);

    //Returns true if there is a car at the specified coord
    boolean carAt(Coords coords);

    //Returns true if there is a road at the specified coord
    boolean roadAt(Coords coords);
    
    //This is not a hashcode, a hashcode is a code returned by an object
    //to uniquely represent itself and does not take parameters
	int stateCode(TrafficLight t);
	
	// A variant on the stateCode
	// Instead of storing the closet car position for each road, it counts how many are stopped on each road 
	int stateCode2(TrafficLight t);

    //returns true if there is room for a car at p to cross the intersection in a direction
    //calculates by counting blank squares thirteen tiles from position
    boolean roomToCrossIntersection(Coords position, Velocity direction, TrafficLight l);

    TrafficLight getClosestTrafficLight(Car car, List<TrafficLight> trafficLights);
}
