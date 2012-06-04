/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import interfaces.Road;

public class RoadImpl implements Road {
    private final int lanes, length;

    public RoadImpl(int lanes, int length) {
        this.lanes = lanes;
        this.length = length;
    }

    public int lanes() {
        return lanes;
    }

    public int length() {
        return length;
    }
}
