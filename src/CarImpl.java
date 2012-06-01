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
                                // -- why always in {1, -1}?
                                // - I used velocity to represent direction because it fit - i needed a way of showing
                                // increasing/decreasing in x/y and velocity already had a way to do that
                                // always 1, -1 because - why not? could make it 1000, -1000 but if we use 1/-1 it is convenient for use with velocity also

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
        // incorrect: moving in the x-direction is to move horizontally
            //clarification: when I made the class, i used standard (x, y) ordering in the coordinates. However because
            //I printed out first looping through x then y, this actually resulted in x referring to the rows and y to
            // the columns, resulting in an increase in x being a movement downwards. You are correct that this is
            // counterintuitive. I cbf changing it though, feel free to if it bothers you :)
        boolean stop = (m.nextNonCarSquareIsTrafficLight(position, direction, l) && ((direction.getXSpeed() !=0 ) == l.horizontalGreen()));
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

    @Override
    public boolean hasLeftMap(RoadMap map)
    {
        return
            position.getX() < 0 || position.getX() >= 40 ||
            position.getY() < 0 || position.getY() >= 40;
    }
}
