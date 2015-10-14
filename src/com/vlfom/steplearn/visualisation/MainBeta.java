package com.vlfom.steplearn.visualisation;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.learning.general.*;
import com.vlfom.steplearn.learning.robot.RobotAction;
import com.vlfom.steplearn.learning.robot.RobotMarkovDecisionProcess;
import com.vlfom.steplearn.learning.robot.RobotState;
import com.vlfom.steplearn.robot.*;
import com.vlfom.steplearn.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;

public class MainBeta extends JPanel implements ActionListener {
    Timer timer;
    RobotMarkovDecisionProcess mdp;
    QLearning qLearning;
    public RobotState start, state;

    public MainBeta() {
        this.addKeyListener(new MyKeyListener());

        timer = new Timer(50, this);
        timer.setInitialDelay(190);
        timer.start();

        Robot robot = new Robot(new Body(50, 100, 5));
        robot.addLeg(new Leg(new Tib(60, 90, 2), new Foot(5, 200, 90, 1)));
        robot.addLeg(new Leg(new Tib(60, 90, 2), new Foot(5, 200, 90, 1)));
        robot.setSupportingLeg(0);
        RobotPicture robotPicture = new RobotPicture(
                robot, new Line2D.Double(0, -1, 700, -1)
        );

        mdp = new RobotMarkovDecisionProcess();
        qLearning = new QLearning(mdp);

        start = new RobotState((RobotPicture) robotPicture.clone());

        int repeatsNumber = 1000;
        int repeatDepth = 1000;
        for (int i = 0; i < repeatsNumber; ++i) {
            if( i%(repeatsNumber/100) == 0) System.out.print(i/
                    (repeatsNumber/100) + "%, ");
            state = (RobotState) start.clone();
            for (int j = 0; j < repeatDepth; ++j) {
                state = (RobotState) qLearning.iterate(state, true);
                if (state == null) {
                    break;
                }
            }
        }

        state = (RobotState) start.clone();

        System.out.println("Ended learning.\n");
    }

    class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            state = (RobotState) start.clone();
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
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
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2));
        g2.translate(0, 2 * 700 - 800);
        g2.scale(1, -1);
        g2.draw(state.robotPicture.getBodyCoords());

        for (int i = 0; i < state.robotPicture.robot.getLegsCount(); ++i) {
            if( i%2 == 0)
                g2.setColor(Color.RED);
            else
                g2.setColor(Color.BLUE);
            g2.draw(state.robotPicture.getTibCoords(i));
            g2.draw(state.robotPicture.getFootCoords(i));
        }
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(state.robotPicture.ground.x1, state
                .robotPicture.ground.y1 - 2, state.robotPicture.ground.x2,
                state.robotPicture.ground.y2 - 2));
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

        System.out.println(state);

        //outputAllPossibleActions(state);

        state = (RobotState) qLearning.iterate(state, false);
        if (state == null) {
            state = (RobotState) start.clone();
            System.out.println("Failed. Starting again.\n");
        }

        System.out.println("");

        render(g2);
    }

    void applyUserAction() {
        RobotAction robotAction = new RobotAction(2);
        robotAction.tibRotation.set(0, 1);
        robotAction.footRotation.set(0, -1);
        robotAction.tibRotation.set(1, 1);
        robotAction.footRotation.set(1, -1);
        robotAction.supportingLegIndex = 0;
        System.out.println(robotAction.applyAction(state));

        outputStateInfo(state);
    }

    void outputStateInfo(RobotState state) {
        System.out.println(state + " " +
                state.robotPicture.bodyCoords.x + " " +
                state.robotPicture.robot.getSupportingLeg().foot.x +
                "    {" +
                (state.robotPicture.robot.getLeg(0) == state.robotPicture
                        .robot.getSupportingLeg() ? 0 : 1) + "}");
    }

    void outputAllPossibleActions(RobotState state) {
        for(com.vlfom.steplearn.learning.general.Action action : mdp.getActionsList(state))
            System.out.println(action + " " + mdp.reward(state, action
                    .applyAction(state)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}