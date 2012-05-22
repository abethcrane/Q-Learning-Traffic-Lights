/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.TrafficLight;
import utils.Coords;

//Contains implementation of methods in interfaces.TrafficLight
public class TrafficLightImpl implements TrafficLight
{
    private Coords coords;
    private boolean horizontalGreen;

    public TrafficLightImpl(Coords coords, boolean horizontalGreen)
    {
        this.coords = coords;
        this.horizontalGreen = horizontalGreen;
    }

    @Override
    public void switchLight()
    {
        horizontalGreen = !horizontalGreen;
    }

    public Coords getCoords()
    {
        return coords;
    }

    @Override
    public boolean horizontalGreen()
    {
        return horizontalGreen;
    }
}
