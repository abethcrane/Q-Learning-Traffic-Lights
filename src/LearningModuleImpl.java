/*
   COMP9417 Machine Learning
   Major Project - Traffic Lights Reinforcement Learning
   Beth Crane
   Gill Morris
   Nathan Wilson
   */

import interfaces.*;
import java.util.*;
import utils.*;

public class LearningModuleImpl implements LearningModule {
    private static final int numCarSpaces = 9;
    private static final int numActions = 2;
    private static final int numRoads = 2;
    private static final int numTrafficLights = 2; // modify this
    private float epsilon = (float)0.1;
    private float gamma = (float)0.9;
    private float alpha = (float)0.1;
    private Map<Integer, Float> qValues = new HashMap<Integer, Float>();
    private ActionImpl[] actions = new ActionImpl[numActions];
    private float id = (float)-0.0;
    private int actionPosition = 1000; // action is a___ in stateCode 2, 
    // in stateCode3 its a_____

    LearningModuleImpl(int actionPosition) {
        actions[0] = new ActionImpl(false);
        actions[1] = new ActionImpl(true);
        this.actionPosition = actionPosition;
    }

    public void setRLParam(float alpha, float gamma, float epsilon) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
    }

    @Override
    public List<Boolean> updateTrafficLights(
            RoadMap r, List<TrafficLight> trafficLights, int timeRan) {
        List<Boolean> switches = new ArrayList<Boolean>();
        // Less naive, using the optimal policy (i.e. best q-value)
        for (TrafficLight t : trafficLights) {
            boolean a = (t.getDelay() == 0 && getAction(r, t).action());
            //Add whether or not we are changing the light - note we do
            //not add the action chosen, but whether the clock switches.
            //this is because we wish to learn on the change in state 
            //which takes 3 clock cycles to happen after we make an 
            //action

            switches.add(t.getDelay() == 1);
            //If we change the light - do NOT update the clock
            //changing the light resets the delay - if we delay and
            //then immediately update clock then the delay is
            //prematurely decremented
            if (a) {
                t.switchLight();
            } else {
                t.clock();
            }
        }
        return switches;
    }

    @Override
    public List<Boolean> updateTrafficLightsRandomly(
            RoadMap mapWithCars, List<TrafficLight> trafficLights) {
        List<Boolean> switched = new ArrayList<Boolean>();
        for (TrafficLight light : trafficLights) {
            double r = Math.random();
            boolean s = r <= 0.5;
            switched.add(s);
            if (s) {
                light.switchLight();
            }
        }
        return switched;
    }

    @Override
    public void learn(
            List<Integer> pastStates, List<Boolean> switches, 
            List<Integer> rewards, List<Integer> newStates, 
            List<TrafficLight> lights) {
        updateAlpha();

        //use old hashcode and current state
        for (int i = 0; i < lights.size(); i++) {
            //for each traffic light we get the state code, action, 
            //reward and old qvalue
            int state = pastStates.get(i);
            int nextState = newStates.get(i);
            Action action = new ActionImpl(switches.get(i));
            int reward = rewards.get(i);
            Float qVal = qValues.get(
                    state + actionPosition*action.actionInt());
            if (qVal == null) {
                qVal = (float)id;
            }

            //calculate new
            Float newQValue = 
                    ((1 - alpha) * qVal) + 
                    alpha*(reward + (gamma * getMaxQValue(nextState)));
            qValues.put(
                    (state + actionPosition*action.actionInt()), 
                    newQValue
            );
        }
    }

    public Float getMaxQValue (int state) {
        Float q1 = qValues.get(state + actionPosition);
        if (q1 == null) {
            q1 = (float)id;
        }

        Float q2 = qValues.get(state);
        if (q2 == null) {
            q2 = (float)id;
        }

        return Math.max(q1, q2);
    }

    //Reward -1.0 if a car is stopped at a red light on either road, 
    //zero otherwise.
    public int reward(int stateCode) {
        //__1__ = horizontal green
        // 0____ = car at 0 on horizontal
        //_0___ = car at 0 on vertical
        // hence 0_0__ = car stopped at horizontal road
        // hence _01__ = car stopped at vertical road

        int rewardNum;

        // If it's 1 digit both roads have a car at 0
        if (stateCode / 10 == 0) {
            rewardNum = -2;
        // If it's 2 digits horizontal road has car at 0
        // Hence we check if the light is 0 (red for horizontal)
        } else if (stateCode / 100 == 0 && stateCode % 10 == 0) {
            rewardNum = -1;
        // Else if the second digit is 0 there's a car at the
        // vertical road
        // Hence we check if the light is 1 (red for vertical)
        } else if (stateCode/10 % 10 == 0 && stateCode % 10 == 1) {
            rewardNum = -1;
        } else {
            rewardNum = 0;
        }

        return rewardNum;
    }

    //Reward -1*numCars stopped at a red light, + numCars queued up
    //going through a greenlight
    public int reward2(int stateCode) {
        //__1__ = horizontal green
        // 0____ = car at 0 on horizontal
        //_0___ = car at 0 on vertical
        // hence 0_0__ = car stopped at horizontal road
        // hence _01__ = car stopped at vertical road

        int rewardNum;
        // If it's 1 digit both roads have 0 cars stopped
        if (stateCode / 10 == 0) {
            rewardNum = 0;
        // If it's 2 digits horizontal road has 0 cars queued up
        // Hence if light is red for vertical, we check how many 
        // are in the vertical position
        } else if (stateCode / 100 == 0 && stateCode % 10 != 0) {
            rewardNum = 
                    stateCode % 10 == 1 ? -1 :
                    stateCode % 10 == 2 ? -2 :
                            -100;
        // Else if the second digit is 0 there's a car at the 
        // vertical road
        // Hence we check if the light is 1 (red for vertical)
        } else {
            // If the traffic light is red for horizontal we add on 
            // how many cars are stopped horizontally
            // And positive how many are queued up vertically
            if (stateCode % 10 != 1) {
                rewardNum = -(stateCode/100) + stateCode/10%10;
            }	
            // If traffic light is red for vertical we add on negative 
            // how many cars are stopped vertically
            // And positive how many are queued up horizontally
            else if (stateCode % 10 == 1) {
                rewardNum = -(stateCode / 10 % 10) + stateCode/100;
                // Else if amber both directions are stopped
            } else if (stateCode%10 == 2) {
                rewardNum = -(stateCode / 10 % 10) - stateCode/100;
            } else {
                rewardNum = 0;
            }
        }

        return rewardNum;
    }

    //Reward -1*numCars stopped at a red light, + numCars queued up
    //going through a greenlight. However minus 20 if there's a car 
    //directly on the other side of the intersection (i.e. not letting
    //our car move)
    public int reward3(int stateCode) {
        //__1__ = horizontal green
        // 0____ = car at 0 on horizontal
        //_0___ = car at 0 on vertical
        // hence 0_0__ = car stopped at horizontal road
        // hence _01__ = car stopped at vertical road

        int rewardNum = 0;

        // work these out then mod by 1000%
        // if it's 4 digits then horizontally is bad to be 
        // horizontal (0____) 
        if (stateCode/1000 == 0) {
            // if light is currently 1 - green horizontal, and action is 
            // 0 = bad. if light is currently 0 - red horizontal, 
            // and action is 1 = bad
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
            rewardNum += 0;
        // If it's 2 digits horizontal road has 0 cars queued up
        // Hence if light is red for vertical, we check how many
        // are in the vertical position
        } else if (stateCode/100 == 0) {
            if ((stateCode)%10 == 1 || (stateCode%10) == 2) {
                rewardNum -= (stateCode/10);
            }
        // Else if the second digit is 0 there's a car at the
        // vertical road. Hence we check if the light is 1 
        // (red for vertical)
        } else {
            // If the traffic light is red for horizontal we add on 
            // how many cars are stopped horizontally
            // And positive how many are queued up vertically
            if (stateCode%10 == 0|| (stateCode%10) == 2) {
                rewardNum -=  (stateCode/100);
                rewardNum += (stateCode/10)%10;
            }	
            // If traffic light is red for vertical we add on 
            // negative how many cars are stopped vertically
            // And positive how many are queued up horizontally
            else if (stateCode%10 == 1) {
                rewardNum -= ((stateCode/10)%10);
                rewardNum += stateCode/100;
            // Else if amber both directions are stopped
            } else if (stateCode%10 == 2) {
                rewardNum -= ((stateCode/10)%10);
                rewardNum -= (stateCode/100);
            }
        }

        return rewardNum;
    }


    //naive implementation of reward where you are penalised for 
    //each stopped car;
    public int reward4(List<Car> cars, TrafficLight light) {
        int numStopped = 0, hasSwitched = 0;
        for (Car car : cars) {
            if (car.stopped()) {
                numStopped++;
            }
        }
        if (light.getDelay() == 3) {
            hasSwitched = 1;
        }
        return Math.round((float)(-0.1) * (numStopped+10*hasSwitched));
    }



    public Action getAction(RoadMap r, TrafficLight t) {
        ActionImpl a;

        double highestQ = -100;
        Action highestAction = new ActionImpl();
        for (int i = 0; i < numActions; i++) {
            a = actions[i];
            Float q = qValues.get(r.stateCode(t) + actionPosition*i);
            if (q == null) {
                q = (float)0;
            }
            if (q > highestQ) {
                highestQ = q;
                highestAction = a;
            }
        }

        Random rand = new Random();
        float probability = rand.nextFloat();
        // If our probability is less than epsilon we return a
        // different action
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
