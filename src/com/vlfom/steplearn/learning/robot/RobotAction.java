package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.learning.general.Action;
import com.vlfom.steplearn.learning.general.State;
import com.vlfom.steplearn.robot.Robot;

import java.util.ArrayList;

public class RobotAction extends Action implements Cloneable {
    public ArrayList<Integer> tibRotation;
    public ArrayList<Integer> footRotation;

    public RobotAction(int legsCount) {
        tibRotation = new ArrayList<>(legsCount);
        footRotation = new ArrayList<>(legsCount);
    }

    @Override
    public State applyAction(State s) {
        if(!(s instanceof RobotState))
            return null;
        State newState;
        try {
            newState = (State) ((RobotState) s).clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
        RobotPicture robotPicture = ((RobotState)newState).robotPicture;
        for(int i = 0 ; i < robotPicture.robot.getLegsCount(); ++i)
            try {
                robotPicture.rotateLeg(i, tibRotation.get(i), footRotation
                        .get(i));
            } catch (HitObjectException ignored) {
                return null;
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
            hash = hash * 181 + angle;
        }
        for (Integer angle : footRotation) {
            hash = hash * 181 + angle;
        }
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RobotAction cloned = (RobotAction)super.clone();
        cloned.tibRotation = (ArrayList<Integer>) tibRotation.clone();
        cloned.footRotation = (ArrayList<Integer>) footRotation.clone();
        return cloned;
    }
}
