package com.vlfom.steplearn.draw;

import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.robot.Leg;
import com.vlfom.steplearn.robot.Robot;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RobotPicture implements Cloneable {
    public Robot robot;
    public Line2D.Double ground;
    public Point2D.Double bodyCoords;

    public RobotPicture(Robot robot, Line2D.Double ground) {
        bodyCoords = new Point2D.Double();
        this.robot = robot;
        this.ground = ground;
    }

    public Rectangle2D.Double getBodyCoords() {
        return new Rectangle2D.Double(bodyCoords.x - robot.body.width / 2.0,
                bodyCoords.y - robot.body.height / 2.0, robot.body.width,
                robot.body.height);
    }

    public Line2D.Double getTibCoords(int id) {
        Leg leg = robot.getLeg(id);
        return new Line2D.Double(bodyCoords.x, bodyCoords.y - robot.body
                .height / 2.0, bodyCoords.x + leg.tib.length * Math.cos(Math
                .toRadians(leg.tib.angle)), bodyCoords.y - robot.body.height
                / 2.0 - leg.tib.length * Math.sin(Math.toRadians(leg.tib
                .angle)));
    }

    public Line2D.Double getFootCoords(int id) {
        Leg leg = robot.getLeg(id);
        Point2D.Double center = new Point2D.Double(bodyCoords.x + leg.tib
                .length * Math.cos(Math.toRadians(leg.tib.angle)), bodyCoords
                .y - robot.body.height / 2.0 - leg.tib.length * Math.sin(Math
                .toRadians(leg.tib.angle)));
        return new Line2D.Double(center.x + leg.foot.length / 2.0 * Math.cos
                (Math.toRadians(leg.foot.angle + leg.tib.angle)), center.y +
                leg.foot.length / 2.0 * Math.sin(Math.toRadians(leg.foot
                        .angle + leg.tib.angle)), center.x - leg.foot.length
                / 2.0 * Math.cos(Math.toRadians(leg.foot.angle + leg.tib
                .angle)), center.y - leg.foot.length / 2.0 * Math.sin(Math
                .toRadians(leg.foot.angle + leg.tib.angle)));
    }

    public void rotateSupportingLeg(int degTib, int degFoot) throws
            RobotFallException {
        Leg leg = robot.getSupportingLeg();

        if (leg.tib.angle + leg.foot.angle != 180)
            throw new RobotFallException("");

        leg.tib.angle += degTib;
        leg.foot.angle += degFoot;

        if (leg.tib.angle + leg.foot.angle != 180 ||
                leg.tib.angle <= 0 || leg.tib.angle >= 180 ||
                leg.foot.angle <= 0 || leg.foot.angle >= 180) {
            leg.tib.angle -= degTib;
            leg.foot.angle -= degFoot;
            throw new RobotFallException("");
        }

        bodyCoords.x = leg.foot.x - leg.tib.length * Math.cos(Math.toRadians
                (180 - leg.foot.angle));
        bodyCoords.y = robot.body.height / 2.0 + leg.tib.length * Math.sin
                (Math.toRadians(180 - leg.foot.angle));
    }

    public void rotateRegularLeg(int id, int degTib, int degFoot) throws
            HitObjectException {
        Leg leg = robot.getLeg(id);
        leg.tib.angle += degTib;
        leg.foot.angle += degFoot;

        Line2D.Double tibCoords = getTibCoords(id);
        Line2D.Double footCoords = getFootCoords(id);
        Line2D.Double bodyBottom = new Line2D.Double(bodyCoords.x - robot
                .body.width / 2.0, bodyCoords.y - robot.body.height / 2.0 +
                2, bodyCoords.x + robot.body.width / 2.0, bodyCoords.y -
                robot.body.height / 2.0 + 2);
        if (tibCoords.intersectsLine(ground) ||
                tibCoords.intersectsLine(bodyBottom) ||
                footCoords.intersectsLine(ground) ||
                footCoords.intersectsLine(bodyBottom)) {
            leg.tib.angle -= degTib;
            leg.foot.angle -= degFoot;
            throw new HitObjectException("");
        }

        leg.foot.x = bodyCoords.x + leg.tib.length * Math.cos(Math.toRadians
                (leg.tib.angle));
    }

    @Override
    public Object clone() {
        RobotPicture cloned = new RobotPicture((Robot) robot.clone(), (Line2D
                .Double) ground.clone());
        cloned.bodyCoords = (Point2D.Double) bodyCoords.clone();
        return cloned;
    }
}