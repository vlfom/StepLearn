package com.vlfom.steplearn.robot;

public class Foot extends BodyPart implements Cloneable {
    public int length;
    public int angle;

    public Foot(int length, double x, int angle, int weight) {
        this.length = length;
        this.x = x;
        this.angle = angle;
        this.weight = weight;
    }

    @Override
    protected Object clone() {
        return new Foot(length, x, angle, weight);
    }
}