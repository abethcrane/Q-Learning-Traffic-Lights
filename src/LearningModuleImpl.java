/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import interfaces.LearningModule;
import interfaces.TrafficLight;

import java.util.List;

//Implementation of methods in interfaces.LearningModule interface
public class LearningModuleImpl implements LearningModule
{

   final static int NUM_CAR_SPACES = 9;
   final static int NUM_ACTIONS = 2;
   final static int NUM_ROADS = 2;
   final static int NUM_STATES =  java.lang.Math.pow(NUM_CAR_SPACES, NUM_ROADS) * NUM_ACTIONS;  
   final static float EPSILON = 0.1;
   final static float GAMMA = 0.9;
   final static float ALPHA = 0.1;
   
   //Might need to be a new state - would base it off traffic lights
   int[] qValues = new int[NUM_STATES];
   ActionImpl[] actions = new ActionImpl[NUM_ACTIONS];
   TrafficLight s;
   
   // we pass in traffic light t to the learner
   //fuck yeah, this.s = t
   // updates s for a while
   // then we never tell t about it
   // and t updates itself back in main
   // and t never tells us about that
   
   LearningModuleImpl(TrafficLight trafficlight) {
         this.s = trafficlight;   
    }

   public void learn() {
      Action a = getAction();
      // this just changes the lights, if we 
      updateAlpha();
      qValues[s.hashCode(a)] = ((1 - ALPHA) * qValues[s.hashCode(a)])
         + (ALPHA * (reward() + (GAMMA * getMaxQValue(s.getNextState(a))) ));
   }

   public int getMaxQValue (TrafficLight s) {
      Action a;
      int highestQ = 0;
      for (int i = 0; i < NUM_ACTIONS; i++) {
         a = actions[i];
         if (qValues[s.hashCode(a)] > highestQ) {
            highestQ = qValues[s.hashCode(a)];
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
         if (qValues[s.hashCode(a)] > highestQ) {
            highestQ = qValues[s.hashCode(a)];
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


/*
    @Override
    public void updateTrafficLights(List<TrafficLight> trafficLights, Action a) {
        //So far, naive 'switch at every ten steps' counter
        //counter++;
        if (a.toSwitch()) {
            for (TrafficLight light : trafficLights)
            {
                light.switchLight();
            }
        }
    }*/
}
