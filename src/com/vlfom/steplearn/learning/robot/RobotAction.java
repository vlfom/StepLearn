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

    public RobotAction(int legsCount) {
        tibRotation = new ArrayList<>(Collections.nCopies(legsCount, 0));
        footRotation = new ArrayList<>(Collections.nCopies(legsCount, 0));
    }

    @Override
    public String toString() {
        return tibRotation + " " + footRotation;
    }

    @Override
    public State applyAction(State s) {
        if (!(s instanceof RobotState)) {
            return null;
        }
        State newState;
        newState = (State) ((RobotState) s).clone();
        RobotPicture robotPicture = ((RobotState) newState).robotPicture;
        for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i) {
            try {
                robotPicture.rotateLeg(i, tibRotation.get(i), footRotation
                        .get(i));
            } catch (HitObjectException e) {
                return null;
            }
        }

        try {
            robotPicture.updateStateInfo();
        } catch (RobotFallException e) {
            return null;
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
        return hash;
    }

    @Override
    public Object clone() {
        RobotAction cloned = new RobotAction(tibRotation.size());
        cloned.tibRotation = (ArrayList<Integer>) tibRotation.clone();
        cloned.footRotation = (ArrayList<Integer>) footRotation.clone();
        return cloned;
    }
}
