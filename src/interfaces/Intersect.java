/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
*/

package interfaces;

import java.util.List;

public interface Intersect {

    List<Road> in();

    List<Road> out();
    
    int lightSetting();

    List<Integer> delay();

    void clock();

    void setLight(int setting);
}
