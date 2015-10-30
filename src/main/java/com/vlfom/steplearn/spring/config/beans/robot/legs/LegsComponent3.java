package com.vlfom.steplearn.spring.config.beans.robot.legs;

import com.vlfom.steplearn.core.robot.legs.Foot;
import com.vlfom.steplearn.core.robot.legs.Leg;
import com.vlfom.steplearn.core.robot.legs.Shin;
import com.vlfom.steplearn.core.robot.legs.Thigh;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LegsComponent3 {

    @Bean
    @Qualifier("legs3-1")
    public Leg legs3_leg1() {
        return new Leg(new Thigh(30, 90, 2), new Shin(70, 180, 2),
                new Foot(5, 200, 90, 1));
    }

    @Bean
    @Qualifier("legs3-2")
    public Leg legs3_leg2() {
        return new Leg(new Thigh(60, 90, 2), new Shin(40, 180, 2),
                new Foot(5, 200, 90, 1));
    }
}
