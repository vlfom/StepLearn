package com.vlfom.steplearn.learning.general;

import java.util.ArrayList;

public abstract class MarkovDecisionProcess {
    public double learningF = 0.9;
    public double discountF = 0.7;
    public double observationP = 0.5;

    public abstract ArrayList<Action> getActionsList(State s, boolean learning);

    public abstract Action getAction(State state, Long hash);

    public abstract double reward(State s, State n);

    public abstract State applyAction(State state, Action action);
}
