package com.vlfom.steplearn.robot;

public class Leg extends BodyPart {
    public Tib tib;
    public Foot foot;

    public Leg(Tib tib, Foot foot) {
        this.tib = tib;
        this.foot = foot;
        this.weight = tib.weight + foot.weight;
    }
}