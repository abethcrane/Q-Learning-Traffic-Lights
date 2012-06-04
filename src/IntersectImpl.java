/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.Intersect;
import interfaces.Road;
import java.util.List;

public class IntersectImpl implements Intersect {
	private List<Road> in, out;
	private boolean[] lightSetting;
	private int[] delay;
	private final int maxDelay = 3;    

	public IntersectImpl(List<Road> in, List<Road> out) {
		this.in = in;
		this.out = out;
		this.lightSetting = new boolean[in.size()];
		this.delay = new int[in.size()];
	}

	public List<Road> in() {
		return in;
	}

	public List<Road> out() {
		return out;
	}

	public boolean[] lightSetting() {
		return lightSetting;
	}

	public int[] getDelay()  {
		return delay;
	}    

	public void switchLight(boolean[] newAction) {
		for (int i = 0; i < newAction.length; i++) {
			if (newAction[i]) {
				if (getDelay()[i] == 0) {
					lightSetting[i] = !lightSetting[i];
					delay[i] = maxDelay;
				}
			}
		} 
	}
	
    public void clock() {
    	for (int i = 0; i < in.size(); i++) {
	        if (delay[i] > 0) {
	            delay[i]--;
	        }
    	}
    }	

}
