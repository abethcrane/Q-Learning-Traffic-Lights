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
import java.util.ArrayList;

public class IntersectImpl implements Intersect {
    private List<Road> in, out;
    private int lightSetting;
    private List<Integer> delay;
    private final int maxDelay = 3;    

    public IntersectImpl(List<Road> in, List<Road> out) {
        this.in = in;
        this.out = out;
        this.lightSetting = 0;
        this.delay = new ArrayList<Integer>(in.size());
    }

    public List<Road> in() {
        return in;
    }

    public List<Road> out() {
        return out;
    }

    public int lightSetting() {
        return lightSetting;
    }

    public List<Integer> delay() {
        return delay;
    }    

    public void setLight(int setting) {
        lightSetting = setting;
    }

    public void clock() {
        for (int i = 0; i < in.size(); ++i) {
            int u = delay.get(i);
            if (u > 0) {
                delay.set(u - 1, i);
            }
        }
    }
}
