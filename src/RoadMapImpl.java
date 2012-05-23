/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.Car;
import interfaces.RoadMap;
import interfaces.TrafficLight;
import utils.Coords;
import utils.Velocity;

import java.util.ArrayList;
import java.util.List;

//Roadmap Implementation class - implements methods from interfaces.RoadMap
public class RoadMapImpl implements RoadMap
{
    private char[][] grid;
    int gridSize = 40;
    private List<Coords> roadEntrances = new ArrayList<Coords>();

    RoadMapImpl()
    {
        grid = new char[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++)
        {
            for (int j = 0; j < gridSize; j++)
            {
                grid[i][j] = 'x';
                if (i == (gridSize/2) || j == (gridSize/2))
                {
                    grid[i][j] = ' ';
                }
            }
        }
        //Manually add entrances to roads
        roadEntrances.add(new Coords(20, 0));
        roadEntrances.add(new Coords(0, 20));
    }

    public RoadMapImpl(char[][] newGrid)
    {
        grid = copyGrid(newGrid);
        //Manually add entrances to roads
        roadEntrances.add(new Coords(20, 0));
        roadEntrances.add(new Coords(0, 20));
    }

    @Override
    public void print(List<Car> cars, List<TrafficLight> trafficLights)
    {
        //copy grid and place cars onto it
        char[][] newGrid = copyGrid(grid);
        for (Car car : cars)
        {
            newGrid[car.getPosition().getX()][car.getPosition().getY()] = 'C';
        }
        for(TrafficLight light : trafficLights)
        {
            if (light.horizontalGreen())
            {
                newGrid[light.getCoords().getX()][light.getCoords().getY()] = '>';
            }
            else
            {
                newGrid[light.getCoords().getX()][light.getCoords().getY()] = 'v';
            }
        }

        //print new grid to screen
        int i,j;
        for( i=0; i < gridSize; i++ ) {
            for( j=0; j < gridSize; j++ ) {
                System.out.print(newGrid[i][j]);
            }
            System.out.println("");
        }

        System.out.println("");
    }

    @Override
    public List<Coords> getRoadEntrances()
    {
        return roadEntrances;
    }

    @Override
    public Velocity getStartingVelocity(Coords roadEntrance)
    {
        if (roadEntrance.getX() == 0)
        {
            return new Velocity(1, 0);
        }
        if (roadEntrance.getX() == gridSize)
        {
            return new Velocity(-1, 0);
        }
        if (roadEntrance.getY() == 0)
        {
            return new Velocity(0, 1);
        }
        if (roadEntrance.getY() == gridSize)
        {
            return new Velocity(0, -1);
        }
        return null;
    }

    @Override
    public RoadMap copyMap()
    {
        return new RoadMapImpl(grid);
    }

    @Override
    public void addCars(List<Car> cars)
    {
        //place cars onto grid
        for (Car car : cars)
        {
            grid[car.getPosition().getX()][car.getPosition().getY()] = 'C';
        }
    }

    @Override
    public boolean nextNonCarSquareIsTrafficLight(Coords start, Velocity direction, TrafficLight trafficLight)
    {
        //iterate along all squares in front until you reach an empty square
        Coords current = new Coords(start.getX(), start.getY());
        while (current.getX() < gridSize && current.getX() >= 0 &&
                current.getY() < gridSize && current.getY() >= 0 &&
                (grid[current.getX()][current.getY()] != ' ') &&
                !trafficLight.getCoords().equals(current))
        {
            current.setX(current.getX() + direction.getXSpeed());
            current.setY(current.getY() + direction.getYSpeed());
        }

        //return true if said empty square is a traffic light, false otherwise
        return trafficLight.getCoords().equals(current);

    }

    @Override
    public boolean carAt(Coords coords)
    {
        return grid[coords.getX()][coords.getY()] == 'C';
    }

    private char[][] copyGrid(char[][] grid)
    {
        char[][] newGrid = new char[gridSize][gridSize];

        //explicit copy so array is not a reference to previous array
        int i,j;
        for( i=0; i < gridSize; i++ ) {
            for( j=0; j < gridSize; j++ ) {
                newGrid[i][j] = grid[i][j];
            }
        }
        return newGrid;
    }
}