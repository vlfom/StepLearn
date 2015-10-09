package com.vlfom.steplearn.draw;

import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.robot.Leg;
import com.vlfom.steplearn.robot.Robot;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

class RobotPicture {
    public Robot robot;
    private Point2D.Double bodyCoords;

    public void recalc() throws RobotFallException {
        robot.recalcSupportingLeg();
    }

    public Point2D.Double getBodyCoords() {
        Leg leg = robot.getSupportingLeg();
        bodyCoords.x = leg.foot.x + leg.tib.length * Math.cos(leg.tib.angle);
        bodyCoords.y = robot.height / 2.0 + leg.tib.length * Math.sin(leg.tib
                .angle);
        return bodyCoords;
    }

    public Line2D.Double getLegCoords(int id) throws
            IndexOutOfBoundsException, RobotFallException {
        Leg leg = robot.getLeg(id);
        return new Line2D.Double(bodyCoords.x, bodyCoords.y - robot.height /
                2.0, bodyCoords.x + leg.tib.length * Math.cos(leg.tib.angle),
                bodyCoords.y - robot.height / 2.0 - leg.tib.length * Math.sin
                        (leg.tib.angle));
    }
}