/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import interfaces.LearningModule;
import interfaces.TrafficLight;

import java.util.List;

//Implementation of methods in interfaces.LearningModule interface
public class LearningModuleImpl implements LearningModule
{
    private int counter;

    LearningModuleImpl()
    {
        counter = 0;
    }

    @Override
    public void updateTrafficLights(List<TrafficLight> trafficLights)
    {
        counter++;
        if (counter == 10)
        {
            for (TrafficLight light : trafficLights)
            {
                light.switchLight();
            }
            counter = 0;
        }
    }
}
