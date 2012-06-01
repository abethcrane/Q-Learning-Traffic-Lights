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
    void updateTrafficLights
    (
            RoadMap mapWithCars, 
            List<TrafficLight> trafficLights
    );

    //void learn(RoadMap currentMapWithCars, RoadMap nextMapWithCars);

	void learn(RoadMap s, RoadMap sPrime, List<TrafficLight> trafficLights);
}
