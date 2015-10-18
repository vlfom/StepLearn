package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.learning.general.Action;
import com.vlfom.steplearn.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.learning.general.State;
import com.vlfom.steplearn.util.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class RobotMarkovDecisionProcess extends MarkovDecisionProcess {
    public RobotPicture robotPicture;

    public RobotMarkovDecisionProcess(RobotPicture robotPicture) {
        this.robotPicture = robotPicture;
    }

    @Override
    public Action getAction(State state, Long hash) {
        int legsCount = robotPicture.robot.getLegsCount();
        RobotAction action = new RobotAction(legsCount);

        action.supportingLegIndex = (int) (hash % 29);
        hash /= 29;
        for (int i = legsCount - 1; i >= 0; --i) {
            action.shinRotation.set(i, (int) (hash % 29) - 14);
            hash /= 29;
        }
        for (int i = legsCount - 1; i >= 0; --i) {
            action.thighRotation.set(i, (int) (hash % 29) - 14);
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
            robotPicture.robot.legs.get(i).thigh.angle = s.thighAngle.get(i);
            robotPicture.robot.legs.get(i).shin.angle = s.shinAngle.get(i);
            robotPicture.robot.legs.get(i).foot.angle = s.shinAngle.get(i) -
                    s.thighAngle.get(i);
            robotPicture.robot.legs.get(i).foot.x = s.footCoords.get(i);
        }
        robotPicture.initialize();
        return robotPicture;
    }

    @Override
    public State applyAction(State s, Action a) {
        RobotState rs = (RobotState) s;
        RobotAction ra = (RobotAction) a;

        for(int i = 0 ; i < rs.legsCount; ++i)
            if(rs.thighAngle.get(i) + ra.thighRotation.get(i) < 70 ||
                    rs.thighAngle.get(i) + ra.thighRotation.get(i) > 130 ||
                    rs.shinAngle.get(i) + ra.shinRotation.get(i) < 150 ||
                    (rs.supportingLegIndex != ra.supportingLegIndex && Math
                            .abs(rs.thighAngle.get(rs.supportingLegIndex)-rs
                                    .thighAngle.get(ra.supportingLegIndex)) <
                            30 ) ||
                    rs.shinAngle.get(ra.supportingLegIndex) + ra.shinRotation
                            .get(ra.supportingLegIndex) != 180)
                return null;

        robotPicture.robot.setSupportingLeg(ra.supportingLegIndex);
        for (int i = 0; i < rs.legsCount; ++i) {
            robotPicture.robot.legs.get(i).thigh.angle = rs.thighAngle.get(i);
            robotPicture.robot.legs.get(i).shin.angle = rs.shinAngle.get(i);
            robotPicture.robot.legs.get(i).foot.angle = rs.shinAngle.get(i) -
                    rs.thighAngle.get(i);
            robotPicture.robot.legs.get(i).foot.x = rs.footCoords.get(i);
        }
        robotPicture.initialize();

        try {
            robotPicture.rotateSupportingLeg(ra.thighRotation.get(ra
                    .supportingLegIndex), ra.shinRotation.get(ra
                    .supportingLegIndex), ra.shinRotation.get(ra
                    .supportingLegIndex) - ra.thighRotation.get(ra
                    .supportingLegIndex));
        } catch (RobotFallException e) {
            if(ra.shinRotation.get(ra.supportingLegIndex) == 0)
            return null;
        }

        for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i) {
            if (i != ra.supportingLegIndex) {
                try {
                    robotPicture.rotateRegularLeg(i, ra.thighRotation.get(i),
                            ra.shinRotation.get(i), ra.shinRotation.get(i) -
                                    ra.thighRotation.get(i));
                } catch (HitObjectException e) {
                    return null;
                }
            }
        }

        RobotState ns = new RobotState(rs.legsCount);
        ns.supportingLegIndex = ra.supportingLegIndex;
        ns.legsCount = rs.legsCount;
        ns.bodyX = robotPicture.bodyCoords.x;

        for (int i = 0; i < rs.legsCount; ++i) {
            ns.thighAngle.set(i, robotPicture.robot.getLeg(i).thigh.angle);
            ns.shinAngle.set(i, robotPicture.robot.getLeg(i).shin.angle);
            ns.footCoords.set(i, robotPicture.robot.getLeg(i).foot.x);
        }

        return ns;
    }

    @Override
    public ArrayList<Utils.Pair> getActionsList(State s, boolean learning) {
        ArrayList<Utils.Pair> robotActions = new ArrayList<>();
        RobotAction robotAction = new RobotAction();
        RobotState rs = (RobotState) s;

        robotAction.thighRotation = new ArrayList<>(Collections.nCopies
                (robotPicture.robot.getLegsCount(), 0));
        robotAction.shinRotation = new ArrayList<>(Collections.nCopies
                (robotPicture.robot.getLegsCount(), 0));

        int angle = 3;
        for (int i0 = -1; i0 <= 1; i0 += 2) {
            for (int j0 = -1; j0 <= 1; j0 += 1) {
                for (int i1 = -1; i1 <= 1; i1 += 2) {
                    for (int j1 = -1; j1 <= 1; j1 += 1)
                        if (i0 != i1) {
                            robotAction.thighRotation.set(0, i0 * angle);
                            robotAction.shinRotation.set(0, j0 * angle);
                            robotAction.thighRotation.set(1, i1 * angle);
                            robotAction.shinRotation.set(1, j1 * angle);
                            for (int k = 0; k < robotPicture.robot.getLegsCount(); ++k)
                                if (!(
                                        (k == 0 && i1 > 0 && j1 < 0) ||
                                        (k == 1 && i0 > 0 && j0 < 0) )) {
                                    robotAction.supportingLegIndex = k;
                                    State n = applyAction(rs, robotAction);
                                    if (n != null) {
                                        robotActions.add(new Utils.Pair
                                                (robotAction.copy(), n));
                                    }
                                }
                        }
                }
            }
        }

        return robotActions;
    }
}
