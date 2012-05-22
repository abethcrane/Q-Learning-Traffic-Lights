import interfaces.Car;
import interfaces.RoadMap;
import interfaces.TrafficLight;
import utils.Coords;
import utils.Velocity;

import java.util.List;

/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/
public class CarImpl implements Car
{
    Coords position;
    Velocity velocity;

    public CarImpl(Coords position, Velocity startingVelocity)
    {
        this.position = position;
        this.velocity = startingVelocity;
    }

    @Override
    public void updateVelocity(List<TrafficLight> trafficLightList, RoadMap mapWithCars)
    {

    }

    @Override
    public void updatePosition()
    {
        position.setX(position.getX() + velocity.getXSpeed());
        position.setY(position.getY() + velocity.getYSpeed());
    }

    @Override
    public Coords getPosition()
    {
         return position;
    }

    @Override
    public boolean removeIfOffRoad(RoadMap map)
    {
        return (position.getX() < 0 || position.getX() >= 40 || position.getY() < 0 || position.getY() >= 40);
    }
}
