import interfaces.Car;
import interfaces.Road;
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
public class CarImpl implements Car {
    private Road road;
    private int lane;
    private int distAlongRoad;
    private int velocity;
    
    public CarImpl(Road r, int lane, int v0) {
        this.road = new RoadImpl(r.lanes(), r.length());
        this.velocity = v0;
        this.lane = lane;
        this.distAlongRoad = 0;
    }
/*
    public void move(TrafficLight l, RoadMap m) {
        boolean greenLight = 
                l.getDelay() == 0 &&
                l.horizontalGreen() == (direction.getYSpeed() == 0) &&
                m.roomToCrossIntersection(position, direction, l);
        boolean stop =
                !greenLight &&
                m.nextNonCarSquareIsTrafficLight(position,direction,l);
        velocity.setXSpeed(stop ? 0 : direction.getXSpeed());
        velocity.setYSpeed(stop ? 0 : direction.getYSpeed());
        
        position.setX(position.getX() + velocity.getXSpeed());
        position.setY(position.getY() + velocity.getYSpeed());
    }
*/
    public void move() {
    	distAlongRoad += velocity;
    }
    /*public Coords getCoords() {
         return position;
    }

    public Velocity getDirection() {
        return direction;
    }*/
    
    public int distAlongRoad() {
    	return distAlongRoad;
    }

    public int velocity() {
        return velocity;
    }
    
    public Road road() {
    	return road;
    }
}
