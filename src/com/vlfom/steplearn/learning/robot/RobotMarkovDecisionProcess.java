package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.learning.general.Action;
import com.vlfom.steplearn.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.learning.general.State;

import java.util.ArrayList;

public class RobotMarkovDecisionProcess extends MarkovDecisionProcess {
    public RobotPicture robotPicture;

    public RobotMarkovDecisionProcess(RobotPicture robotPicture) {
        this.robotPicture = robotPicture;
    }

    @Override
    public Action getAction(State state, Long hash) {
        int cnt = robotPicture.robot.getLegsCount();
        RobotAction action = new RobotAction(cnt);
        action.supportingLegIndex = (int) (hash % 29);
        hash /= 29;
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
            return -1e9;
        }

        return (((RobotState) n).bodyX - ((RobotState) s).bodyX) * 1000;
    }

    public RobotPicture getSpecificRobotPicture(RobotState s) {
        robotPicture.robot.setSupportingLeg(s.supportingLegIndex);
        for (int i = 0; i < s.legsCount; ++i) {
            robotPicture.robot.legs.get(i).tib.angle = s.tibAngle.get(i);
            robotPicture.robot.legs.get(i).foot.angle = s.footAngle.get(i);
            robotPicture.robot.legs.get(i).foot.x = s.footCoords.get(i);
        }
        robotPicture.initialize();
        return robotPicture;
    }

    @Override
    public State applyAction(State s, Action a) {
        RobotState rs = (RobotState) s;
        RobotAction ra = (RobotAction) a;

        robotPicture.robot.setSupportingLeg(ra.supportingLegIndex);
        for (int i = 0; i < rs.legsCount; ++i) {
            robotPicture.robot.legs.get(i).tib.angle = rs.tibAngle.get(i);
            robotPicture.robot.legs.get(i).foot.angle = rs.footAngle.get(i);
            robotPicture.robot.legs.get(i).foot.x = rs.footCoords.get(i);
        }
        robotPicture.initialize();

        try {
            robotPicture.rotateSupportingLeg(ra.tibRotation.get(ra
                    .supportingLegIndex), ra.footRotation.get(ra
                    .supportingLegIndex));
        } catch (RobotFallException e) {
            return null;
        }

        for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i) {
            if (i != ra.supportingLegIndex) {
                try {
                    robotPicture.rotateRegularLeg(i, ra.tibRotation.get(i),
                            ra.footRotation.get(i));
                } catch (HitObjectException e) {
                    return null;
                }
            }
        }

        RobotState ns = new RobotState();
        ns.legsCount = rs.legsCount;
        ns.bodyX = robotPicture.bodyCoords.x;
        ns.tibAngle = new ArrayList<>(rs.legsCount);
        ns.footAngle = new ArrayList<>(rs.legsCount);
        ns.footCoords = new ArrayList<>(rs.legsCount);

        for (int i = 0; i < rs.legsCount; ++i) {
            ns.tibAngle.add(robotPicture.robot.getLeg(i).tib.angle);
            ns.footAngle.add(robotPicture.robot.getLeg(i).foot.angle);
            ns.footCoords.add(robotPicture.robot.getLeg(i).foot.x);
        }

        return ns;
    }

    @Override
    public ArrayList<Action> getActionsList(State s, boolean learning) {
        ArrayList<Action> robotActions = new ArrayList<>();
        RobotAction robotAction = new RobotAction(robotPicture.robot
                .getLegsCount());

        int angle = 1;
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                if (i != j || Math.random() < 0.5) {
                    robotAction.tibRotation.set(0, i * angle);
                    robotAction.footRotation.set(0, -i * angle);
                    robotAction.tibRotation.set(1, j * angle);
                    robotAction.footRotation.set(1, -j * angle);
                    for (int k = 0; k < robotPicture.robot.getLegsCount();
                         ++k) {
                        robotAction.supportingLegIndex = k;
                        if (applyAction(s, robotAction) != null) {
                            robotActions.add((RobotAction) robotAction.copy());
                        }
                    }
                }
            }
        }

        return robotActions;
    }
}
