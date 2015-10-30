package com.vlfom.steplearn.spring.config.beans.learning;

import com.vlfom.steplearn.core.learning.robot.RobotState;
import com.vlfom.steplearn.core.robot.Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class RobotStateComponent {

    @Bean
    @Autowired
    public RobotState robotState(Robot robot) {
        return new RobotState(robot);
    }
}
