package com.vlfom.steplearn.learning.general;

import java.util.TreeMap;

public class QLearning {
    private MarkovDecisionProcess mdp;
    TreeMap q = new TreeMap<StateTransition, Double>();

    public QLearning(MarkovDecisionProcess mdp) {
        this.mdp = mdp;
    }
}
