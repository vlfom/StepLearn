package com.vlfom.steplearn.core.learning.general;

import com.vlfom.steplearn.core.util.Utils;

import java.util.ArrayList;

public abstract class MarkovDecisionProcess {
    public double learningF = 0.9;
    public double discountF = 0.8;
    public double observationP = 0.5;

    public abstract ArrayList<Utils.Pair> getActionsList(State s);

    public abstract Action getAction(State state, Long hash);

    public abstract double reward(State s, Action a, State n);

    public abstract State applyAction(State state, Action action);
}
