package com.vlfom.steplearn.core.learning.robot;

import com.vlfom.steplearn.core.learning.general.Action;
import com.vlfom.steplearn.core.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.core.learning.general.State;
import com.vlfom.steplearn.core.robot.RobotProjection;
import com.vlfom.steplearn.core.robot.exceptions.HitObjectException;
import com.vlfom.steplearn.core.robot.exceptions.RobotFallException;
import com.vlfom.steplearn.core.util.Utils;

import java.util.ArrayList;

public class RobotMarkovDecisionProcess extends MarkovDecisionProcess {
    private RobotProjection robotProjection;

    public RobotMarkovDecisionProcess(RobotProjection robotProjection) {
        this.robotProjection = robotProjection;
    }

    public RobotProjection getRobotProjection() {
        return robotProjection;
    }

    public void setRobotProjection(RobotProjection robotProjection) {
        this.robotProjection = robotProjection;
    }

    @Override
    public Action getAction(State state, Long hash) {
        int legsCount = robotProjection.getRobot().getLegsCount();
        RobotAction action = new RobotAction(legsCount);

        action.setSupportingLegIndex((int) (hash % 29));
        hash /= 29;
        for (int i = legsCount - 1; i >= 0; --i) {
            action.setShinRotation(i, (int) (hash % 29) - 14);
            hash /= 29;
        }
        for (int i = legsCount - 1; i >= 0; --i) {
            action.setThighRotation(i, (int) (hash % 29) - 14);
            hash /= 29;
        }
        return action;
    }

    @Override
    public double reward(State s, Action a, State n) {
        if (a == null || n == null) {
            return -1e9;
        }

        RobotState rs = (RobotState) s;
        RobotAction ra = (RobotAction) a;

        robotProjection.getRobot().setSupportingLeg(ra.getSupportingLegIndex());
        for (int i = 0; i < rs.getLegsCount(); ++i) {
            robotProjection.getRobot().legs.get(
                    i).thigh.angle = rs.getThighAngle(i);
            robotProjection.getRobot().legs.get(i).shin.angle = rs.getShinAngle(
                    i);
            robotProjection.getRobot().legs.get(i).foot.angle = rs.getShinAngle(
                    i) - rs.getThighAngle(i);
            robotProjection.getRobot().legs.get(i).foot.x = 0;
        }
        try {
            robotProjection.reInitialize();
        } catch (HitObjectException e) {
            return -1e9;
        }

        double bodyX = robotProjection.getRobot().body.x;

        try {
            robotProjection.rotateSupportingLeg(
                    ra.getThighRotation(ra.getSupportingLegIndex()),
                    ra.getShinRotation(ra.getSupportingLegIndex()),
                    ra.getShinRotation(
                            ra.getSupportingLegIndex()) - ra.getThighRotation(
                            ra.getSupportingLegIndex()));
            for (int i = 0; i < robotProjection.getRobot()
                    .getLegsCount(); ++i) {
                if (i != ra.getSupportingLegIndex()) {
                    robotProjection.rotateRegularLeg(i, ra.getThighRotation(i),
                            ra.getShinRotation(i),
                            ra.getShinRotation(i) - ra.getThighRotation(i));
                }
            }
        } catch (Exception e) {
            return -1e9;
        }

        return (robotProjection.getRobot().body.x - bodyX) * 10000;
    }

