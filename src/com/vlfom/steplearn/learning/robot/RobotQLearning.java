package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.learning.general.QLearning;
import com.vlfom.steplearn.learning.general.State;

public class RobotQLearning extends QLearning {
    public RobotQLearning(MarkovDecisionProcess mdp) {
        super(mdp);
    }

    @Override
    public Double reward(State s, State n) {
        return null;
    }
}
