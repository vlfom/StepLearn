package com.vlfom.steplearn.core.learning.robot;

import com.vlfom.steplearn.core.learning.general.State;
import com.vlfom.steplearn.core.robot.Robot;

import java.util.ArrayList;
import java.util.Collections;

public class RobotState extends State {
    private int legsCount;
    private int supportingLegIndex;
    private ArrayList<Integer> thighAngle, shinAngle;

    public RobotState() {
    }

    public RobotState(int legsCount) {
        this.thighAngle = new ArrayList<>(Collections.nCopies(legsCount, 0));
        this.shinAngle = new ArrayList<>(Collections.nCopies(legsCount, 0));
    }

    public RobotState(Robot robot) {
        this.legsCount = robot.getLegsCount();
        this.supportingLegIndex = robot.supportingLegIndex;
        this.thighAngle = new ArrayList<>(robot.getLegsCount());
        this.shinAngle = new ArrayList<>(robot.getLegsCount());
        for (int i = 0; i < robot.getLegsCount(); ++i) {
            thighAngle.add(robot.getLeg(i).thigh.angle);
            shinAngle.add(robot.getLeg(i).shin.angle);
        }
    }

    public RobotState(int legsCount, int supportingLegIndex,
                      ArrayList<Integer> thighAngle, ArrayList<Integer>
                              shinAngle) {
        this.legsCount = legsCount;
        this.supportingLegIndex = supportingLegIndex;
        this.thighAngle = new ArrayList<>(legsCount);
        this.shinAngle = new ArrayList<>(legsCount);
        for (int i = 0; i < legsCount; ++i) {
            this.thighAngle.add(thighAngle.get(i));
            this.shinAngle.add(shinAngle.get(i));
        }
    }

    public int getLegsCount() {
        return legsCount;
    }

    public void setLegsCount(int legsCount) {
        this.legsCount = legsCount;
    }

    public int getSupportingLegIndex() {
        return supportingLegIndex;
    }

    public void setSupportingLegIndex(int supportingLegIndex) {
        this.supportingLegIndex = supportingLegIndex;
    }

    public Integer getThighAngle(int id) {
        return thighAngle.get(id);
    }

    public void setThighAngle(int id, Integer value) {
        this.thighAngle.set(id, value);
    }

    public Integer getShinAngle(int id) {
        return shinAngle.get(id);
    }

    public void setShinAngle(int id, Integer value) {
        this.shinAngle.set(id, value);
    }

    @Override
    public String toString() {
        String string = "State: ";
        for (int i = 0; i < legsCount; ++i) {
            string += "{" + thighAngle.get(i) + ", " + shinAngle.get(
                    i) + " " + (shinAngle.get(i) - thighAngle.get(i)) +
                    "} ";
        }
        string += "{" + this.supportingLegIndex + "}";
        return string;
    }

    @Override
    public long hash() {
        long hash = 0;
        for (int i = 0; i < legsCount; ++i) {
            hash = hash * 181 + thighAngle.get(i);
        }
        for (int i = 0; i < legsCount; ++i) {
            hash = hash * 181 + shinAngle.get(i);
        }
        hash = hash * 181 + supportingLegIndex;
        return hash;
    }

    @Override
    public Object copy() {
        return new RobotState(legsCount, supportingLegIndex, thighAngle,
                shinAngle);
    }
}
