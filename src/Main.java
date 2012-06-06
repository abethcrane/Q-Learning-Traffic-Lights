/*
COMP9417 Machine Learning
Major Project - Traffic Lights Reinforcement Learning
Beth Crane
Gill Morris
Nathan Wilson
 */

import interfaces.Car;
import interfaces.LearningModule;
import interfaces.RoadMap;
import interfaces.Road;
import interfaces.Intersect;
import utils.Coords;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main (String[] args) {
        //Graphics and runtime parameters
        int runTime = 50200;
        int quietTime = 50000;
        boolean graphicalOutput = true;
        boolean consoleOutput = false;
        boolean output = graphicalOutput || consoleOutput;
        double trafficIntensity = 0.2;

        RoadMap map = new RoadMapImpl();
        LearningModule learner = new LearningModuleImpl();
        Viewer viewer = new Viewer(map);
        for (int t = 0; t < runTime; ++t) {
            System.out.println("Time " + t);
            List<Intersect> _ = map.intersections();
            System.out.println("Intersections for " + map + " are " + _);
            int n = _.size(), j = 0;
            int[] a = new int[n], s = new int[n], sp = new int[n];

            for (Intersect i : map.intersections()) {
                s[j] = learner.stateCode(i, map);
                a[j] = learner.decide(s[j]);
                i.setLight(a[j++]);
            }
            map.clock();
            for (int u = 0; u < map.nRoads(); ++u) {
                for (int v = 0; v < map.road(u).lanes(); ++v) {
                    if (Math.random() < trafficIntensity) {
                        map.spawn(u, v);
                    }
                }
            }
            j = 0;
            for (Intersect i : map.intersections()) {
                sp[j] = learner.stateCode(i, map);
                learner.learn(a[j], s[j], sp[j]);
                ++j;
            }

            if (t >= quietTime) {
                if (output) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        System.err.println("Unable to sleep.");
                    }
                }
            }
        }
    }
}
