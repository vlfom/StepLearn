package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.Action;

import java.util.ArrayList;
import java.util.Collections;

public class RobotAction extends Action {
    public ArrayList<Integer> tibRotation;
    public ArrayList<Integer> footRotation;
    public int supportingLegIndex;

    public RobotAction(int legsCount) {
        tibRotation = new ArrayList<>(Collections.nCopies(legsCount, 0));
        footRotation = new ArrayList<>(Collections.nCopies(legsCount, 0));
    }

    @Override
    public String toString() {
        String string = "Action: ";
        for (int i = 0; i < tibRotation.size(); ++i) {
            string += "{" + tibRotation.get(i) + ", " +
                    footRotation.get(i) + "} ";
        }
        string += "{" + supportingLegIndex + "}";
        return string;
    }

    @Override
    public long hash() {
        long hash = 0;
        for (Integer angle : tibRotation) {
            hash = hash * 29 + (angle + 14);
        }
        for (Integer angle : footRotation) {
            hash = hash * 29 + (angle + 14);
        }
        hash = hash * 29 + supportingLegIndex;
        return hash;
    }

    @Override
    public Object copy() {
        RobotAction copied = new RobotAction(tibRotation.size());
        copied.tibRotation = (ArrayList<Integer>) tibRotation.clone();
        copied.footRotation = (ArrayList<Integer>) footRotation.clone();
        copied.supportingLegIndex = supportingLegIndex;
        return copied;
    }
}
