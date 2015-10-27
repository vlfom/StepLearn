package com.vlfom.steplearn.core.robot.body;

import com.vlfom.steplearn.core.robot.general.BodyPart;

public class Body extends BodyPart {
    public int width;
    public int height;

    public Body(int width, int height, int weight) {
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    @Override
    public Object copy() {
        return new Body(width, height, weight);
    }
}