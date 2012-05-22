/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.Car;
import interfaces.LearningModule;
import interfaces.RoadMap;
import interfaces.TrafficLight;
import utils.Coords;
import java.util.ArrayList;
import java.util.List;

//Main class - this will start up the simulation and run it through
public class Main
{
   public static void main (String[] args)
   {
       //(do we need to take in any arguments? my thought is perhaps we should save the
       //     learned values to a file and pass that file as an argument if we wish to resume from a previous trial)

       //Initialise map, list of cars currently on map, and list of trafficlights
       RoadMap map = new RoadMapImpl();
       List<Car> cars = new ArrayList<Car>();
       List<TrafficLight> trafficLights = new ArrayList<TrafficLight>();
       //Add traffic lights initial state
       trafficLights.add(new TrafficLightImpl(new Coords(20,20),false));

       //Traffic density threshold - can make this a parameter if we want
       double trafficDensityThreshold = 0.5;

       //Initialise 'learner' - if we are resuming from a previous learnt run, we can pass necessary values in here
       LearningModule learningModule = new LearningModuleImpl();

       //While loop for a set number of time steps (say 100  - can make this an argument to our program if necessary)
       //Basic logic for each time step
       // - change traffic lights if required - call a function from 'learning' class to do this
       // - move cars in their current direction by velocity (modify velocity if necessary - using CarAI)
       // - spawn cars at extremities
       int timeToRun = 100;
       while (timeToRun > 0)
       {
           RoadMap mapWithCars = map.copyMap();
           mapWithCars.addCars(cars);

           //Change traffic lights - we will probably add parameters and things here,
           //     or change things in the traffic light class so that they know more about cars approaching, etc
           learningModule.updateTrafficLights(trafficLights);

           //Move cars currently on map
           List<Car> carsToRemove = new ArrayList<Car>();
           for (Car car : cars)
           {
               car.updateVelocity(trafficLights, mapWithCars);
               car.updatePosition();
               if (car.removeIfOffRoad(map))
                   carsToRemove.add(car);
           }
           cars.removeAll(carsToRemove);

           //Spawn cars onto map extremities
           for(Coords roadEntrance: map.getRoadEntrances())
           {
               double randomNumber = Math.random();
               if (randomNumber <= trafficDensityThreshold)
               {
                   cars.add(new CarImpl(roadEntrance, map.getStartingVelocity(roadEntrance)));
               }
           }

           //Next iteration
           map.print(cars, trafficLights);
           try
           {
               Thread.sleep(1000);
           }
           catch (InterruptedException e)
           {
               e.printStackTrace();
           }
           timeToRun--;
       }
   }
}
