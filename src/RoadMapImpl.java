/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

// The intention is to change every occurrence of
// (List<Cars> cars, List<TrafficLights> trafficLights
// to
// State s
// Ceebs now.

import interfaces.Car;
import interfaces.RoadMap;
import interfaces.Action;
import interfaces.TrafficLight;
import utils.Coords;
import utils.Velocity;

import java.util.ArrayList;
import java.util.List;


//Roadmap Implementation class - implements methods from interfaces.RoadMap
public class RoadMapImpl implements RoadMap {
    public final int gridSize = 40;
    private final Coords[] defaultEntrances =
        {new Coords(0, gridSize/2), new Coords(gridSize/2, 0)};
    private final char carChar = 'C';
    private char[][] grid;
    private List<Coords> roadEntrances = new ArrayList<Coords>();

    RoadMapImpl() {
        for (Coords c : defaultEntrances) {
            roadEntrances.add(c);
        }
        grid = new char[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = 'x';
                for (Coords k : roadEntrances) {
                    if ((i > 0 && i == k.getX()) || 
                        (j > 0 && j == k.getY())) {
                        	grid[i][j] = ' ';
                    }
                }
            }
        }
    }

    // TODO: I feel this should read
    // public RoadMapImpl(char[][] newGrid, List<Coords> entrances)
    // {
    //     ...
    //     for (Coords c : entrances)
    //     {
    //         ...
    //
    public RoadMapImpl(char[][] newGrid) {
        grid = copyGrid(newGrid);
        for (Coords c : defaultEntrances) {
            roadEntrances.add(c);
        }
    }

    @Override // I'm guessing `print' is actually defined for Object?
    public void print(List<Car> cars, List<TrafficLight> trafficLights) {
        //copy grid and place cars onto it
        char[][] newGrid = copyGrid(grid);
        for (Car car : cars) {
            int x=car.getCoords().getX(), y=car.getCoords().getY();
            newGrid[x][y] = carChar;
        }
        for(TrafficLight light : trafficLights) {
            int x=light.getCoords().getX(), y=light.getCoords().getY();
            newGrid[x][y] = light.horizontalGreen() ? '>' : 'v';
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

    @Override
    // Hash = 5 digit number (longer if more roads are added)
    /*
    1st - closest car position from intersection for road 1 (0-8, 9 if no cars) X
    2nd - closest car position from intersection for road 2 (0-8, 9 if no cars X
    3rd - light setting (ie 0-green, 1 red for one of the roads) X
    4th - light delay (0-3)
    5th - action (0-1)
     */
    // Needs to take in traffic light so it can tell which one to work the things out for
    public int hashCode(TrafficLight t, Action a) {        
        int hash = 0;
        
        int action = 0;
        if (a.action()) {
            action = 1;
        }
        int lightSetting = 0;
        if (t.horizontalGreen()) {
            lightSetting = 1;
        }
        
        hash += action;
        hash += 10*t.getDelay();
        hash += 100*lightSetting;
        
        // For each road off the traffic lights
        // Follow it back until we hit either 9 or a car
        // Mark that place         
        
        Coords c = new Coords(t.getCoords());
        int i;
        // Road one we'll go vertically
        for (i = 0; i < 9; i++) {
            c.setX(c.getX()-1);
            if (carAt(c)) {
                break;
            }
        }
        hash += 1000*i;
        
        c = new Coords(t.getCoords());
        // Road two we'll go horizontally
        for (i = 0; i < 9; i++) {
            c.setY(c.getY()-1);
            if (carAt(c)) {
                break;
            }
        }
        hash += 10000*i;
    
        return hash;
    }

    @Override // ??
    public List<Coords> getRoadEntrances() {
        return roadEntrances;
    }

    @Override
    public Velocity getStartingVelocity(Coords roadEntrance) {
        if (roadEntrance.getX() == 0) {
            return new Velocity(1, 0);
        }
        if (roadEntrance.getX() == gridSize) {
            return new Velocity(-1, 0);
        }
        if (roadEntrance.getY() == 0) {
            return new Velocity(0, 1);
        }
        if (roadEntrance.getY() == gridSize) {
            return new Velocity(0, -1);
        }
        return null;
    }

    @Override
    public RoadMap copyMap() {
        return new RoadMapImpl(grid);
    }

    @Override
    public void addCars(List<Car> cars) {
        for (Car c : cars)
        {
            grid[c.getCoords().getX()][c.getCoords().getY()] = carChar;
        }
    }

    @Override
    public boolean nextNonCarSquareIsTrafficLight(
            Coords start, 
            Velocity direction, 
            TrafficLight trafficLight
            ) {
        Coords current = new Coords(start.getX(), start.getY());
        while (current.getX() < gridSize && current.getX() >= 0 &&
                current.getY() < gridSize && current.getY() >= 0 &&
                (grid[current.getX()][current.getY()] != ' ') &&
                !trafficLight.getCoords().equals(current))
        {
            current.setX(current.getX() + direction.getXSpeed());
            current.setY(current.getY() + direction.getYSpeed());
        }

        return trafficLight.getCoords().equals(current);

    }

    @Override
    public boolean carAt(Coords coords)
    {
        boolean result = false;
        if (coords.getX() >= 0 && coords.getY() >= 0 && coords.getX() < gridSize && coords.getY() < gridSize ){
            if (grid[coords.getX()][coords.getY()] == carChar) {
                result = true;
            }
        }
        return result;
    }

    private char[][] copyGrid(char[][] grid)
    {
        char[][] newGrid = new char[gridSize][gridSize];

        //explicit copy so array is not a reference to previous array
        for (int i = 0; i < gridSize; i++)
        {
            for (int j = 0; j < gridSize; j++)
            {
                newGrid[i][j] = grid[i][j];
            }
        }
        return newGrid;
    }
    
}
