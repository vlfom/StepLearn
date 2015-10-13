package com.vlfom.steplearn.robot;

public class Foot extends BodyPart {
    public int length;
    public int angle;

    public Foot(int length, int x, int angle, int weight) {
        this.length = length;
        this.x = x;
        this.angle = angle;
        this.weight = weight;
    }
}