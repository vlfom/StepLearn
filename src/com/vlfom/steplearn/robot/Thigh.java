package com.vlfom.steplearn.robot;

public class Thigh extends BodyPart {
    public int length;
    public int angle;

    public Thigh(int length, int angle, int weight) {
        this.length = length;
        this.angle = angle;
        this.weight = weight;
    }

    @Override
    public Object copy() {
        return new Thigh(length, angle, weight);
    }
}
