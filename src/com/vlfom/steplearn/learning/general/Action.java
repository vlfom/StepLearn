package com.vlfom.steplearn.learning.general;

public abstract class Action implements Cloneable {
    public abstract State applyAction(State state);
    public abstract long hash();
}
