package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.Action;
import com.vlfom.steplearn.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.learning.general.State;

import java.util.ArrayList;

public class RobotMarkovDecisionProcess extends MarkovDecisionProcess {
    private static int rotationSpeedLeftBorder = -5;
    private static int rotationSpeedRightBorder = 5;

    private static int rotationStep = 1;

    @Override
    public boolean compare(State a, State b) {
        return a instanceof RobotState && b instanceof RobotState && Long
                .valueOf(a.hash()).equals
                (b.hash());
    }

    @Override
    public Action getAction(State state, Long hash) {
        int cnt = ((RobotState)state).robotPicture
                .robot.getLegsCount();
        RobotAction action = new RobotAction(cnt);
        for(int i = cnt-1; i >= 0 ; --i) {
            action.footRotation.set(i, (int) (hash % 181));
            hash /= 181;
        }
        for(int i = cnt-1; i >= 0 ; --i) {
            action.tibRotation.set(i, (int) (hash % 181));
            hash /= 181;
        }
        return action;
    }

    @Override
    public ArrayList<Action> getActionsList(State s) throws
            CloneNotSupportedException {
        if(!(s instanceof RobotState))
            return null;
        RobotState state = (RobotState)s;
        ArrayList<Action> robotActions = new ArrayList<>(4);
        RobotAction robotAction = new RobotAction(state.robotPicture.robot
                .getLegsCount());

        for(int i = 0 ; i < state.robotPicture.robot.getLegsCount(); ++i) {
            robotAction.tibRotation.set(i, rotationStep);
            robotAction.footRotation.set(i, rotationStep);
        }
        robotActions.set(0, (RobotAction) robotAction.clone());

        for(int i = 0 ; i < state.robotPicture.robot.getLegsCount(); ++i) {
            robotAction.tibRotation.set(i, rotationStep);
            robotAction.footRotation.set(i, -rotationStep);
        }
        robotActions.set(1, (RobotAction) robotAction.clone());

        for(int i = 0 ; i < state.robotPicture.robot.getLegsCount(); ++i) {
            robotAction.tibRotation.set(i, -rotationStep);
            robotAction.footRotation.set(i, rotationStep);
        }
        robotActions.set(2, (RobotAction) robotAction.clone());

        for(int i = 0 ; i < state.robotPicture.robot.getLegsCount(); ++i) {
            robotAction.tibRotation.set(i, -rotationStep);
            robotAction.footRotation.set(i, -rotationStep);
        }
        robotActions.set(3, (RobotAction) robotAction.clone());

        return robotActions;
    }
}
