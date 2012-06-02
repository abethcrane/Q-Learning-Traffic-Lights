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
    private Coords position;
    private Velocity velocity;
    private Velocity direction; // desired velocity (if no light)

    public CarImpl(Coords position, Velocity startingVelocity)
    {
        this.position = position;
        this.velocity = startingVelocity;
        this.direction = new Velocity(
            startingVelocity.getXSpeed(), startingVelocity.getYSpeed());
    }

    @Override
    public void updateVelocity(TrafficLight l, RoadMap m)
    {
        // this assumes that a car travelling in the x-direction may
        // pass through a traffic light iff !light.horizontalGreen()
        boolean stop =
                (m.nextNonCarSquareIsTrafficLight(position, direction, l))&&
                        (((direction.getXSpeed() !=0) == l.horizontalGreen() ||
                          l.getDelay() != 0) || !m.roomToCrossIntersection(position, direction, l));
        velocity.setXSpeed(stop ? 0 : direction.getXSpeed());
        velocity.setYSpeed(stop ? 0 : direction.getYSpeed());
    }

    @Override
    public void updatePosition()
    {
        position.setX(position.getX() + velocity.getXSpeed());
        position.setY(position.getY() + velocity.getYSpeed());
    }

    @Override
    public Coords getCoords()
    {
         return position;
    }

    public Velocity getDirection()
    {
        return direction;
    }

    @Override
    public boolean hasLeftMap(RoadMap map)
    {
        return
            position.getX() < 0 || position.getX() >= 60 ||
            position.getY() < 0 || position.getY() >= 60;
    }

    @Override
    public char getChar()
    {
        if (direction.getXSpeed() == -1) {
            return '^';
        }  else if (direction.getXSpeed() == 1) {
            return 'v';
        } else if (direction.getYSpeed() == -1) {
            return '<';
        } else {
            return '>';
        }
    }

    @Override
    public boolean stopped()
    {
        return position.getX() == 0 && position.getY() == 0;
    }
}
