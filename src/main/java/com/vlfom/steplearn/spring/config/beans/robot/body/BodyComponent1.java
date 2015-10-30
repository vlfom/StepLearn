package com.vlfom.steplearn.spring.config.beans.robot.body;

import com.vlfom.steplearn.core.robot.body.Body;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BodyComponent1 {

    @Bean
    @Qualifier("body1")
    public Body body1() {
        return new Body(50, 100, 5);
    }
}
