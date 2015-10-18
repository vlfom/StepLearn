package com.vlfom.steplearn.robot;

public class Leg extends BodyPart {
    public Thigh thigh;
    public Shin shin;
    public Foot foot;

    public Leg(Thigh thigh, Shin shin, Foot foot) {
        this.thigh = thigh;
        this.shin = shin;
        this.foot = foot;
        this.weight = shin.weight + foot.weight;
    }

    @Override
    public Object copy() {
        return new Leg((Thigh) thigh.copy(), (Shin) shin.copy(), (Foot) foot
                .copy());
    }
}