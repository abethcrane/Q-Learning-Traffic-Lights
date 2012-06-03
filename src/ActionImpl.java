/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

//Implementation of methods in LearningModule interface
public class ActionImpl implements Action {
    private boolean toSwitch;

    public ActionImpl(boolean toSwitch) {
        this.toSwitch = toSwitch;
    }

    public ActionImpl() {
        this(false);
    }

    public boolean action() {
        return toSwitch;
    }

    public int actionInt() {
        return toSwitch ? 1 : 0;
    }
}
