package com.vlfom.steplearn.draw;

import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.robot.Leg;
import com.vlfom.steplearn.robot.Robot;
import com.vlfom.steplearn.util.Copyable;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RobotPicture implements Copyable {
    public Robot robot;
    public Line2D.Double ground;
    public Point2D.Double bodyCoords;

    public RobotPicture(Robot robot, Line2D.Double ground) {
        bodyCoords = new Point2D.Double();
        this.robot = robot;
        this.ground = ground;
    }

    public void initialize() {
        Leg leg = robot.getSupportingLeg();

        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.foot.angle);

        bodyCoords.x = leg.foot.x + leg.shin.length * Math.cos(angle2) - leg
                .thigh.length * Math.cos(angle1);
        bodyCoords.y = robot.body.height / 2.0 + leg.shin.length * Math.sin
                (angle2) + leg.thigh.length * Math.sin(angle1);
    }

    public Rectangle2D.Double getBodyCoords() {
        return new Rectangle2D.Double(bodyCoords.x - robot.body.width / 2.0,
                bodyCoords.y - robot.body.height / 2.0, robot.body.width,
                robot.body.height);
    }

    public Line2D.Double getThighCoords(int id) {
        Leg leg = robot.getLeg(id);
        double angle = Math.toRadians(leg.thigh.angle);
        return new Line2D.Double(bodyCoords.x, bodyCoords.y - robot.body
                .height / 2.0, bodyCoords.x + leg.thigh.length * Math.cos
                (angle), bodyCoords.y - robot.body.height / 2.0 - leg.thigh
                .length * Math.sin(angle));
    }

    public Line2D.Double getShinCoords(int id) {
        Leg leg = robot.getLeg(id);
        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.thigh.angle - leg.shin.angle);
        Point2D.Double O = new Point2D.Double(bodyCoords.x + leg.thigh.length
                * Math.cos(angle1), bodyCoords.y - robot.body.height / 2.0 -
                leg.thigh.length * Math.sin(angle1));
        return new Line2D.Double(O.x, O.y, O.x - leg.shin.length * Math.cos
                (angle2), O.y + leg.shin.length * Math.sin(angle2));
    }

    public Line2D.Double getFootCoords(int id) {
        Leg leg = robot.getLeg(id);
        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.thigh.angle - leg.shin.angle);
        double angle3 = Math.toRadians(180 - leg.thigh.angle + leg.shin.angle
                - leg.foot.angle);
        Point2D.Double O = new Point2D.Double(bodyCoords.x + leg.thigh.length
                * Math.cos(angle1), bodyCoords.y - robot.body.height / 2.0 -
                leg.thigh.length * Math.sin(angle1));
        Point2D.Double C = new Point2D.Double(O.x - leg.shin.length * Math
                .cos(angle2), O.y + leg.shin.length * Math.sin(angle2));
        double dx = leg.foot.length * Math.cos(angle3);
        double dy = leg.foot.length * Math.sin(angle3);
        return new Line2D.Double(C.x + dx, C.y + dy, C.x - dx, C.y - dy);
    }

    public void rotateSupportingLeg(int degThigh, int degShin, int degFoot)
            throws RobotFallException {
        Leg leg = robot.getSupportingLeg();

        if (leg.shin.angle != leg.thigh.angle + leg.foot.angle) {
            throw new RobotFallException("");
        }

        leg.thigh.angle += degThigh;
        leg.shin.angle += degShin;
        leg.foot.angle += degFoot;

        if (leg.shin.angle != leg.thigh.angle + leg.foot.angle ||
                leg.shin.angle < 0 || leg.shin.angle > 180 ||
                leg.foot.angle < 0 || leg.foot.angle > 180 ||
                leg.thigh.angle < 0 || leg.thigh.angle > 180) {
            leg.thigh.angle -= degThigh;
            leg.shin.angle -= degShin;
            leg.foot.angle -= degFoot;
            throw new RobotFallException("");
        }

        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.foot.angle);

        bodyCoords.x = leg.foot.x + leg.shin.length * Math.cos(angle2) - leg
                .thigh.length * Math.cos(angle1);
        bodyCoords.y = robot.body.height / 2.0 + leg.shin.length * Math.sin
                (angle2) + leg.thigh.length * Math.sin(angle1);
    }

    public void rotateRegularLeg(int id, int degThigh, int degShin, int
            degFoot) throws HitObjectException {
        Leg leg = robot.getLeg(id);

        leg.thigh.angle += degThigh;
        leg.shin.angle += degShin;
        leg.foot.angle += degFoot;

        Line2D.Double thighCoords = getThighCoords(id);
        Line2D.Double shinCoords = getShinCoords(id);
        Line2D.Double footCoords = getFootCoords(id);
        Line2D.Double bodyBottom = new Line2D.Double(bodyCoords.x - robot
                .body.width / 2.0, bodyCoords.y - robot.body.height / 2.0 +
                1, bodyCoords.x + robot.body.width / 2.0, bodyCoords.y -
                robot.body.height / 2.0 + 1);
        if (leg.shin.angle < 0 || leg.shin.angle > 180 ||
                leg.foot.angle < 0 || leg.foot.angle > 180 ||
                leg.thigh.angle < 0 || leg.thigh.angle > 180 ||
                thighCoords.intersectsLine(ground) ||
                thighCoords.intersectsLine(bodyBottom) ||
                shinCoords.intersectsLine(ground) ||
                shinCoords.intersectsLine(bodyBottom) ||
                footCoords.intersectsLine(ground) ||
                footCoords.intersectsLine(bodyBottom)) {
            leg.thigh.angle -= degThigh;
            leg.shin.angle -= degShin;
            leg.foot.angle -= degFoot;
            throw new HitObjectException("");
        }

        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.thigh.angle - leg.shin.angle);
        leg.foot.x = bodyCoords.x + leg.thigh.length * Math.cos(angle1) - leg
                .shin.length * Math.cos(angle2);
    }

    @Override
    public Object copy() {
        RobotPicture copied = new RobotPicture((Robot) robot.copy(), (Line2D
                .Double) ground.clone());
        copied.bodyCoords = (Point2D.Double) bodyCoords.clone();
        return copied;
    }
}