package com.vlfom.steplearn.learning.general;

import java.util.ArrayList;

public abstract class MarkovDecisionProcess {
    public double learningF = 0.9;
    public double discountF = 0.7;
    public double observationP = 0.5;

    private static int rotationSpeedLeftBorder = -5;
    private static int rotationSpeedRightBorder = 5;

    private static int rotationStep = 1;

    public abstract ArrayList<Action> getActionsList(State s);

    public abstract boolean compare(State a, State b);

    public abstract Action getAction(State state, Long hash);

    public abstract double reward(State s, State n);
}
