package com.vlfom.steplearn.draw;

import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.robot.Leg;
import com.vlfom.steplearn.robot.Robot;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RobotPicture {
    public Robot robot;
    private Point2D.Double bodyCoords;

    public RobotPicture(Robot robot) {
        bodyCoords = new Point2D.Double();
        this.robot = robot;
    }

    public void updateStateInfo() throws RobotFallException {
        robot.updateStateInfo();
        Leg leg = robot.getSupportingLeg();
        bodyCoords.x = leg.foot.x + leg.tib.length * Math.cos(Math.toRadians
                (leg.tib.angle));
        bodyCoords.y = robot.body.height / 2.0 + leg.tib.length * Math.sin
                (Math.toRadians(leg.tib.angle));
    }

    public Rectangle2D.Double getBodyCoords() {
        return new Rectangle2D.Double(bodyCoords.x - robot.body.width / 2.0,
                bodyCoords.y - robot.body.height / 2.0, robot.body.width,
                robot.body.height);
    }

    public Line2D.Double getTibCoords(int id) throws
            IndexOutOfBoundsException, RobotFallException {
        Leg leg = robot.getLeg(id);
        return new Line2D.Double(bodyCoords.x, bodyCoords.y - robot.body
                .height / 2.0, bodyCoords.x + leg.tib.length * Math.cos(Math
                .toRadians(leg.tib.angle)), bodyCoords.y - robot.body.height
                / 2.0 - leg.tib.length * Math.sin(Math.toRadians(leg.tib
                .angle)));
    }

    public Line2D.Double getFootCoords(int id) throws
            IndexOutOfBoundsException, RobotFallException {
        Leg leg = robot.getLeg(id);
        Point2D.Double center = new Point2D.Double(bodyCoords.x + leg.tib
                .length * Math.cos(Math.toRadians(leg.tib.angle)), bodyCoords
                .y - robot.body.height / 2.0 - leg.tib.length * Math.sin(Math
                .toRadians(leg.tib.angle)));
        return new Line2D.Double(center.x + leg.foot.length / 2.0 * Math.cos
                (Math.toRadians(leg.foot.angle - leg.tib.angle)), center.y +
                leg.foot.length / 2.0 * Math.sin(Math.toRadians(leg.foot
                        .angle - leg.tib.angle)), center.x - leg.foot.length
                / 2.0 * Math.cos(Math.toRadians(leg.foot.angle - leg.tib
                .angle)), center.y - leg.foot.length / 2.0 * Math.sin(Math
                .toRadians(leg.foot.angle - leg.tib.angle)));
    }
}