package interfaces;/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

import java.util.List;

//Contains the logic for our reinforcement learning.
public interface LearningModule {

    int decide(int s);

    void learn(int a, int s, int sp);

    int stateCode(Intersect i, RoadMap m);

    void setRLParam(double alpha, double gamma, double epsilon);
}
