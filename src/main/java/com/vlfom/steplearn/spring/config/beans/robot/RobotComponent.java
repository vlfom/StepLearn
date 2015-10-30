package com.vlfom.steplearn.spring.config.beans.robot;

import com.vlfom.steplearn.core.robot.Robot;
import com.vlfom.steplearn.core.robot.body.Body;
import com.vlfom.steplearn.core.robot.legs.Leg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class RobotComponent {

    @Bean
    @Autowired
    public Robot robot(
            @Qualifier("body1") Body body,
            @Qualifier("legs1-1") Leg leg1,
            @Qualifier("legs2-2") Leg leg2) {
        Robot robot = new Robot(body);
        robot.addLeg(leg1);
        robot.addLeg(leg2);
        robot.setSupportingLeg(0);
        return robot;
    }
}
