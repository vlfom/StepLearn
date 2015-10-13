package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.State;
import com.vlfom.steplearn.robot.Robot;

import java.util.ArrayList;

public class RobotActionsList {
    private static int rotationSpeedLeftBorder = -5;
    private static int rotationSpeedRightBorder = 5;

    private static int rotationStep = 1;

    public static ArrayList<RobotAction> getActionsList(State s) throws
            CloneNotSupportedException {
        if(!(s instanceof RobotState))
            return null;
        RobotState state = (RobotState)s;
        ArrayList<RobotAction> robotActions = new ArrayList<>(4);
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
