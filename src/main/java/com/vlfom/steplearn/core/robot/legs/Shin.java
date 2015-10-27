package com.vlfom.steplearn.core.robot.legs;

import com.vlfom.steplearn.core.robot.general.BodyPart;

public class Shin extends BodyPart {
    public int length;
    public int angle;

    public Shin(int length, int angle, int weight) {
        this.length = length;
        this.angle = angle;
        this.weight = weight;
    }

    @Override
    public Object copy() {
        return new Shin(length, angle, weight);
    }
}