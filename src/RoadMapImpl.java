/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.Car;
import interfaces.RoadMap;
import interfaces.Action;
import interfaces.TrafficLight;
import utils.Coords;
import utils.Velocity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Roadmap Implementation class -
//implements methods from interfaces.RoadMap
public class RoadMapImpl implements RoadMap {
    public final int gridSize = 60;
    private final Coords[] defaultEntrances =
        {new Coords(0, 21),
                new Coords(0, 41),
                new Coords(59, 19),
                new Coords(59, 39),
                new Coords(19, 0),
                new Coords(39, 0),
                new Coords(21, 59),
                new Coords(41, 59),
        };
    private final char carChar = 'C';
    private final int roadChar = ' ';
    private char[][] grid;
    private List<Coords> roadEntrances = new ArrayList<Coords>();

    RoadMapImpl() {
        Collections.addAll(roadEntrances, defaultEntrances);
        grid = new char[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = 'x';
                for (Coords k : roadEntrances) {
                    if ((0 < i && i < gridSize-1 && i == k.getX()) || 
                        (0 < j && j < gridSize-1 && j == k.getY()))
                    {
                        grid[i][j] = roadChar;
                    }
                }
            }
        }
    }

    public RoadMapImpl(char[][] newGrid) {
        grid = copyGrid(newGrid);
        Collections.addAll(roadEntrances, defaultEntrances);
    }

    @Override
    public void print(List<Car> cars, List<TrafficLight> trafficLights){
        //copy grid and place cars onto it
        char[][] newGrid = copyGrid(grid);
        for (Car car : cars) {
            int y = car.getCoords().getX(), x = car.getCoords().getY();
            int dx = car.getDirection().getXSpeed();
            int dy = car.getDirection().getYSpeed();
            newGrid[y][x] = 
                dx == 0 ? dy < 0 ? '^' : 'v' :
                dy == 0 ? dx < 0 ? '<' : '>' :
                    '6';
        }
        for(TrafficLight light : trafficLights) {
            int x=light.getCoords().getX(), y=light.getCoords().getY();
            if (light.getDelay() != 0) {
                newGrid[y][x] = 'o';
            } else {
                newGrid[y][x] = light.horizontalGreen() ? '>' : 'v';
            }
        }

        //print new grid to screen
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(newGrid[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Hash = 4 digit number (longer if more roads are added)
    /*
    1st - Num cars with velocity 0 on road 1
    2nd - Num cars with velocity 0 on road 2
    3rd - light setting (ie 0-green, 1 red for one of the roads) X
     */    
    public int stateCode(TrafficLight t) {
        int hash = 0;

        int lightSetting = 0;
        if (t.horizontalGreen()) {
            lightSetting = 1;
        }

        hash += lightSetting;
        
        // For each road off the traffic lights
        // Follow it back until we hit either 9 or a car
        // Mark that place         
        
        Coords c = new Coords(t.getCoords()).left().up();
        int i;
        // Road one we'll go vertically up
        for (i = 0; i < 9; i++) {
            c.setY(c.getY()-1);
            if (carAt(c)) {
                break;
            }
        }
        int v1 = i;
        c = new Coords(t.getCoords()).right().down();
        CarImpl curCar;
        // Road one we'll go vertically down
        
        for (i = 0; i < 9; i++) {
        	c.setY(c.getY()+1);    
            if (!carAt(c)) {
                break;
            }
        }
        int v2 = i-1;
        hash += 10*(Math.min(v1, v2));

        c = new Coords(t.getCoords()).left().down();
        // Road two we'll go horizontally left
        for (i = 0; i < 9; i++) {
            c.setX(c.getX()-1);
            if (carAt(c)) {
                break;
            }
        }
        int h1 = i;
        c = new Coords(t.getCoords()).right().up();
        // Road two we'll go horizontally right
        for (i = 0; i < 9; i++) {
            c.setX(c.getX()+1);
            if (carAt(c)) {
                break;
            }
        }
        int h2 = i;
        hash += 100*(Math.min(h1, h2));
    
        return hash;
    }
    
    
    @Override
    // Hash = 4 digit number (longer if more roads are added)
    //
    //  1st - closest car position from intersection for road 1 
    //        (0-8, 9 if no cars) X
    //  2nd - closest car position from intersection for road 2 
    //        (0-8, 9 if no cars X
    //  3rd - light setting (ie 0-green, 1 red for one of the roads)
    //
    // Needs to take in traffic light so it can tell which one to work
    // the things out for
    public int stateCode2(TrafficLight t) {
        int hash = 0;

        int lightSetting = 0;
        if (t.horizontalGreen()) {
            lightSetting = 1;
        }

        hash += lightSetting;
        
        // For each road off the traffic lights
        // Follow it back until we hit either 9 or a car
        // Mark that place         
        
        // Road one we'll go vertically up
        int i = 0;
        Coords c = new Coords(t.getCoords()).left().up();
        c.setY(c.getY()-1);  
        while (carAt(c)) {
        	i++;
            c.setY(c.getY()-1); 
        }
        int v1 = i;
        
        // Road one we'll go vertically down
        i = 0;
        c = new Coords(t.getCoords()).right().down();
        c.setY(c.getY()+1);
        while (carAt(c)) {
            i++;
        	c.setY(c.getY()+1);
        }
        int v2 = i;
        
        hash += 10*(v1+v2);

        // Road two we'll go horizontally left
        i = 0;
        c = new Coords(t.getCoords()).left().down();
        c.setX(c.getX()-1);
        while(carAt(c)) {
        	i++;
            c.setX(c.getX()-1);
        }
        int h1 = i;
        
        // Road two we'll go horizontally right
        i = 0;
        c = new Coords(t.getCoords()).right().up();
        c.setX(c.getX()+1);
        for (i = 0; i < 9; i++) {
            i++;
            c.setX(c.getX()+1);
        }
        int h2 = i;
        
        hash += 100*(h1+h2);
    
        return hash;
    }

    @Override
    public boolean roomToCrossIntersection(Coords position, Velocity direction, TrafficLight l)
    {
        //set coords for start of intersection
        Coords trafficLightCoords = trafficLightCoords(direction, l);
        //iterate for thirteen squares from start of intersection, counting blanks
        int blankRoadTiles = 0;
        Coords current = new Coords(trafficLightCoords.getX(), trafficLightCoords.getY());
        for (int i = 0; i < 20; i++) {
            if (!carAt(current)) {
                blankRoadTiles++;
            }
            current.setX(current.getX() + direction.getXSpeed());
            current.setY(current.getY() + direction.getYSpeed());
        }
        //4 tiles - three for intersection, one for car on other side
        return blankRoadTiles >= 4;
    }

    @Override
    public TrafficLight getClosestTrafficLight(Car car, List<TrafficLight> trafficLights)
    {
        //Iterate along road in direction of car, return traffic light first encountered or null
       Coords coords = new Coords(car.getCoords().getX(), car.getCoords().getY());
        while (!(coords.getX() < 0) && !(coords.getY() < 0) &&
                !(coords.getX() >= 60) && !(coords.getY() >= 60)) {
            for (TrafficLight t : trafficLights) {
                if (coords.equals(trafficLightCoords(car.getDirection(), t))) {
                    return t;
                }
            }
            coords.setX(coords.getX() + car.getDirection().getXSpeed());
            coords.setY(coords.getY() + car.getDirection().getYSpeed());
        }
        return trafficLights.get(0);
    }

    @Override
    public List<Coords> getRoadEntrances() {
        return roadEntrances;
    }

    @Override
    public Velocity getStartingVelocity(Coords roadEntrance) {
        int x = roadEntrance.getX(), y = roadEntrance.getY();
        return new Velocity(
            y == 0 || y == gridSize - 1 ? 0 : x == 0 ? 1 : -1,
            x == 0 || x == gridSize - 1 ? 0 : y == 0 ? 1 : -1
        );
    }

    @Override
    public RoadMap copyMap() {
        return new RoadMapImpl(grid);
    }

    @Override
    public void addCars(List<Car> cars) {
        for (Car c : cars)
        {
            grid[c.getCoords().getY()][c.getCoords().getX()] = carChar;
        }
    }

    @Override
    public boolean nextNonCarSquareIsTrafficLight(
            Coords start, 
            Velocity direction, 
            TrafficLight trafficLight
    ) {
        Coords current = new Coords(start.getX(), start.getY());
        current.setX(current.getX() + direction.getXSpeed());
        current.setY(current.getY() + direction.getYSpeed());
        //need different traffic light coords depending on direction
        Coords trafficLightCoords = trafficLightCoords(direction, trafficLight);

        while (current.getX() < gridSize && current.getX() >= 0 &&
                current.getY() < gridSize && current.getY() >= 0 &&
                carAt(current) &&
                //(grid[current.getY()][current.getX()] != roadChar) &&
                !trafficLightCoords.equals(current))
        {
            //System.out.println("Current " + current.getX() + " " + current.getY());
            current.setX(current.getX() + direction.getXSpeed());
            current.setY(current.getY() + direction.getYSpeed());
        }
        boolean ret = 
        trafficLightCoords.equals(current);
        //    System.out.println("Current " + current.getX() + " " + current.getY() + " light " + trafficLightCoords.getX() + " " + trafficLightCoords.getY() + " -> " + ret);
        return ret;

    }

    @Override
    public boolean carAt(Coords coords) {

        return  
            0 <= coords.getX() && coords.getX() < gridSize &&
            0 <= coords.getY() && coords.getY() < gridSize &&
            grid[coords.getY()][coords.getX()] == carChar;
    }
    
    public boolean roadAt(Coords coords) {
        return  
            0 <= coords.getX() && coords.getX() < gridSize &&
            0 <= coords.getY() && coords.getY() < gridSize &&
            grid[coords.getY()][coords.getX()] == roadChar;
    }

    private char[][] copyGrid(char[][] grid)
    {
        char[][] newGrid = new char[gridSize][gridSize];

        //explicit copy so array is not a reference to previous array
        for (int i = 0; i < gridSize; i++)
        {
            System.arraycopy(grid[i], 0, newGrid[i], 0, gridSize);
        }
        return newGrid;
    }

    private Coords trafficLightCoords(Velocity direction, TrafficLight trafficLight) {
        Coords trafficLightCoords = new Coords(0,0);
        if (direction.getXSpeed() == 0) {
            if (direction.getYSpeed() == 1) {
                trafficLightCoords =
                        trafficLight.getCoords().left().up();
            } else if (direction.getYSpeed() == -1) {
                trafficLightCoords =
                        trafficLight.getCoords().right().down();
            }
        } else if (direction.getYSpeed() == 0) {
            if (direction.getXSpeed() == 1) {
                trafficLightCoords =
                        trafficLight.getCoords().down().left();
            } else if (direction.getXSpeed() == -1) {
                trafficLightCoords =
                        trafficLight.getCoords().right().up();
            }
        }
        return trafficLightCoords;
    }
}
