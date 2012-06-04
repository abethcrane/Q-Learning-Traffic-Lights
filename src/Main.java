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

public class Main {
    public static void main (String[] args) {
        //Graphics and runtime parameters
        int runTime = 50200;
        int quietTime = 50000;
        boolean graphicalOutput = true;
        boolean consoleOutput = false;
        boolean output = graphicalOutput || consoleOutput;
        double trafficIntensity = 0.2;

        //Initialise map, list of cars currently on map, and list of 
        //trafficlights
        RoadMap map = new RoadMapImpl();
        List<Car> cars = new ArrayList<Car>();
        List<TrafficLight> trafficLights = 
                new ArrayList<TrafficLight>();
        trafficLights.add(
                new TrafficLightImpl(new Coords(20, 20),false));
        trafficLights.add(
                new TrafficLightImpl(new Coords(20, 40),true));
        trafficLights.add(
                new TrafficLightImpl(new Coords(40, 20),true));
        trafficLights.add(
                new TrafficLightImpl(new Coords(40, 40),false));

        //Set actionposition based on arg1
        LearningModule learningModule = new LearningModuleImpl();
        Viewer v = graphicalOutput ? new Viewer() : null;

        //Basic logic for each time step
        // - change traffic lights if required - call a function from 
        //   'learning' class to do this
        // - move cars in their current direction by velocity (modify 
        //   velocity if necessary - using CarAI)
        // - spawn cars at extremities
        // - Now that we have the new state, update the qvalue for the
        //  previous s,a pair
        for (int t = 0; t < runTime; ++t) {
            //Params required to learn
            RoadMap currentState = map.copyMap();
            currentState.addCars(cars);
            List<Boolean> switchedLights;
            List<Integer> states = new ArrayList<Integer>();
            List<Integer> nextStates = new ArrayList<Integer>();
            List<Integer> rewards = new ArrayList<Integer>();

            //Save the states of each traffic light before updating
            for (TrafficLight light: trafficLights) {
                states.add(currentState.stateCode(light));
            }

            //Use the learned values to update the traffic lights
            switchedLights = learningModule.updateTrafficLights(
                    currentState, trafficLights, t
            );

            //copy updated state of map
            RoadMap nextState = currentState.copyMap();

            //Move cars currently on map
            List<Car> carsToRemove = new ArrayList<Car>();
            for (Car car : cars) {
                car.move(
                        currentState.getClosestTrafficLight(
                                car, trafficLights),
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
            for (TrafficLight light : trafficLights) {
                rewards.add(learningModule.reward(
                        nextState.stateCode(light)));
                nextStates.add(nextState.stateCode(light));
            }

            //Learn on the new state and reward with reference to 
            //the previous state and action
            learningModule.learn(
                states, switchedLights, rewards, nextStates, 
                trafficLights
            );

            if (t >= quietTime) {
                if (graphicalOutput) {
                    v.view(map, cars, trafficLights);
                }
                if (consoleOutput) {
                    map.print(cars, trafficLights);
                }
                if (output) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}
