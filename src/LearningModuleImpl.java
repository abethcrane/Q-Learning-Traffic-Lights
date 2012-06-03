/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.*;

import java.util.*;

import utils.Coords;
import utils.Velocity;

public class LearningModuleImpl implements LearningModule
{
    // TODO:  MERGING
    private static final int numCarSpaces = 9;
    private static final int numActions = 2;
    private static final int numRoads = 2;
    private static final int numTrafficLights = 2; // modify this
    // FIXME: {alpha, gamma, epsilon} are probably dependent - how?
    private static final float epsilon = (float)0.1;
    private static final float gamma = (float)0.9;
    private float alpha = (float)0.1;
    private Map<Integer, Float> qValues = new HashMap<Integer, Float>();
    private ActionImpl[] actions = new ActionImpl[numActions];

    LearningModuleImpl() {
        actions[0] = new ActionImpl(false);
        actions[1] = new ActionImpl(true);
    }

    @Override
    public List<Boolean> updateTrafficLights(
            RoadMap r,
            List<TrafficLight> trafficLights
    ) {
        List<Boolean> switches = new ArrayList<Boolean>();
        // Less naive, using the optimal policy (i.e. best q-value)
        for (TrafficLight t : trafficLights) {
            boolean a = 
                (t.getDelay() == 0 && getAction(r, t).action());
            switches.add(a);
            if (a) {
                t.switchLight();
            }
            /*
            if (t.getDelay() == 0) {
                Action a = getAction(r, t);
                switches.add(a.action());
                if (a.action()) { // if we add more actions, change this
                    t.switchLight();
                }
            } else {
                //decrements counter and switches if necessary
                //switches.add(t.getDelay() == 1);
                switches.add(false);
            }
            */
            t.clock();
        }
        return switches;
    }

    @Override
    public List<Integer> updateTrafficLightsRandomly(RoadMap mapWithCars, List<TrafficLight> trafficLights)
    {
        List<Integer> switched = new ArrayList<Integer>();
        for (TrafficLight light : trafficLights) {
            //switch randomly
            double r = Math.random();
            if (r <= 0.5) {
                light.switchLight();
                switched.add(1);
            } else {
                switched.add(0);
            }
        }
        return switched;
    }

    @Override
    public void learn(List<Integer> pastStates, List<Boolean> switches, List<Integer> rewards, List<Integer> newStates, List<TrafficLight> lights) {
        //uses a = lastAction
        //currently doesn't learn a lot
        updateAlpha();

        //use old hashcode and current state
        for (int i = 0; i < lights.size(); i++) {
            if (lights.get(i).getDelay() == 0) {
                //for each traffic light we get the state code, action, reward and old qvalue
                int state = pastStates.get(i);
                int nextState = newStates.get(i);
                Action action = new ActionImpl(switches.get(i));
                int reward = rewards.get(i);
                Float qVal = qValues.get(state + 1000*action.actionInt());
                if (qVal == null)
                    qVal = (float)0.0;

                //calculate new
                Float newQValue = (((1 - alpha) * qVal) + (alpha*reward + (gamma * getMaxQValue(nextState))));
                qValues.put((state + 1000*action.actionInt()), newQValue);
            }
        }
    }

    public Float getMaxQValue (int state) {
        Float q1 = qValues.get(state + 1000);
        if (q1 == null)
            q1 = (float)0.0;

        Float q2 = qValues.get(state);
        if (q2 == null)
            q2 = (float)0.0;

        return Math.max(q1, q2);
    }

    //Reward -1.0 if a car is stopped at a red light on either road, zero otherwise.
    public int reward(int stateCode) {

    	//__1__ = horizontal green
    	// 0____ = car at 0 on horizontal
    	//_0___ = car at 0 on vertical
    	// hence 0_0__ = car stopped at horizontal road
    	// hence _01__ = car stopped at vertical road

    	int rewardNum = 0;

        // If it's 1 digit both roads have a car at 0
        if (stateCode / 10 == 0) {
        	rewardNum = -2;
        // If it's 2 digits horizontal road has car at 0
        // Hence we check if the light is 0 (red for horizontal)
        } else if (stateCode/100 == 0) {
        	if ((stateCode)%10 == 0) {
        		rewardNum = -1;
        	}
        // Else if the second digit is 0 there's a car at the vertical road
        // Hence we check if the light is 1 (red for vertical)
        } else if ((stateCode/10)%10 == 0) {
        	if (stateCode%10 == 1) {
        		rewardNum = -1;
        	}
        }

        return rewardNum;
    }

