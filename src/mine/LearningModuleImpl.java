/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import interfaces.LearningModule;
import interfaces.TrafficLight;
import interfaces.RoadMap;

import java.util.List;

public class LearningModuleImpl implements LearningModule
{
    /* TODO:  MERGING
    private static final int numCarSpaces = 9;
    private static final int numActions = 2;
    private static final int numRoads = 2;
    private static final int numStates = 
            (int)java.lang.Math.pow(numCarSpaces,numRoads) * numActions;
    // FIXME: {alpha, gamma, epsilon} are probably dependent - how?
    private static final float epsilon = 0.1;
    private static final float gamma = 0.9;
    private static final float alpha = 0.1;
    */

    private int counter;
    /* TODO: MERGING
    private Action lastAction;
    private ArrayList<float> qValues;
    */

    LearningModuleImpl()
    {
        counter = 0;
    }

    @Override
    public void updateTrafficLights(
            RoadMap mapWithCars,
            List<TrafficLight> trafficLights
    ) {
        //So far, naive 'switch at every ten steps' counter
        //disregards the actual state of the road
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

    @Override
    public void learn(RoadMap s, RoadMap sPrime)
    {
        //uses a = lastAction
        //currently doesn't learn a lot
    }
}
