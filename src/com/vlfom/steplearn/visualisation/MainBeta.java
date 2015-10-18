package com.vlfom.steplearn.visualisation;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.HitObjectException;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.learning.general.QLearning;
import com.vlfom.steplearn.learning.robot.RobotAction;
import com.vlfom.steplearn.learning.robot.RobotMarkovDecisionProcess;
import com.vlfom.steplearn.learning.robot.RobotState;
import com.vlfom.steplearn.robot.*;
import com.vlfom.steplearn.robot.Robot;
import com.vlfom.steplearn.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;

public class MainBeta extends JPanel implements ActionListener {
    public RobotState start, state;
    Timer timer;
    RobotMarkovDecisionProcess mdp;
    QLearning qLearning;
    RobotPicture robotPicture, startRobotPicture;

    public MainBeta() {
        this.addKeyListener(new MyKeyListener());

        timer = new Timer(50, this);
        timer.setInitialDelay(190);
        timer.start();

        Robot robot = new Robot(new Body(50, 100, 5));
        robot.addLeg(new Leg(new Thigh(60, 90, 2), new Shin(40, 180, 2), new
                Foot(5, 200, 90, 1)));
        robot.addLeg(new Leg(new Thigh(50, 90, 2), new Shin(50, 180, 2), new
                Foot(5, 200, 90, 1)));
        robot.setSupportingLeg(0);
        startRobotPicture = new RobotPicture(robot, new Line2D.Double(0, -1,
                700, -1));
        try {
            startRobotPicture.initialize();
        } catch (HitObjectException ignored) {
        }

        mdp = new RobotMarkovDecisionProcess((RobotPicture) startRobotPicture
                .copy());
        qLearning = new QLearning(mdp);

        ArrayList<Integer> thighAngle = new ArrayList<>(robot.getLegsCount());
        ArrayList<Integer> shinAngle = new ArrayList<>(robot.getLegsCount());
        for (int i = 0; i < robot.getLegsCount(); ++i) {
            thighAngle.add(robot.getLeg(i).thigh.angle);
            shinAngle.add(robot.getLeg(i).shin.angle);
        }
        start = new RobotState(robot.getLegsCount(), robot
                .supportingLegIndex, thighAngle, shinAngle);

        int repeatsNumber = 100;
        int repeatDepth = 100;
        for (int i = 0; i < repeatsNumber; ++i) {
            if (i % (repeatsNumber / 100) == 0) {
                System.out.print(i / (repeatsNumber / 100) + "%, ");
            }
            state = (RobotState) start.copy();
            for (int j = 0; j < repeatDepth; ++j) {
                state = (RobotState) qLearning.iterateLearning(state);
                if (state == null) {
                    break;
                }
            }
        }

        state = (RobotState) start.copy();
        robotPicture = (RobotPicture) startRobotPicture.copy();

        System.out.println("Ended learning.\n");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TimerBasedAnimation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new MainBeta();
        panel.setFocusable(true);
        frame.add(panel);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.translate(0, 2 * 700 - 800);
        g2.scale(1, -1);

        g2.draw(robotPicture.getBodyCoords());

        Color colors[] = {Color.DARK_GRAY, Color.GRAY, Color.GREEN};

        Line2D.Double thigh, shin, foot;
        for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i) {
            g2.setColor(colors[i]);
            thigh = robotPicture.getThighCoords(i);
            shin = robotPicture.getShinCoords(i);
            foot = robotPicture.getFootCoords(i);
            g2.draw(new Ellipse2D.Double(thigh.x1 - 2, thigh.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x1 - 2, shin.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x2 - 2, shin.y2 - 2, 4, 4));
            g2.draw(thigh);
            g2.draw(shin);
            g2.draw(foot);
        }
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(robotPicture.ground.x1, robotPicture.ground
                .y1 - 2, robotPicture.ground.x2, robotPicture.ground.y2 - 2));
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints
                .KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints
                .VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);

        RobotAction ra = (RobotAction) qLearning.argMax2(state);

        if (ra == null) {
            state = (RobotState) start.copy();
            robotPicture = (RobotPicture) startRobotPicture.copy();
            System.out.println("Failed. Starting again.\n");
        } else {

            robotPicture.robot.setSupportingLeg(ra.supportingLegIndex);
            try {
                robotPicture.initialize();
            } catch (HitObjectException ignored) {
            }

            try {
                robotPicture.rotateSupportingLeg(ra.thighRotation.get(ra
                        .supportingLegIndex), ra.shinRotation.get(ra
                        .supportingLegIndex), ra.shinRotation.get(ra
                        .supportingLegIndex) - ra.thighRotation.get(ra
                        .supportingLegIndex));
            } catch (RobotFallException ignored) {
            }

            for (int i = 0; i < robotPicture.robot.getLegsCount(); ++i) {
                if (i != ra.supportingLegIndex) {
                    try {
                        robotPicture.rotateRegularLeg(i, ra.thighRotation.get
                                (i), ra.shinRotation.get(i), ra.shinRotation
                                .get(i) - ra.thighRotation.get(i));
                    } catch (HitObjectException ignored) {
                    }
                }
            }

            state.supportingLegIndex = ra.supportingLegIndex;
            for (int i = 0; i < state.legsCount; ++i) {
                state.thighAngle.set(i, robotPicture.robot.getLeg(i).thigh
                        .angle);
                state.shinAngle.set(i, robotPicture.robot.getLeg(i).shin.angle);
            }
        }

        render(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            state = (RobotState) start.copy();
            robotPicture = (RobotPicture) startRobotPicture.copy();
            System.out.println("Restarting.\n");
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}