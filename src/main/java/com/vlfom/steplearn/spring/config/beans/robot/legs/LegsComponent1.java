package com.vlfom.steplearn.spring.config.beans.robot.legs;

import com.vlfom.steplearn.core.robot.legs.Foot;
import com.vlfom.steplearn.core.robot.legs.Leg;
import com.vlfom.steplearn.core.robot.legs.Shin;
import com.vlfom.steplearn.core.robot.legs.Thigh;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LegsComponent1 {

    @Bean
    @Qualifier("legs1-1")
    public Leg legs1_leg1() {
        return new Leg(new Thigh(50, 90, 2), new Shin(50, 180, 2),
                new Foot(5, 200, 90, 1));
    }

    @Bean
    @Qualifier("legs1-2")
    public Leg legs1_leg2() {
        return new Leg(new Thigh(50, 90, 2), new Shin(50, 180, 2),
                new Foot(5, 200, 90, 1));
    }
}