    //Reward -1*numCars stopped at a red light, + numCars queued up going through a greenlight
    public int reward2(int stateCode) {

    	//__1__ = horizontal green
    	// 0____ = car at 0 on horizontal
    	//_0___ = car at 0 on vertical
    	// hence 0_0__ = car stopped at horizontal road
    	// hence _01__ = car stopped at vertical road

    	
    	int rewardNum = 0;
        // If it's 1 digit both roads have 0 cars stopped
        if (stateCode / 10 == 0) {
        	rewardNum = 0;
        // If it's 2 digits horizontal road has 0 cars queued up
        // Hence if light is red for vertical, we check how many are in the vertical position
        } else if (stateCode/100 == 0) {
        	if ((stateCode)%10 == 1) {
        		rewardNum = -1 * (stateCode/10);
        	}
        // Else if the second digit is 0 there's a car at the vertical road
        // Hence we check if the light is 1 (red for vertical)
        } else {
        	// If the traffic light is red for horizontal we add on how many cars are stopped horizontally
			// And positive how many are queued up vertically
        	if (stateCode%10 == 0) {
        		rewardNum = -1 * (stateCode/100);
        		rewardNum += (stateCode/10)%10;
        	}	
        	// If traffic light is red for vertical we add on negative how many cars are stopped vertically
        	// And positive how many are queued up horizontally
        	if (stateCode%10 == 1) {
        		rewardNum = -1 * ((stateCode/10)%10);
        		rewardNum += stateCode/100;
        	}
        }
        

        return rewardNum;
    }

    //Reward -1*numCars stopped at a red light, + numCars queued up going through a greenlight
    // However minus 20 if there's a car directly on the other side of the intersection (i.e. not letting our car move)
    public int reward3(int stateCode) {

    	//__1__ = horizontal green
    	// 0____ = car at 0 on horizontal
    	//_0___ = car at 0 on vertical
    	// hence 0_0__ = car stopped at horizontal road
    	// hence _01__ = car stopped at vertical road

    	
    	int rewardNum = 0;

    	// work these out then mod by 1000%
    	
    	// if it's 4 digits then horizontally is bad to be horizontal (0____) 
    	if (stateCode/1000 == 0) {
    		// if light is currently 1 - green horizontal, and action is 0 = bad
    		// if light is currently 0 - red horizontal, and action is 1 = bad
    		if (stateCode%10 == 1) {
    			rewardNum -= 10;
    		}
        	// if it's 3 digits it's bad to be either    		
    	} else if (stateCode/100 == 0) {
    		rewardNum -= 10;
    		// if it's 5 digits with _0___ then it's bad to be vertical
    	} else if ((stateCode/100)%10 == 0) {
    		if (stateCode%10 == 0) {
    			rewardNum -= 10;
    		}
    	}
    	
    	
    	// Now we're working with 3 digits, like before
    	stateCode %= 1000;

        // If it's 1 digit both roads have 0 cars stopped
        if (stateCode / 10 == 0) {
        	rewardNum = 0;
        // If it's 2 digits horizontal road has 0 cars queued up
        // Hence if light is red for vertical, we check how many are in the vertical position
        } else if (stateCode/100 == 0) {
        	if ((stateCode)%10 == 1) {
        		rewardNum = -1 * (stateCode/10);
        	}
        // Else if the second digit is 0 there's a car at the vertical road
        // Hence we check if the light is 1 (red for vertical)
        } else {
        	// If the traffic light is red for horizontal we add on how many cars are stopped horizontally
			// And positive how many are queued up vertically
        	if (stateCode%10 == 0) {
        		rewardNum = -1 * (stateCode/100);
        		rewardNum += (stateCode/10)%10;
        	}	
        	// If traffic light is red for vertical we add on negative how many cars are stopped vertically
        	// And positive how many are queued up horizontally
        	if (stateCode%10 == 1) {
        		rewardNum = -1 * ((stateCode/10)%10);
        		rewardNum += stateCode/100;
        	}
        }
        

        return rewardNum;
    }
    
    
    //naive implementation of reward where you are penalised for each stopped car
    public int reward3(List<Car> cars, TrafficLight light) {
        int count = 0;
        for (Car car : cars) {
            if (car.stopped())
            {
                count++;
            }
        }
        if (light.getDelay() == 3) {
            count += 10;//extra penalty for switch
        }
        return Math.round((float)0.1 * count);
    }



    public Action getAction (RoadMap r, TrafficLight t) {
        ActionImpl a;

        double highestQ = -9999;
        Action highestAction = new ActionImpl();
        for (int i = 0; i < numActions; i++) {
            a = actions[i];
            Float q = qValues.get(r.stateCode(t) + 1000*i);
            if (q == null) {
                q = (float)0;
            }
            // FIXME: as the identifier suggests, we actually need
            // the highest q.
            // however, this temporarily gets us around the problem of
            // q(true) = 0 (always) and q(false) being generally in 
            // [-30, -5]
            if (q < highestQ) {
                highestQ = q;
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

        return highestAction;
    }

    public void updateAlpha () {
        // potentially update ALPHA
        alpha = alpha;
    }


}
