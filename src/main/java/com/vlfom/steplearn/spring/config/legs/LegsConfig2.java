package com.vlfom.steplearn.spring.config.legs;

import com.vlfom.steplearn.core.robot.legs.Foot;
import com.vlfom.steplearn.core.robot.legs.Leg;
import com.vlfom.steplearn.core.robot.legs.Shin;
import com.vlfom.steplearn.core.robot.legs.Thigh;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Vladimir Fomenko
 */

@Component
public class LegsConfig2 {

    @Bean
    public Leg leg1() {
        return new Leg(new Thigh(20, 90, 2), new Shin(80, 180, 2), new
                Foot(5, 200, 90, 1));
    }

    @Bean
    public Leg leg2() {
        return new Leg(new Thigh(80, 90, 2), new Shin(20, 180, 2), new
                Foot(5, 200, 90, 1));
    }
}
