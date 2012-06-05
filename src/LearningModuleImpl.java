/*
 * COMP9417 Machine Learning
 * Major Project - Traffic Lights Reinforcement Learning
 * Beth Crane
 * Gill Morris
 * Nathan Wilson
*/

import interfaces.*;
import java.util.*;
import utils.*;

public class LearningModuleImpl implements LearningModule {
    private static final int nStates = 1000, nActions = 2;
    private double alpha = 0.7, gamma = 0.9, epsilon = 0.1;
    private double[] q;
    private int[] lastAction;

    LearningModuleImpl() {
        lastAction = new int[nStates];
        q = new double[nStates];
        for (int i = 0; i < nStates; ++i) {
            q[i] = 0;
        }
    }

    public void setRLParam(double alpha, double gamma, double epsilon) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
    }

    private boolean stoppedCar(Car c) {
        return c.velocity() == 0;
    }

    public int decide(int s) {
        int bestAction = 0;
        double bestQ = -100;
        for (int i = 0; i < nActions; ++i) {
            if (q[10*s + i] > bestQ) {
                bestQ = q[10*s + i];
                bestAction = i;
            }
        }
        lastAction[s] = bestAction;
        return bestAction;
    }

    public void learn(int a, int s, int sp) {
        double newQ = (1-alpha) * q[s*nActions + a] +
            alpha * (reward(sp) + gamma*maxQ(sp));
        q[s*nActions + a] = newQ;
    }
    
    public int stateCode(Intersect l, RoadMap m) {
        int ret = l.lightSetting();
    	for (int i = 0; i < l.in().size(); ++i) {
    		Road r = l.in().get(i);
    		int closestCar = 9;
    		for (Car c : m.carsOn(i)) {
    			closestCar = Math.min(closestCar,  
                    r.length()-c.distAlongRoad());
    		}
    		ret += Math.pow(10, i+2)*closestCar;
    	}
    	return ret;
    }
    
    public double maxQ(int s) {
        double ret = -100;
        for (int i = 0; i < nActions; ++i) {
            ret = Math.max(ret, q[s*nActions + i]);
        }
        return ret;
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
}
