package com.vlfom.steplearn.robot;

public class Leg extends BodyPart {
    public Tib tib;
    public Foot foot;

    public Leg(Tib tib, Foot foot) {
        this.tib = tib;
        this.foot = foot;
        this.weight = tib.weight + foot.weight;
    }

    public class Foot extends BodyPart {
        public int length;
        public int angle;

        public Foot(int length, int x, int angle, int weight) {
            this.length = length;
            this.x = x;
            this.angle = angle;
            this.weight = weight;
        }

        public boolean isLeaned() {
            return angle == 0;
        }
    }

    public class Tib extends BodyPart {
        public int length;
        public int angle;

        public Tib(int length, int angle, int weight) {
            this.length = length;
            this.angle = angle;
            this.weight = weight;
        }
    }
}