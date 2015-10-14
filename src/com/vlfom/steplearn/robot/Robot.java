package com.vlfom.steplearn.robot;

import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Robot implements Cloneable {
    public int weight;
    public Body body;

    private Leg supportingLeg;
    private int supportingLegIndex;

    private ArrayList<Leg> legs;

    public Robot(Body body) {
        legs = new ArrayList<>();
        this.body = body;
        this.weight = body.weight;
    }

    public void setSupportingLeg(int id) {
        supportingLeg = legs.get(id);
        supportingLegIndex = id;
    }

    public Leg getSupportingLeg() {
        return supportingLeg;
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
    public Object clone() {
        Robot cloned = new Robot((Body) body.clone());
        cloned.supportingLegIndex = supportingLegIndex;
        cloned.supportingLeg = (Leg) supportingLeg.clone();
        cloned.legs = new ArrayList<>();
        for(Leg leg:legs)
            cloned.legs.add((Leg) leg.clone());
        return cloned;
    }
}