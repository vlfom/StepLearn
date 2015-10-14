package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.learning.general.Action;
import com.vlfom.steplearn.learning.general.State;

import java.util.ArrayList;
import java.util.Collections;

public class RobotAction extends Action implements Cloneable {
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
        for(int i = 0 ; i < tibRotation.size(); ++i)
            string += "{" + tibRotation.get(i) + ", " +
                    footRotation.get(i) + "} ";
        string += "{" + supportingLegIndex + "}";
        return string;
    }

    @Override
    public State applyAction(State s) {
        if (!(s instanceof RobotState)) {
            return null;
        }
        RobotState newState = (RobotState) ((RobotState) s).clone();
        RobotPicture robotPicture = newState.robotPicture;
        robotPicture.robot.setSupportingLeg(supportingLegIndex);

        try {
            robotPicture.rotateSupportingLeg(tibRotation.get
                    (supportingLegIndex), footRotation.get(supportingLegIndex));
        } catch (RobotFallException e) {
            return null;
        }

        for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i)
            if (i != supportingLegIndex) {
                try {
                    robotPicture.rotateRegularLeg(i, tibRotation.get(i), footRotation.get(i));
                } catch (HitObjectException e) {
                    return null;
                }
            }

        return newState;
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
        hash = hash*29 + supportingLegIndex;
        return hash;
    }

    @Override
    public Object clone() {
        RobotAction cloned = new RobotAction(tibRotation.size());
        cloned.tibRotation = (ArrayList<Integer>) tibRotation.clone();
        cloned.footRotation = (ArrayList<Integer>) footRotation.clone();
        cloned.supportingLegIndex = supportingLegIndex;
        return cloned;
    }
}
