package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.learning.general.State;

import java.util.ArrayList;

public class RobotState extends State {
    public double bodyX;
    public int legsCount;
    public int supportingLegIndex;
    public ArrayList<Integer> tibAngle, footAngle;
    public ArrayList<Double> footCoords;

    public RobotState() {
    }

    public RobotState(double bodyX, int legsCount, int supportingLegIndex,
                      ArrayList<Integer> tibAngle, ArrayList<Integer>
                              footAngle, ArrayList<Double> footCoords) {
        this.bodyX = bodyX;
        this.legsCount = legsCount;
        this.supportingLegIndex = supportingLegIndex;
        this.tibAngle = new ArrayList<>(legsCount);
        this.footAngle = new ArrayList<>(legsCount);
        this.footCoords = new ArrayList<>(legsCount);
        for (int i = 0; i < legsCount; ++i) {
            this.tibAngle.add(tibAngle.get(i));
            this.footAngle.add(footAngle.get(i));
            this.footCoords.add(footCoords.get(i));
        }
    }

    @Override
    public String toString() {
        String string = "State: ";
        for (int i = 0; i < legsCount; ++i) {
            string += "{" + tibAngle.get(i) + ", " +
                    footAngle.get(i) + "} ";
        }
        return string;
    }

    @Override
    public long hash() {
        long hash = 0;
        for (int i = 0; i < legsCount; ++i) {
            hash = hash * 181 + tibAngle.get(i);
        }
        for (int i = 0; i < legsCount; ++i) {
            hash = hash * 181 + footAngle.get(i);
        }
        return hash;
    }

    @Override
    public Object copy() {
        return new RobotState(bodyX, legsCount, supportingLegIndex, tibAngle,
                footAngle, footCoords);
    }
}
