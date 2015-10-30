package com.vlfom.steplearn.spring.config.beans.learning;

import com.vlfom.steplearn.core.learning.QLearningRobotModel;
import com.vlfom.steplearn.core.learning.general.QLearning;
import com.vlfom.steplearn.core.robot.RobotProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class QLearningRobotModelComponent {

    @Bean
    @Autowired
    public QLearningRobotModel qLearningRobotModel(
            QLearning qLearning, RobotProjection robotProjection) {
        return new QLearningRobotModel(qLearning, robotProjection);
    }
}