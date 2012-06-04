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
import java.util.Random;

public class Main {
    public static void main (String[] args) {
        //Graphics and runtime parameters
        int runTime = 50200;
        int quietTime = 50000;
        boolean graphicalOutput = true;
        boolean consoleOutput = false;
        boolean output = graphicalOutput || consoleOutput;
        double trafficIntensity = 0.2;
        int score = 0;

        //Initialise map, list of cars currently on map, and list of 
        //trafficlights
        RoadMap map = new RoadMapImpl();
        List<Car> cars = new ArrayList<Car>();
        List<TrafficLight> lights = new ArrayList<TrafficLight>();
        lights.add(new TrafficLightImpl(new Coords(20, 20), false));
        lights.add(new TrafficLightImpl(new Coords(20, 40), true));
        lights.add(new TrafficLightImpl(new Coords(40, 20), true));
        lights.add(new TrafficLightImpl(new Coords(40, 40), false));

        //Set actionposition based on arg1
        LearningModule learner = new LearningModuleImpl();
        Viewer v = graphicalOutput ? new Viewer() : null;

        Random rand = new Random();        
        
        //Basic logic for each time step
        // - change traffic lights if required - call a function from 
        //   'learning' class to do this
        // - move cars in their current direction by velocity (modify 
        //   velocity if necessary - using CarAI)
        // - spawn cars at extremities
        // - Now that we have the new state, update the qvalue for the
        //  previous s,a pair
        for (int t = 0; t < runTime; ++t) {
            
            RoadMap curState = map.copyMap();
            curState.addCars(cars);
            RoadMap nextState = curState.copyMap();

            for (TrafficLight l : lights) {
            	l.switchLight(learner.decide(curState, l));
            }

            List<Car> toRemove = new ArrayList<Car>();
            for (Car c : cars) {
                //c.move(curState.getClosestTrafficLight(c, lights), nextState);
            	c.move();
                //int x = c.getCoords().getX(), y = c.getCoords().getY();
                //if (x < 0 || x >= 60 || y < 0 || y >= 60) {
            	if (c.distAlongRoad() == c.getRoad().length()) {
                    toRemove.add(c);
                }
            }
            cars.removeAll(toRemove);

            for (Coords e : map.getRoadEntrances()) {
                double r = Math.random();
                if (r <= trafficIntensity && !curState.carAt(e)) {
                    cars.add(new CarImpl(
                        road, rand.nextInt(road.lanes()), map.getStartingVelocity(e)));
                }
            }

            for (TrafficLight l : lights) {
                learner.learn(l, curState, nextState);
            }

            /* OLD CODE: WORKS FINE WITH OLD ROADMAP
            //Params required to learn
            RoadMap currentState = map.copyMap();
            currentState.addCars(cars);
            List<Boolean> switchedLights;
            List<Integer> states = new ArrayList<Integer>();
            List<Integer> nextStates = new ArrayList<Integer>();
            List<Integer> rewards = new ArrayList<Integer>();

            //Save the states of each traffic light before updating
            for (TrafficLight light : lights) {
                states.add(currentState.stateCode(light));
            }

            //Use the learned values to update the traffic lights
            switchedLights = learningModule.updateTrafficLights(
                    currentState, lights, t
            );

            //copy updated state of map
            RoadMap nextState = currentState.copyMap();

            //Move cars currently on map
            List<Car> carsToRemove = new ArrayList<Car>();
            for (Car car : cars) {
                car.move(
                        currentState.getClosestTrafficLight(
                                car, lights),
                        nextState);
                int x=car.getCoords().getX(), y=car.getCoords().getY();
                if (x<0 || x>=60 || y<0 || y>=60) {
                     carsToRemove.add(car);
                }
            }
            cars.removeAll(carsToRemove);

            //Spawn cars onto map extremities
            for (Coords roadEntrance : map.getRoadEntrances()) {
                if (
                    Math.random() <= trafficIntensity &&
                    !currentState.carAt(roadEntrance)
                ) {
                    Car c = new CarImpl(
                            new Coords(roadEntrance),
                            map.getStartingVelocity(roadEntrance)
                    );
                    cars.add(c);
                }
            }
            nextState.addCars(cars);

            // Updates q-values
            //calculate reward and state code for each traffic light
            for (TrafficLight light : lights) {
                rewards.add(learningModule.reward(
                        nextState.stateCode(light)));
                nextStates.add(nextState.stateCode(light));
            }

            //Learn on the new state and reward with reference to 
            //the previous state and action
            learningModule.learn(
                states, switchedLights, rewards, nextStates, 
                lights
            );
            */

            if (t >= quietTime) {
                if (graphicalOutput) {
                    v.view(map, cars, lights);
                }
                if (consoleOutput) {
                    //map.print(cars, lights);
                }
                if (output) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.err.println("Unable to sleep.");
                    }
                }
                for (Car c : cars) {
                    score += c.getVelocity() == 0 ? -1 : 0;
                }
            }
            for (TrafficLight l : lights) {
                l.clock();
            }
        }
        System.out.println("Score " + (float)score/(runTime-quietTime));
    }
}
