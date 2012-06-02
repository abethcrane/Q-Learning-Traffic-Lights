package interfaces;/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import java.util.List;

//Contains the logic for our reinforcement learning.
public interface LearningModule
{
    List<Integer> updateTrafficLights
        (
                RoadMap mapWithCars,
                List<TrafficLight> trafficLights
        );

    //Uses this method while still learning
    List<Integer> updateTrafficLightsRandomly(RoadMap mapWithCars, List<TrafficLight> trafficLights);

    //learning method
    void learn(List<Integer> pastStates, List<Integer> switches, List<Integer> rewards, List<Integer> newStates, List<TrafficLight> lights);

    int reward(RoadMap r, TrafficLight t);
}
