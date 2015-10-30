package com.vlfom.steplearn.spring.config.beans.robot;

import com.vlfom.steplearn.core.robot.Robot;
import com.vlfom.steplearn.core.robot.RobotProjection;
import com.vlfom.steplearn.core.robot.exceptions.HitObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.awt.geom.Line2D;

@Component
@ComponentScan
public class RobotProjectionComponent {

    @Bean
    public Line2D.Double ground() {
        return new Line2D.Double(0, -1, 700, -1);
    }

    @Bean
    @Autowired
    public RobotProjection robotProjection(Robot robot, Line2D.Double ground) {
        RobotProjection robotProjection = new RobotProjection(robot, ground);
        try {
            robotProjection.reInitialize();
        } catch (HitObjectException ignored) {
        }
        return new RobotProjection(robot, ground);
    }
}