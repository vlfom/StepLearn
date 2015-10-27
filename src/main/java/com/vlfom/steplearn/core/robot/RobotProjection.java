package com.vlfom.steplearn.core.robot;

import com.vlfom.steplearn.core.robot.exceptions.HitObjectException;
import com.vlfom.steplearn.core.robot.exceptions.RobotFallException;
import com.vlfom.steplearn.core.robot.legs.Leg;
import com.vlfom.steplearn.core.util.Copyable;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RobotProjection implements Copyable {
    private Robot robot;
    private Line2D.Double ground;

    public RobotProjection(Robot robot, Line2D.Double ground) {
        this.robot = robot;
        this.ground = ground;
    }

    public Line2D.Double getGround() {
        return ground;
    }

    public void setGround(Line2D.Double ground) {
        this.ground = ground;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void reInitialize() throws HitObjectException {
        Leg leg = robot.getSupportingLeg();

        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.foot.angle);

        robot.body.x = leg.foot.x + leg.shin.length * Math.cos(
                angle2) - leg.thigh.length * Math.cos(angle1);
        robot.body.y = robot.body.height / 2.0 + leg.shin.length * Math.sin(
                angle2) + leg.thigh.length * Math.sin(angle1);

        Line2D.Double thighCoords;
        Line2D.Double shinCoords;
        Line2D.Double footCoords;
        Line2D.Double bodyBottom = new Line2D.Double(
                robot.body.x - robot.body.width / 2.0,
                robot.body.y - robot.body.height / 2.0 + 1,
                robot.body.x + robot.body.width / 2.0,
                robot.body.y - robot.body.height / 2.0 + 1);

        for (int i = 0; i < robot.getLegsCount(); ++i) {
            thighCoords = getThighCoords(i);
            shinCoords = getShinCoords(i);
            footCoords = getFootCoords(i);

            if (leg.shin.angle < 0 || leg.shin.angle > 180 ||
                    leg.foot.angle < 0 || leg.foot.angle > 180 ||
                    leg.thigh.angle < 0 || leg.thigh.angle > 180 ||
                    thighCoords.intersectsLine(ground) ||
                    thighCoords.intersectsLine(bodyBottom) ||
                    shinCoords.intersectsLine(ground) ||
                    shinCoords.intersectsLine(bodyBottom) ||
                    footCoords.intersectsLine(ground) ||
                    footCoords.intersectsLine(bodyBottom)) {
                throw new HitObjectException("");
            }
        }
    }

    public Rectangle2D.Double getBodyCoords() {
        return new Rectangle2D.Double(robot.body.x - robot.body.width / 2.0,
                robot.body.y - robot.body.height / 2.0, robot.body.width,
                robot.body.height);
    }

    public Line2D.Double getThighCoords(int id) {
        Leg leg = robot.getLeg(id);
        double angle = Math.toRadians(leg.thigh.angle);
        return new Line2D.Double(robot.body.x,
                robot.body.y - robot.body.height / 2.0,
                robot.body.x + leg.thigh.length * Math.cos(angle),
                robot.body.y - robot.body.height / 2.0 - leg.thigh.length * Math
                        .sin(angle));
    }

    public Line2D.Double getShinCoords(int id) {
        Leg leg = robot.getLeg(id);
        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.thigh.angle - leg.shin.angle);
        Point2D.Double O = new Point2D.Double(
                robot.body.x + leg.thigh.length * Math.cos(angle1),
                robot.body.y - robot.body.height / 2.0 -
                        leg.thigh.length * Math.sin(angle1));
        return new Line2D.Double(O.x, O.y,
                O.x - leg.shin.length * Math.cos(angle2),
                O.y + leg.shin.length * Math.sin(angle2));
    }

    public Line2D.Double getFootCoords(int id) {
        Leg leg = robot.getLeg(id);
        double angle1 = Math.toRadians(leg.thigh.angle);
        double angle2 = Math.toRadians(leg.thigh.angle - leg.shin.angle);
        double angle3 = Math.toRadians(
                180 - leg.thigh.angle + leg.shin.angle - leg.foot.angle);
        Point2D.Double O = new Point2D.Double(
                robot.body.x + leg.thigh.length * Math.cos(angle1),
                robot.body.y - robot.body.height / 2.0 -
                        leg.thigh.length * Math.sin(angle1));
        Point2D.Double C = new Point2D.Double(
                O.x - leg.shin.length * Math.cos(angle2),
                O.y + leg.shin.length * Math.sin(angle2));
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

        robot.body.x = leg.foot.x + leg.shin.length * Math.cos(
                angle2) - leg.thigh.length * Math.cos(angle1);
        robot.body.y = robot.body.height / 2.0 + leg.shin.length * Math.sin(
                angle2) + leg.thigh.length * Math.sin(angle1);
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
        Line2D.Double bodyBottom = new Line2D.Double(
                robot.body.x - robot.body.width / 2.0,
                robot.body.y - robot.body.height / 2.0 + 1,
                robot.body.x + robot.body.width / 2.0,
                robot.body.y - robot.body.height / 2.0 + 1);
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
        leg.foot.x = robot.body.x + leg.thigh.length * Math.cos(
                angle1) - leg.shin.length * Math.cos(angle2);
    }

    @Override
    public Object copy() {
        return new RobotProjection((Robot) robot.copy(),
                (Line2D.Double) ground.clone());
    }
}