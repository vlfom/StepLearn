package com.vlfom.steplearn.core.robot;

import com.vlfom.steplearn.core.robot.body.Body;
import com.vlfom.steplearn.core.robot.legs.Leg;
import com.vlfom.steplearn.core.util.Copyable;

import java.util.ArrayList;

public class Robot implements Copyable {
    public int weight;
    public Body body;

    public Leg supportingLeg;
    public int supportingLegIndex;

    public ArrayList<Leg> legs;

    public Robot(Body body) {
        legs = new ArrayList<>();
        this.body = body;
        this.weight = body.weight;
    }

    public Leg getSupportingLeg() {
        return supportingLeg;
    }

    public void setSupportingLeg(int id) {
        supportingLeg = legs.get(id);
        supportingLegIndex = id;
    }

    public int getSupportingLegIndex() {
        return supportingLegIndex;
    }

    public Leg getLeg(int id) {
        return legs.get(id);
    }

    public int addLeg(Leg leg) {
        legs.add(leg);
        return legs.size() - 1;
    }

    public int getLegsCount() {
        return legs.size();
    }

    @Override
    public Object copy() {
        Robot cloned = new Robot((Body) body.copy());
        cloned.supportingLegIndex = supportingLegIndex;
        cloned.supportingLeg = (Leg) supportingLeg.copy();
        cloned.legs = new ArrayList<>();
        for (Leg leg : legs) {
            cloned.legs.add((Leg) leg.copy());
        }
        return cloned;
    }
}