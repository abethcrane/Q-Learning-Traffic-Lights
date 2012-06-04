/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

package interfaces;

//import Road;

public interface Car {
    // tells the car to continue its journey. it should make up its
    // own mind where its going, whether changing lanes is a good idea,
    // etc.
    void move();

    // returns a value in Z+_2 indicating the car's location
   // Coords getCoords();

    // the direction the car would like to be going.
    // note that this is nonzero even if stopped at a light
    //Velocity getDirection();

    int getVelocity();
    Road getRoad();
    int distAlongRoad();
}
