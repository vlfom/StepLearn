package com.vlfom.steplearn.spring.config.body;

import com.vlfom.steplearn.core.robot.body.Body;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Vladimir Fomenko
 */

@Component
public class BodyConfig1 {

    @Bean
    public Body body() {
        return new Body(50, 100, 5);
    }
}
