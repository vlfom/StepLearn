package com.vlfom.steplearn.learning.general;

public class StateTransition {
    public Integer stateHash;
    public Action action;
    StateTransition(Integer stateHash, Action action) {
        this.stateHash = stateHash;
        this.action = action;
    }
}
