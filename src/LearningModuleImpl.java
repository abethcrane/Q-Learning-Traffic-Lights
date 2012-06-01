/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.LearningModule;
import interfaces.TrafficLight;
import interfaces.RoadMap;
import interfaces.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LearningModuleImpl implements LearningModule
{
    // TODO:  MERGING
    private static final int numCarSpaces = 9;
    private static final int numActions = 2;
    private static final int numRoads = 2;
    private static final int numStates = 
            (int)java.lang.Math.pow(numCarSpaces,numRoads) * numActions;
    private static final int arraySize = 100000; // 5 digit hashCodes
    private static final int numTrafficLights = 2; // modify this
    // FIXME: {alpha, gamma, epsilon} are probably dependent - how?
    private static final float epsilon = (float)0.1;
    private static final float gamma = (float)0.9;
    private float alpha = (float)0.1;


    private int counter;
    // TODO: MERGING
    private ActionImpl lastAction = new ActionImpl();
    private ArrayList<Float> qValues = new ArrayList<Float>(arraySize);
    private ActionImpl[] actions = new ActionImpl[numActions];
    private int[] prevReward = new int[numTrafficLights];

    LearningModuleImpl() {
        counter = 0;
        actions[0] = new ActionImpl(false);
        actions[1] = new ActionImpl(true);
        for (int i = 0; i < arraySize; i++) {

            qValues.add(i,(float)0.0);
        }
    }

    @Override
    public void updateTrafficLights(
            RoadMap r,
            List<TrafficLight> trafficLights
            ) {
        /*
        //So far, naive 'switch at every ten steps' counter
        //disregards the actual state of the road
        counter++;
        if (counter == 10) {
            for (TrafficLight light : trafficLights) {
                light.switchLight();
            }
            counter = 0;
        }
         */
        // Less naive, using the optimal policy (i.e. best q-value)
        int trafficLightNum = 0; 
        for (TrafficLight t : trafficLights) {
            Action a = getAction(r, t);
            if (a.action()) { // if we add more actions, change this
                t.switchLight();
            }   
            // Going to need a queue of these
            prevReward[trafficLightNum] = reward(r, t); // is this r(s) or r(s')?
            trafficLightNum++;
        }

    }

    @Override
    public void learn(RoadMap s, RoadMap sPrime, List<TrafficLight> trafficLights) {
        //uses a = lastAction
        //currently doesn't learn a lot

        updateAlpha();
        //use old hashcode and current state
        int trafficLightNum = 0;
        for (TrafficLight t: trafficLights) {
            float newQValue = ((1 - alpha) * qValues.get(s.hashCode(t, lastAction))) + (alpha * (prevReward[trafficLightNum] + (gamma * getMaxQValue(sPrime, t))));
            qValues.set(s.hashCode(t, lastAction), newQValue);
            trafficLightNum++; 
        }

    }

    public float getMaxQValue (RoadMap sPrime, TrafficLight t) {
        Action a;
        float highestQ = 0;
        for (int i = 0; i < numActions; i++) {
            a = actions[i];
            if (qValues.get(sPrime.hashCode(t,a)) > highestQ) {
                highestQ = qValues.get(sPrime.hashCode(t,a));
            }
        }  

        return highestQ;      
    }

    //Reward -1.0 if a car is stopped at a red light on either road, zero otherwise.
    public int reward(RoadMap r, TrafficLight t) {
        // work this out based on the cars array of the traffic light

        // if the hash of the roadmap traffic light (irrelevant of action)
        // starts at 
        //1st 0 = 4 digits
       // 2nd 0  = fine
        //1st and 2nd 0 =3 digits
    	// Doesn't matter what the action is, need it to get the hash
    	
    	int rewardNum = 0;
    	
    	ActionImpl a = new ActionImpl(false);
        int hashCode = r.hashCode(t,a);
        // If it's 3 digits both roads have a car at 0
        if (hashCode / 1000 == 0) {
        	rewardNum = -1;
        // If it's 4 digits one road has a car at 0		
        } else if (hashCode/10000 == 0) {
        	rewardNum = -1;
        // Else if the second digit is 0 there's a car at one of the roads
        } else if ((hashCode/1000)%10 == 0) {
        	rewardNum = -1;
        }
        
        return rewardNum;        		
    }

    public ActionImpl getAction (RoadMap r, TrafficLight t) {
        ActionImpl a = new ActionImpl();

        float highestQ = 0;
        Action highestAction = new ActionImpl();
        for (int i = 0; i < numActions; i++) {
            a = actions[i];
            if (qValues.get(r.hashCode(t,a)) > highestQ) {
                highestQ = qValues.get(r.hashCode(t,a));
                highestAction = a;
            }
        }

        Random rand = new Random();
        float probability = rand.nextFloat();

        // If our probability is less than epsilon we return a different action
        // Picking between them with uniform probability
        if (probability <= epsilon) {
            highestAction = actions[rand.nextInt(numActions)];
        }

        return (ActionImpl) highestAction;
    }

    public void updateAlpha () {
        // potentially update ALPHA
        alpha = alpha;
    }


}
