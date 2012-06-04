/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

package interfaces;

import utils.Velocity;
import utils.Coords;

public interface Car {
    // tells the car to continue its journey. it should make up its
    // own mind where its going, whether changing lanes is a good idea,
    // etc.
    void move(TrafficLight l, RoadMap m);

    // returns a value in Z+_2 indicating the car's location
    Coords getCoords();

    // the direction the car would like to be going.
    // note that this is nonzero even if stopped at a light
    Velocity getDirection();

    Velocity getVelocity();
}
