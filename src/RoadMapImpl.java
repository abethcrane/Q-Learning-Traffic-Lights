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

//Roadmap Implementation class - implements methods from interfaces.RoadMap
public class RoadMapImpl implements RoadMap {
    public final int gridSize = 40;
    private final Coords[] defaultEntrances =
        {new Coords(gridSize-1, gridSize/2-2), new Coords(gridSize/2-2, 0),
        new Coords(0, gridSize/2), new Coords(gridSize/2, gridSize-1)};
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
        Collections.addAll(roadEntrances, defaultEntrances);
    }

    @Override
    public void print(List<Car> cars, List<TrafficLight> trafficLights) {
        //copy grid and place cars onto it
        char[][] newGrid = copyGrid(grid);
        for (Car car : cars) {
            int x=car.getCoords().getX(), y=car.getCoords().getY();
            newGrid[x][y] = car.getChar();
        }
        for(TrafficLight light : trafficLights) {
            int x=light.getCoords().getX(), y=light.getCoords().getY();
            if (light.getDelay() != 0) {
                newGrid[x][y] = 'o';
            } else {
                newGrid[x][y] = light.horizontalGreen() ? '>' : 'v';
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

    @Override
    // Hash = 4 digit number (longer if more roads are added)
    /*
    1st - closest car position from intersection for road 1 (0-8, 9 if no cars) X
    2nd - closest car position from intersection for road 2 (0-8, 9 if no cars X
    3rd - light setting (ie 0-green, 1 red for one of the roads) X
     */
    // Needs to take in traffic light so it can tell which one to work
    // the things out for
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
        
        Coords c = new Coords(t.getCoords()).right().up();
        int i;
        // Road one we'll go vertically up
        for (i = 0; i < 9; i++) {
            c.setX(c.getX()-1);
            if (carAt(c)) {
                break;
            }
        }
        int v1 = i;
        c = new Coords(t.getCoords()).left().down();
        // Road one we'll go vertically down
        for (i = 0; i < 9; i++) {
            c.setX(c.getX()+1);
            if (carAt(c)) {
                break;
            }
        }
        int v2 = i;
        hash += 10*(Math.min(v1, v2));

        c = new Coords(t.getCoords()).left().up();
        // Road two we'll go horizontally left
        for (i = 0; i < 9; i++) {
            c.setY(c.getY()-1);
            if (carAt(c)) {
                break;
            }
        }
        int h1 = i;
        c = new Coords(t.getCoords()).right().down();
        // Road two we'll go horizontally right
        for (i = 0; i < 9; i++) {
            c.setY(c.getY()+1);
            if (carAt(c)) {
                break;
            }
        }
        int h2 = i;
        hash += 100*(Math.min(h1, h2));
    
        return hash;
    }

    @Override
    public List<Coords> getRoadEntrances() {
        return roadEntrances;
    }

    @Override
    public Velocity getStartingVelocity(Coords roadEntrance) {
        if (roadEntrance.getX() == 0) {
            return new Velocity(1, 0);
        }
        if (roadEntrance.getX() == gridSize-1) {
            return new Velocity(-1, 0);
        }
        if (roadEntrance.getY() == 0) {
            return new Velocity(0, 1);
        }
        if (roadEntrance.getY() == gridSize-1) {
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
        current.setX(current.getX() + direction.getXSpeed());
        current.setY(current.getY() + direction.getYSpeed());
        //need different traffic light coords depending on direction
        Coords trafficLightCoords = new Coords(0,0);
        if (direction.getXSpeed() == 0) {
            if (direction.getYSpeed() == 1) {
                trafficLightCoords = trafficLight.getCoords().left().up();
            } else if (direction.getYSpeed() == -1) {
                trafficLightCoords = trafficLight.getCoords().right().down();
            }
        } else if (direction.getYSpeed() == 0) {
            if (direction.getXSpeed() == 1) {
                trafficLightCoords = trafficLight.getCoords().right().up();
            } else if (direction.getXSpeed() == -1) {
                trafficLightCoords = trafficLight.getCoords().left().down();
            }
        }

        while (current.getX() < gridSize && current.getX() >= 0 &&
                current.getY() < gridSize && current.getY() >= 0 &&
                (grid[current.getX()][current.getY()] != roadChar) &&
                !trafficLightCoords.equals(current))
        {
            current.setX(current.getX() + direction.getXSpeed());
            current.setY(current.getY() + direction.getYSpeed());
        }

        return trafficLightCoords.equals(current);

    }

    @Override
    public boolean carAt(Coords coords) {
        return  
            0 <= coords.getX() && coords.getX() < gridSize &&
            0 <= coords.getX() && coords.getX() < gridSize &&
            grid[coords.getX()][coords.getY()] == carChar;
    }
    
    public boolean roadAt(Coords coords) {
        return  
            0 <= coords.getX() && coords.getX() < gridSize &&
            0 <= coords.getX() && coords.getX() < gridSize &&
            grid[coords.getX()][coords.getY()] == roadChar;
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
    
}
