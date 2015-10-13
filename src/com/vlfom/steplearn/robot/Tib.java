package com.vlfom.steplearn.robot;

public class Tib extends BodyPart implements Cloneable {
    public int length;
    public int angle;

    public Tib(int length, int angle, int weight) {
        this.length = length;
        this.angle = angle;
        this.weight = weight;
    }

    @Override
    protected Object clone() {
        return new Tib(length, angle, weight);
    }
}