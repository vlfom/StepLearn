package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.State;

import java.util.ArrayList;
import java.util.Collections;

public class RobotState extends State {
    public int legsCount;
    public int supportingLegIndex;
    public ArrayList<Integer> thighAngle, shinAngle;

    public RobotState(int legsCount) {
        this.thighAngle = new ArrayList<>(Collections.nCopies(legsCount, 0));
        this.shinAngle = new ArrayList<>(Collections.nCopies(legsCount, 0));
    }

    public RobotState(int legsCount, int supportingLegIndex,
                      ArrayList<Integer> thighAngle, ArrayList<Integer>
                              shinAngle) {
        this.legsCount = legsCount;
        this.supportingLegIndex = supportingLegIndex;
        this.thighAngle = new ArrayList<>(legsCount);
        this.shinAngle = new ArrayList<>(legsCount);
        for (int i = 0; i < legsCount; ++i) {
            this.thighAngle.add(thighAngle.get(i));
            this.shinAngle.add(shinAngle.get(i));
        }
    }

    @Override
    public String toString() {
        String string = "State: ";
        for (int i = 0; i < legsCount; ++i) {
            string += "{" + thighAngle.get(i) + ", " + shinAngle.get(i) + " "
                    + (shinAngle.get(i) - thighAngle.get(i)) +
                    "} ";
        }
        string += "{" + this.supportingLegIndex + "}";
        return string;
    }

    @Override
    public long hash() {
        long hash = 0;
        for (int i = 0; i < legsCount; ++i) {
            hash = hash * 181 + thighAngle.get(i);
        }
        for (int i = 0; i < legsCount; ++i) {
            hash = hash * 181 + shinAngle.get(i);
        }
        hash = hash*181 + supportingLegIndex;
        return hash;
    }

    @Override
    public Object copy() {
        return new RobotState(legsCount, supportingLegIndex,
                thighAngle, shinAngle);
    }
}
