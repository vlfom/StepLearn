package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.Action;
import com.vlfom.steplearn.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.learning.general.State;
import com.vlfom.steplearn.robot.Robot;

import java.util.ArrayList;

public class RobotMarkovDecisionProcess extends MarkovDecisionProcess {
    private static int rotationSpeedLeftBorder = -5;
    private static int rotationSpeedRightBorder = 5;

    private static int rotationStep = 1;

    @Override
    public boolean compare(State a, State b) {
        return a instanceof RobotState && b instanceof RobotState && Long
                .valueOf(a.hash()).equals(b.hash());
    }

    @Override
    public Action getAction(State state, Long hash) {
        int cnt = ((RobotState) state).robotPicture.robot.getLegsCount();
        RobotAction action = new RobotAction(cnt);
        for (int i = cnt - 1; i >= 0; --i) {
            action.footRotation.set(i, (int) (hash % 29) - 14);
            hash /= 29;
        }
        for (int i = cnt - 1; i >= 0; --i) {
            action.tibRotation.set(i, (int) (hash % 29) - 14);
            hash /= 29;
        }
        return action;
    }

    @Override
    public double reward(State s, State n) {
        if (n == null) {
            return -1e6;
        }

        double x1 = ((RobotState) s).robotPicture.bodyCoords.x;
        double x2 = ((RobotState) n).robotPicture.bodyCoords.x;
        return x2 - x1;
    }

    @Override
    public ArrayList<Action> getActionsList(State s) {
        if (!(s instanceof RobotState)) {
            return null;
        }
        RobotState state = (RobotState) s;
        ArrayList<Action> robotActions = new ArrayList<>();
        RobotAction robotAction = new RobotAction(state.robotPicture.robot
                .getLegsCount());

        int[][] angles = new int[9][2];
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                angles[(i + 1) * 3 + (j + 1)][0] = i;
                angles[(i + 1) * 3 + (j + 1)][1] = j;
            }
        }

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                robotAction.tibRotation.set(0, angles[i][0]);
                robotAction.footRotation.set(0, angles[i][1]);
                robotAction.tibRotation.set(1, angles[j][0]);
                robotAction.footRotation.set(1, angles[j][1]);
                if (robotAction.applyAction(s) != null) {
                    robotActions.add((RobotAction) robotAction.clone());
                }
            }
        }

        return robotActions;
    }
}
