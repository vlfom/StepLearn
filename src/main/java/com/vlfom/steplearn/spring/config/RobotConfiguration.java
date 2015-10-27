package com.vlfom.steplearn.spring.config;

import com.vlfom.steplearn.core.robot.Robot;
import com.vlfom.steplearn.core.robot.body.Body;
import com.vlfom.steplearn.core.robot.legs.Leg;
import com.vlfom.steplearn.spring.config.body.BodyConfig1;
import com.vlfom.steplearn.spring.config.legs.LegsConfig1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author Vladimir Fomenko
 */

@Configuration
@ComponentScan(
        useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType
                .ASSIGNABLE_TYPE,
                value = BodyConfig1.class), @ComponentScan.Filter(type =
                FilterType.ASSIGNABLE_TYPE,
                value = LegsConfig1.class)})
public class RobotConfiguration {

    @Bean
    @Autowired
    public Robot robot(Body body, Leg leg1, Leg leg2) {
        Robot robot = new Robot(body);
        robot.addLeg(leg1);
        robot.addLeg(leg2);
        robot.setSupportingLeg(0);
        return robot;
    }
}
