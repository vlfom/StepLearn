package com.vlfom.steplearn.robot;

public class Body extends BodyPart {
    public int width;
    public int height;
    public int weight;

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