package com.vlfom.steplearn.learning.general;

public abstract class State implements Cloneable {
    public abstract boolean compare(State o);
    public abstract Integer hash();
}
