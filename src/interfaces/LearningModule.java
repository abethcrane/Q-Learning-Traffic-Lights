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
    List<Boolean> updateTrafficLights
        (
                RoadMap mapWithCars,
                List<TrafficLight> trafficLights
        );

    List<Integer> updateTrafficLightsRandomly(RoadMap mapWithCars, List<TrafficLight> trafficLights);

    //learning method
    void learn(List<Integer> pastStates, List<Boolean> switches, List<Integer> rewards, List<Integer> newStates, List<TrafficLight> lights);

    int reward(int stateCode);

    int reward2(int stateCode);
    int reward3(List<Car> cars, TrafficLight light);
}
