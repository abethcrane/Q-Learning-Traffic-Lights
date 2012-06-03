import interfaces.RoadMap;
import interfaces.TrafficLight;

import java.util.List;

//Contains the logic for our reinforcement learning.
public interface LearningModule {
    List<Boolean> updateTrafficLights
        (
                RoadMap mapWithCars,
                List<TrafficLight> trafficLights,
                int timeRan
        );

    List<Boolean> updateTrafficLightsRandomly(
        RoadMap mapWithCars, List<TrafficLight> trafficLights);

    void setRLParam(float alpha, float gamma, float epsilon);

    void learn
        (List<Integer> pastStates, List<Boolean> switches, 
        List<Integer> rewards, List<Integer> newStates, 
        List<TrafficLight> lights
    );

    int reward(int stateCode);
    int reward2(int stateCode);
    int reward3(int stateCode);
    int reward4(List<Car> cars, TrafficLight light);
}