    @Override
    public State applyAction(State s, Action a) {
        if (s == null || a == null) {
            return null;
        }

        RobotState rs = (RobotState) s;
        RobotAction ra = (RobotAction) a;

        for (int i = 0; i < rs.getLegsCount(); ++i) {
            if (rs.getThighAngle(i) + ra.getThighRotation(i) < 60 ||
                    rs.getThighAngle(i) + ra.getThighRotation(i) > 120 ||
                    rs.getShinAngle(i) + ra.getShinRotation(i) < 10 ||
                    rs.getShinAngle(i) + ra.getShinRotation(i) > 180 ||
                    (rs.getSupportingLegIndex() != ra.getSupportingLegIndex()
                            && Math
                            .abs(rs.getThighAngle(
                                    rs.getSupportingLegIndex()) - rs
                                    .getThighAngle(ra.getSupportingLegIndex()
                                    )) < 10) ||
                    (rs.getSupportingLegIndex() != ra.getSupportingLegIndex()
                            && Math
                            .abs(rs.getThighAngle(
                                    rs.getSupportingLegIndex()) - rs
                                    .getThighAngle(ra.getSupportingLegIndex()
                                    )) > 90)) {
//                System.out.println(
//                        (rs.thighAngle.get(i) + ra.getThighRotation(i) < 30) +
//                                " " +
//                                (rs.thighAngle.get(i) + ra.thighRotation.get
//                                        (i) > 160) +
//                                " " +
//                                (rs.shinAngle.get(i) + ra.getShinRotation(i)
//                                        < 100) +
//                                " " +
//                                (rs.getSupportingLegIndex() != ra
// .getSupportingLegIndex() && Math
//                                        .abs(rs.thighAngle.get(rs
// .getSupportingLegIndex()) -
//                                                rs.thighAngle.get(ra
// .getSupportingLegIndex())) <
//                                        30) +
//                                " " +
//                                (rs.shinAngle.get(ra.getSupportingLegIndex
// ()) !=
//                                        180) +
//                                " " +
//                                (rs.shinAngle.get(ra.getSupportingLegIndex
// ()) +
//                                        ra.getShinRotation(ra
//                                                .getSupportingLegIndex())
// != 180)
//                );
                return null;
            }
        }

        for (int i = 0; i < rs.getLegsCount(); ++i) {
            robotProjection.getRobot().legs.get(
                    i).thigh.angle = rs.getThighAngle(i);
            robotProjection.getRobot().legs.get(i).shin.angle = rs.getShinAngle(
                    i);
            robotProjection.getRobot().legs.get(i).foot.angle = rs.getShinAngle(
                    i) - rs.getThighAngle(i);
            robotProjection.getRobot().legs.get(i).foot.x = 350;
        }
        robotProjection.getRobot().setSupportingLeg(ra.getSupportingLegIndex());
        try {
            robotProjection.reInitialize();
        } catch (HitObjectException e) {
            return null;
        }

        try {
            robotProjection.rotateSupportingLeg(
                    ra.getThighRotation(ra.getSupportingLegIndex()),
                    ra.getShinRotation(ra.getSupportingLegIndex()),
                    ra.getShinRotation(
                            ra.getSupportingLegIndex()) - ra.getThighRotation(
                            ra.getSupportingLegIndex()));
        } catch (RobotFallException e) {
            return null;
        }

        for (int i = 0; i < robotProjection.getRobot().getLegsCount(); ++i) {
            if (i != ra.getSupportingLegIndex()) {
                try {
                    robotProjection.rotateRegularLeg(i, ra.getThighRotation(i),
                            ra.getShinRotation(i),
                            ra.getShinRotation(i) - ra.getThighRotation(i));
                } catch (HitObjectException e) {
                    return null;
                }
            }
        }

        try {
            robotProjection.reInitialize();
        } catch (HitObjectException e) {
            return null;
        }

        RobotState ns = new RobotState(rs.getLegsCount());
        ns.setSupportingLegIndex(ra.getSupportingLegIndex());
        ns.setLegsCount(rs.getLegsCount());

        for (int i = 0; i < rs.getLegsCount(); ++i) {
            ns.setThighAngle(i,
                    robotProjection.getRobot().getLeg(i).thigh.angle);

            ns.setShinAngle(i, robotProjection.getRobot().getLeg(i).shin.angle);
        }

        return ns;
    }

    @Override
    public ArrayList<Utils.Pair> getActionsList(State s) {
        ArrayList<Utils.Pair> robotActions = new ArrayList<>();
        RobotState rs = (RobotState) s;
        RobotAction robotAction = new RobotAction(rs.getLegsCount());

        int angle = 2;
        for (int i0 = -1; i0 <= 1; i0 += 2) {
            for (int j0 = -1; j0 <= 1; j0 += 1) {
                for (int i1 = -1; i1 <= 1; i1 += 2) {
                    for (int j1 = -1; j1 <= 1; j1 += 1) {
                        if (i0 != i1) {
                            robotAction.setThighRotation(0, i0 * angle);
                            robotAction.setShinRotation(0, j0 * angle);
                            robotAction.setThighRotation(1, i1 * angle);
                            robotAction.setShinRotation(1, j1 * angle);
                            for (int k = 0; k < robotProjection.getRobot()
                                    .getLegsCount(); ++k) {
//                                if (!(
//                                        (k == 0 && i1 > 0 && j1 < 0) ||
//                                        (k == 1 && i0 > 0 && j0 < 0)
//                                )) {
                                robotAction.setSupportingLegIndex(k);
                                State n = applyAction(rs, robotAction);
                                if (n != null) {
                                    robotActions.add(
                                            new Utils.Pair(robotAction.copy(),
                                                    n));
                                }
                            }
                        }
                    }
                }
            }
        }

        return robotActions;
    }
}
