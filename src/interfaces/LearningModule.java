package interfaces;/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import java.util.List;

//Contains the logic for our reinforcement learning.
public interface LearningModule {
    boolean decide(RoadMap m, TrafficLight l);
    void learn(TrafficLight l, RoadMap s, RoadMap sPrime);

    List<Boolean> updateTrafficLights
        (
                RoadMap mapWithCars,
                List<TrafficLight> trafficLights,
                int timeRan
        );

    List<Boolean> updateTrafficLightsRandomly(
        RoadMap mapWithCars, List<TrafficLight> trafficLights);

    void setRLParam(float alpha, float gamma, float epsilon);

    /*
    void learn
        (List<Integer> pastStates, List<Boolean> switches, 
        List<Integer> rewards, List<Integer> newStates, 
        List<TrafficLight> lights
    );
    */

    int reward(int stateCode);
    int reward2(int stateCode);
    int reward3(int stateCode);
    int reward4(List<Car> cars, TrafficLight light);
}
