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
    Road road();

    int distAlongRoad();

    void move();

    int velocity();
}
