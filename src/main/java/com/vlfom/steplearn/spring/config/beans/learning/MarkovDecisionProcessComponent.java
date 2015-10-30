package com.vlfom.steplearn.spring.config.beans.learning;

import com.vlfom.steplearn.core.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.core.learning.robot.RobotMarkovDecisionProcess;
import com.vlfom.steplearn.core.robot.RobotProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class MarkovDecisionProcessComponent {

    @Bean
    @Autowired
    public MarkovDecisionProcess markovDecisionProcess(
            RobotProjection robotProjection) {
        return new RobotMarkovDecisionProcess(
                (RobotProjection) robotProjection.copy());
    }
}
