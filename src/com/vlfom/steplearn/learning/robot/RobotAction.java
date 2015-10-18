package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.Action;

import java.util.ArrayList;
import java.util.Collections;

public class RobotAction extends Action {
    public ArrayList<Integer> thighRotation, shinRotation;
    public int supportingLegIndex;

    public RobotAction() {
    }

    public RobotAction(int legsCount) {
        this.thighRotation = new ArrayList<>(Collections.nCopies(legsCount, 0));
        this.shinRotation = new ArrayList<>(Collections.nCopies(legsCount, 0));
    }

    public RobotAction(ArrayList<Integer> thighRotation, ArrayList<Integer>
            shinRotation, int supportingLegIndex) {
        this.thighRotation = (ArrayList<Integer>) thighRotation.clone();
        this.shinRotation = (ArrayList<Integer>) shinRotation.clone();
        this.supportingLegIndex = supportingLegIndex;
    }

    @Override
    public String toString() {
        String string = "Action: ";
        for (int i = 0; i < shinRotation.size(); ++i) {
            string += "{" + thighRotation.get(i) + ", " + shinRotation.get(i)
                    + ", " + (shinRotation.get(i) - thighRotation.get(i)) +
                    "} ";
        }
        string += "{" + supportingLegIndex + "}";
        return string;
    }

    @Override
    public long hash() {
        long hash = 0;
        for (Integer angle : thighRotation) {
            hash = hash * 29 + (angle + 14);
        }
        for (Integer angle : shinRotation) {
            hash = hash * 29 + (angle + 14);
        }
        hash = hash * 29 + supportingLegIndex;
        return hash;
    }

    @Override
    public Object copy() {
        return new RobotAction(thighRotation, shinRotation, supportingLegIndex);
    }
}
