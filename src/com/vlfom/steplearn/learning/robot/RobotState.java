package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.learning.general.State;

public class RobotState extends State implements Cloneable {
    public RobotPicture robotPicture;

    public RobotState(RobotPicture robotPicture) {
        this.robotPicture = robotPicture;
    }

    @Override
    public String toString() {
        String string = "";
        for(int i = 0 ; i < robotPicture.robot.getLegsCount(); ++i)
            string += "{" + robotPicture.robot.getLeg(i).tib.angle + ", " +
                    robotPicture.robot.getLeg(i).foot.angle + "} ";
        return string;
    }

    @Override
    public long hash() {
        long hash = 0;
        for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i) {
            hash = hash * 181 + robotPicture.robot.getLeg(i).tib.angle;
        }
        for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i) {
            hash = hash * 181 + robotPicture.robot.getLeg(i).foot.angle;
        }
        return hash;
    }

    @Override
    public Object clone() {
        return new RobotState((RobotPicture) robotPicture.clone());
    }
}
