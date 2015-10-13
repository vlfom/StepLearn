package com.vlfom.steplearn.visualisation;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.learning.general.QLearning;
import com.vlfom.steplearn.learning.robot.RobotMarkovDecisionProcess;
import com.vlfom.steplearn.learning.robot.RobotState;
import com.vlfom.steplearn.robot.*;
import com.vlfom.steplearn.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

public class MainBeta extends JPanel implements ActionListener {
    Timer timer;
    Robot robot;
    RobotPicture robotPicture;
    RobotMarkovDecisionProcess mdp;
    QLearning qLearning;
    RobotState start, state;

    public MainBeta() {
        timer = new Timer(200, this);
        timer.setInitialDelay(190);
        timer.start();

        robot = new Robot(new Body(100, 100, 5));
        robot.addLeg(new Leg(new Tib(50, 90, 2), new Foot(5, 200, 90, 1)));
        robot.addLeg(new Leg(new Tib(50, 90, 2), new Foot(5, 200, 90, 1)));
        robotPicture = new RobotPicture(robot, new Line2D.Double(0, -2, 700,
                -2));
        try {
            robotPicture.updateStateInfo();
        } catch (RobotFallException e) {
            e.printStackTrace();
        }


        mdp = new RobotMarkovDecisionProcess();
        qLearning = new QLearning(mdp);

        start = new RobotState((RobotPicture) robotPicture.clone());

        for (int i = 0; i < 10; ++i) {
            state = (RobotState) start.clone();
            for (int j = 0; j < 10000; ++j) {
                state = (RobotState) qLearning.iterate(state, true);
                if (state == null) {
                    break;
                }
                try {
                    state.robotPicture.updateStateInfo();
                } catch (RobotFallException e) {
                    break;
                }
            }
        }

        state = (RobotState) start.clone();

        System.out.println("Ended learning.");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TimerBasedAnimation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainBeta());
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2));
        g2.translate(0, 2 * 700 - 800);
        g2.scale(1, -1);
        try {
            g2.draw(state.robotPicture.getBodyCoords());
            for (int i = 0; i < state.robotPicture.robot.getLegsCount(); ++i) {
                g2.draw(state.robotPicture.getTibCoords(i));
                g2.draw(state.robotPicture.getFootCoords(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.setColor(Color.BLACK);
        g2.draw(state.robotPicture.ground);
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

        state = (RobotState) qLearning.iterate(state, false);
        if (state == null) {
            state = (RobotState) start.clone();
            System.out.println("Failed. Starting again.\n");
        }

        try {
            state.robotPicture.updateStateInfo();
        } catch (RobotFallException e) {
            e.printStackTrace();
        }

        render(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}