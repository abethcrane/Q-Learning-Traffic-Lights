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
    //This is always 1 or -1 in the direction the car is going
    Velocity direction;

    public CarImpl(Coords position, Velocity startingVelocity)
    {
        this.position = position;
        this.velocity = startingVelocity;
        this.direction = new Velocity(startingVelocity.getXSpeed(), startingVelocity.getYSpeed());
    }

    @Override
    public void updateVelocity(TrafficLight trafficLight, RoadMap mapWithCars)
    {
        //If next non-car space is a traffic light
            //If red, stop
            //if green, go
        //If not a traffic light, then go
        if(mapWithCars.nextNonCarSquareIsTrafficLight(position, direction, trafficLight))
        {
            if (direction.getXSpeed() == 0)
            {
                //we are heading in the Y coordinate direction - aka horizontally
                if (trafficLight.horizontalGreen())
                {
                    velocity.setYSpeed(direction.getYSpeed());
                }
                else
                {
                    velocity.setYSpeed(0);
                }
            }
            if (direction.getYSpeed() == 0)
            {
                //we are heading in the X coordinate direction - aka vertically
                if (!trafficLight.horizontalGreen())
                {
                    velocity.setXSpeed(direction.getXSpeed());
                }
                else
                {
                    velocity.setXSpeed(0);
                }
            }

        }
        else
        {
            velocity.setXSpeed(direction.getXSpeed());
            velocity.setYSpeed(direction.getYSpeed());
        }
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
