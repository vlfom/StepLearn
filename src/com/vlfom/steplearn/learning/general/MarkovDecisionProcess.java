package com.vlfom.steplearn.learning.general;

import java.util.ArrayList;

public abstract class MarkovDecisionProcess {
    public double learningF = 0.5;
    public double discountF = 0.5;
    public double observationP = 0.2;

    private static int rotationSpeedLeftBorder = -5;
    private static int rotationSpeedRightBorder = 5;

    private static int rotationStep = 1;

    public abstract ArrayList<Action> getActionsList(State s) throws
            CloneNotSupportedException ;

    public abstract boolean compare(State a, State b);

    public abstract Action getAction(State state, Long hash);
}
