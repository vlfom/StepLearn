package com.vlfom.steplearn.core.learning;

import com.vlfom.steplearn.core.learning.exceptions.LearningErrorException;
import com.vlfom.steplearn.core.learning.general.QLearning;
import com.vlfom.steplearn.core.learning.robot.RobotAction;
import com.vlfom.steplearn.core.learning.robot.RobotState;
import com.vlfom.steplearn.core.robot.RobotProjection;
import com.vlfom.steplearn.core.robot.exceptions.HitObjectException;
import com.vlfom.steplearn.core.robot.exceptions.RobotFallException;

public class QLearningRobotModel extends AbstractLearningModel {
    private QLearning qLearning;
    private RobotState initialState;
    private RobotState currentState;
    private RobotProjection initialProjection;
    private RobotProjection currentProjection;

    public QLearningRobotModel() {
    }

    public QLearningRobotModel(QLearning qLearning, RobotProjection
            robotProjection) {
        this.qLearning = qLearning;
        this.initialProjection = robotProjection;
        this.initialState = new RobotState(robotProjection.getRobot());
        this.resetModel();
    }

    public RobotState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(RobotState currentState) {
        this.currentState = currentState;
    }

    public QLearning getqLearning() {

        return qLearning;
    }

    public void setqLearning(QLearning qLearning) {
        this.qLearning = qLearning;
    }

    public void setInitialState(RobotState initialState) {
        this.initialState = initialState;
    }

    public void setInitialProjection(RobotProjection initialProjection) {
        this.initialProjection = initialProjection;
    }

    public RobotProjection getCurrentProjection() {
        return currentProjection;
    }

    @Override
    public void calibrateLearning(int iterationsCount, int iterationDepth) {
        double remP = qLearning.getMdp().observationP;
        qLearning.getMdp().observationP = 0;
        iterateLearning(iterationsCount, iterationDepth);
        qLearning.getMdp().observationP = remP;
    }

    @Override
    protected void prepareToLearn() {
        this.currentState = (RobotState) initialState.copy();
    }

    @Override
    protected void stepLearn() throws LearningErrorException {
        currentState = (RobotState) qLearning.iterateLearning(currentState);
        if (currentState == null) {
            throw new LearningErrorException("");
        }
    }

    public void resetModel() {
        this.currentProjection = (RobotProjection) this.initialProjection
                .copy();
        this.currentState = (RobotState) this.initialState.copy();
    }

    @Override
    public void makeStep() {
        RobotAction ra = (RobotAction) qLearning.getArgMax(currentState);

        if (ra == null) {
            resetModel();
        } else {
            currentProjection.getRobot()
                    .setSupportingLeg(ra.getSupportingLegIndex());
            try {
                currentProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            currentProjection.getRobot()
                    .setSupportingLeg((ra.getSupportingLegIndex() + 1) % 2);
            try {
                currentProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            currentProjection.getRobot()
                    .setSupportingLeg(ra.getSupportingLegIndex());
            try {
                currentProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            try {
                currentProjection.rotateSupportingLeg(
                        ra.getThighRotation(ra.getSupportingLegIndex()),
                        ra.getShinRotation(ra.getSupportingLegIndex()),
                        ra.getShinRotation(
                                ra.getSupportingLegIndex()) - ra
                                .getThighRotation(ra.getSupportingLegIndex()));
            } catch (RobotFallException ignored) {
            }

            for (int i = 0; i < currentProjection.getRobot()
                    .getLegsCount(); ++i) {
                if (i != ra.getSupportingLegIndex()) {
                    try {
                        currentProjection.rotateRegularLeg(i,
                                ra.getThighRotation(i), ra.getShinRotation(i),
                                ra.getShinRotation(i) - ra.getThighRotation(i));
                    } catch (HitObjectException ignored) {
                    }
                }
            }

            currentState.setSupportingLegIndex(ra.getSupportingLegIndex());
            for (int i = 0; i < currentState.getLegsCount(); ++i) {
                currentState.setThighAngle(i,
                        currentProjection.getRobot().getLeg(i).thigh.angle);
                currentState.setShinAngle(i,
                        currentProjection.getRobot().getLeg(i).shin.angle);
            }
        }
    }
}
