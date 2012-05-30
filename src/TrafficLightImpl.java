/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.TrafficLight;
import utils.Coords;

//Contains implementation of methods in interfaces.TrafficLight
public class TrafficLightImpl implements TrafficLight
{
    private Coords coords;
    private boolean horizontalGreen;
  
   final static int NUM_CAR_SPACES = 9;
   final static int NUM_ACTIONS = 2;
   final static int NUM_ROADS = 2;
   final static int NUM_STATES =  java.lang.Math.pow(NUM_CAR_SPACES, NUM_ROADS) * NUM_ACTIONS;  
   
   final static float EPSILON = 0.1;
   final static float GAMMA = 0.9;
   final static float ALPHA = 0.1;
   
   int[] qValues = new int[NUM_STATES];
   ActionImpl[] actions = new ActionImpl[NUM_ACTIONS];
   
   // This is given to us by road, and updated by road
   // Useful for getting the next state, and for reward
   CarImpl[][] cars = new CarImpl[NUM_ROADS][NUM_CAR_SPACES+1];
  
   public TrafficLightImpl(Coords coords, boolean horizontalGreen) {
        this.coords = coords;
        this.horizontalGreen = horizontalGreen;
    }

    @Override
    public void switchLight() {
        horizontalGreen = !horizontalGreen;
    }

    public Coords getCoords() {
        return coords;
    }

    @Override
    public boolean horizontalGreen() {
        return horizontalGreen;
    }
    
    @Override
    public hashCode (Action a) { 
      // write this
    }
   

   public void learn() {
      Action a = getAction();
      updateAlpha();
      qValues[hashCode(a)] = ((1 - ALPHA) * qValues[hashCode(a)])
         + (ALPHA * (reward() + (GAMMA * getMaxQValue(getNextState(a))) ));
   }

   public int getMaxQValue (TrafficLight s) {
      Action a;
      int highestQ = 0;
      for (int i = 0; i < NUM_ACTIONS; i++) {
         a = actions[i];
         if (qValues[hashCode(a)] > highestQ) {
            highestQ = qValues[hashCode(a)];
         }
      }  
      
      return highestQ;      
   }

   //Reward -1.0 if a car is stopped at a red light on either road, zero otherwise.
   public int reward() {
      // work this out based on the cars array of the traffic light
      return 0;
   }
   
   public ActionImpl getAction () {
      ActionImpl a;

      int highestQ = 0;
      Action highestAction = new ActionImpl();
      for (int i = 0; i < NUM_ACTIONS; i++) {
         a = actions[i];
         if (qValues[hashCode(a)] > highestQ) {
            highestQ = qValues[hashCode(a)];
            highestAction = a;
         }
      }
      
      Random rand = new Random();
      float probability = rand.nextFloat();
      
      // If our probability is less than epsilon we return a different action
      // Picking between them with uniform probability
      if (probability <= EPSILON) {
         highestAction = actions[rand.nextInt(NUM_ACTIONS)];
      }
      
      return highestAction;
   }

   public void updateAlpha () {
      // potentially update ALPHA
   }
   
    // road calls this and passes in the closest 10 road spots
    // if it's null no car
    // else all info about that car
    public void update (CarImpl[][] cars) {
     // update itself based on new road info
     this.cars = cars;
    }     
    
    // update itself with the action, works out where it's cars are at
    // etc
    // the cars array which is changed here gets overwritten by the road update
    public void getNextState (Action a) {
      // just updates the car array and the action
        if (a.toSwitch()) {
            switchLight();
        }      
        // based on this update, modify the cars array
        // so that we can use this for updating the qvalue
    }   
    
    
}
