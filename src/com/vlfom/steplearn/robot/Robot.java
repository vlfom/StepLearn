package com.vlfom.steplearn.robot;

import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;

import java.util.ArrayList;

public class Robot {
    public int width;
    public int height;
    public int weight;
    public Body body;

    private Leg supportingLeg;

    private ArrayList<Leg> Legs;

    public Robot(Body body, int width, int height, int weight) {
        this.body = body;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public void recalcSupportingLeg() throws RobotFallException {
        for (Leg leg : Legs) {
            if (leg.tib.angle == leg.foot.angle) {
                supportingLeg = leg;
                break;
            }
        }
        if (supportingLeg == null) {
            throw new RobotFallException("No supporting leg found.");
        }
    }

    public Leg getSupportingLeg() {
        return supportingLeg;
    }

    public Leg getLeg(int id) throws IndexOutOfBoundsException {
        return Legs.get(id);
    }

    public int addLeg(Leg leg) {
        Legs.add(leg);
        return Legs.size() - 1;
    }

    public void rotateLeg(int id, int deg) throws HitObjectException {
        Leg leg = Legs.get(id);

    }
}