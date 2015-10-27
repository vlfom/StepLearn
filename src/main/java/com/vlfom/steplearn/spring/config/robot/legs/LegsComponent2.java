package com.vlfom.steplearn.spring.config.robot.legs;

import com.vlfom.steplearn.core.robot.legs.Foot;
import com.vlfom.steplearn.core.robot.legs.Leg;
import com.vlfom.steplearn.core.robot.legs.Shin;
import com.vlfom.steplearn.core.robot.legs.Thigh;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LegsComponent2 {

    @Bean
    @Qualifier("legs2-1")
    public Leg legs2_leg1() {
        return new Leg(new Thigh(20, 90, 2), new Shin(80, 180, 2),
                new Foot(5, 200, 90, 1));
    }

    @Bean
    @Qualifier("legs2-2")
    public Leg legs2_leg2() {
        return new Leg(new Thigh(80, 90, 2), new Shin(20, 180, 2),
                new Foot(5, 200, 90, 1));
    }
}
