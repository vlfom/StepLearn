package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.learning.general.Action;
import com.vlfom.steplearn.learning.general.State;
import com.vlfom.steplearn.robot.Robot;

import java.util.ArrayList;

public class RobotAction extends Action {
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
            }
        return newState;
    }
}
