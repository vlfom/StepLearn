package com.vlfom.steplearn.spring.config.beans.robot.body;

import com.vlfom.steplearn.core.robot.body.Body;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BodyComponent2 {

    @Bean
    @Qualifier("body2")
    public Body body2() {
        return new Body(100, 100, 5);
    }
}
