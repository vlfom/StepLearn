package com.vlfom.steplearn.robot;

import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;

import java.util.ArrayList;

public class Robot {
    public int weight;
    public Body body;

    private Leg supportingLeg;

    private ArrayList<Leg> legs;

    public Robot(Body body) {
        legs = new ArrayList<>();
        this.body = body;
        this.weight = body.weight;
    }

    public void updateStateInfo() throws RobotFallException {
        for (Leg leg : legs) {
            if (leg.tib.angle + leg.foot.angle == 180) {
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
        return legs.get(id);
    }

    public int addLeg(Leg leg) {
        legs.add(leg);
        return legs.size() - 1;
    }

    public int getLegsCount() {
        return legs.size();
    }

    public void rotateLeg(int id, int deg) throws HitObjectException {
        Leg leg = legs.get(id);

    }
}