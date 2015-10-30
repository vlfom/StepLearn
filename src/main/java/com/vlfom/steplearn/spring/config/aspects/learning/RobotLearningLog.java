package com.vlfom.steplearn.spring.config.aspects.learning;

import com.vlfom.steplearn.core.learning.QLearningRobotModel;
import com.vlfom.steplearn.core.util.Utils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ComponentScan
public class RobotLearningLog {

    @Before("execution(* iterateLearning(int, int))")
    public void logStartLearning(JoinPoint joinPoint) {
        System.out.println("Started learning...");
    }

    @After("execution(* iterateLearning(int, int))")
    public void logEndLearning(JoinPoint joinPoint) {
        System.out.println("Finished learning.\n");
    }

    @Before("execution(* makeStep(..))")
    public void logState(JoinPoint joinPoint) {
        QLearningRobotModel qModel = (QLearningRobotModel) joinPoint.getThis();
        System.out.println(qModel.getCurrentState());
    }

    @Before("execution(* makeStep(..))")
    public void logPossibleActions(JoinPoint joinPoint) {
        QLearningRobotModel qModel = (QLearningRobotModel) joinPoint.getThis();
        System.out.println("Actions list:");
        for (Utils.Pair pair : qModel.getqLearning()
                .getMdp()
                .getActionsList(qModel.getCurrentState())) {
            System.out.println(pair.first);
        }
        System.out.println("");
    }
}
