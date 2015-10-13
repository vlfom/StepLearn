package com.vlfom.steplearn.learning.robot;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.learning.general.State;

public class RobotState extends State implements Cloneable {
    public RobotPicture robotPicture;

    @Override
    public boolean compare(State o) {
        return o instanceof RobotState && this.hash().equals(o.hash());
    }

    @Override
    public Integer hash() {
        Integer hash = 0;
        for(int i = 0; i < robotPicture.robot.getLegsCount(); ++i)
            hash = hash * 181 + robotPicture.robot.getLeg(i).tib.angle;
        for(int i = 0; i < robotPicture.robot.getLegsCount(); ++i)
            hash = hash * 181 + robotPicture.robot.getLeg(i).foot.angle;
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RobotState cloned = (RobotState)super.clone();
        cloned.robotPicture = (RobotPicture) robotPicture.clone();
        return cloned;
    }
}
