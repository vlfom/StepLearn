package com.vlfom.steplearn.robot;

public class Leg extends BodyPart implements Cloneable {
    public Tib tib;
    public Foot foot;

    public Leg(Tib tib, Foot foot) {
        this.tib = tib;
        this.foot = foot;
        this.weight = tib.weight + foot.weight;
    }

    @Override
    protected Object clone() {
        return new Leg((Tib) tib.clone(), (Foot) foot.clone());
    }
}