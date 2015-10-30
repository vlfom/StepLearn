package com.vlfom.steplearn.core.learning;

import com.vlfom.steplearn.core.learning.exceptions.LearningErrorException;

public abstract class AbstractLearningModel {
    public void iterateLearning(int iterationDepth) {
        int d = iterationDepth;
        prepareToLearn();
        while (d-- > 0) {
            try {
                this.stepLearn();
            } catch (LearningErrorException e) {
                return;
            }
        }
    }

    public void iterateLearning(int iterationsCount, int iterationDepth) {
        int c = iterationsCount;
        while (c-- > 0) {
            this.iterateLearning(iterationDepth);
        }
    }

    public abstract void calibrateLearning(int iterationsCount, int
            iterationDepth);

    protected abstract void prepareToLearn();

    protected abstract void stepLearn() throws LearningErrorException;

    public abstract void makeStep();
}
