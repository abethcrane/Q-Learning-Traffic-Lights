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

    public IntersectImpl(List<Road> in, List<Road> out) {
        this.in = in;
        this.out = out;
    }

    public List<Road> in() {
        return in;
    }

    public List<Road> out() {
        return out;
    }
}
