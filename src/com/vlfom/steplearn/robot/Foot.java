package com.vlfom.steplearn.robot;

public class Foot extends BodyPart {
    public int length;
    public double angle;

    public Foot(int length, int x, double angle, int weight) {
        this.length = length;
        this.x = x;
        this.angle = angle;
        this.weight = weight;
    }
}