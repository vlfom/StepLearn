package com.vlfom.steplearn.visualisation;

import com.vlfom.steplearn.draw.RobotPicture;
import com.vlfom.steplearn.exceptions.RobotFallException;
import com.vlfom.steplearn.robot.*;
import com.vlfom.steplearn.robot.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;

public class Main extends JPanel implements ActionListener {
    Timer timer;
    Robot robot;
    RobotPicture robotPicture;

    public Main() {
        timer = new Timer(20, this);
        timer.setInitialDelay(190);
        timer.start();

        robot = new Robot(new Body(100, 100, 5));
        robot.addLeg(new Leg(new Tib(50, 90, 2), new Foot(5, 200,
                90, 1)));
        robot.addLeg(new Leg(new Tib(50, 90, 2), new Foot(5, 200,
                90, 1)));

        robotPicture = new RobotPicture(robot);
        try {
            robotPicture.updateStateInfo();
        } catch (RobotFallException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TimerBasedAnimation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Main());
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
            g2.draw(robotPicture.getBodyCoords());
            for (int i = 0; i < 2; ++i) {
                g2.draw(robotPicture.getTibCoords(i));
                g2.draw(robotPicture.getFootCoords(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(0, -2, 700, -2));
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


        double toSupport = (int)(Math.random() * 10 - 5);

        robot.getLeg(0).tib.angle += toSupport;
        robot.getLeg(0).foot.angle += toSupport;
        if (robot.getLeg(0).tib.angle < 18 || robot.getLeg(0).tib
                .angle > 180-18) {
            robot.getLeg(0).tib.angle -= toSupport;
            robot.getLeg(0).foot.angle -= toSupport;
        }

        for (int i = 1; i < robot.getLegsCount(); ++i) {
            while (true) {
                double notToSupport = (int)(Math.random() * 10 - 5);
                robot.getLeg(i).tib.angle += notToSupport;
                if (robot.getLeg(i).tib.angle < 18 || robot.getLeg
                        (i).tib.angle > 180-18) {
                    robot.getLeg(i).tib.angle -= notToSupport;
                    continue;
                }
                if (robot.getLeg(0).tib.angle <= 90) {
                    if (robot.getLeg(0).tib.angle >= robot.getLeg(i).tib
                            .angle || robot.getLeg(i).tib.angle >= 180 - robot
                            .getLeg(0).tib.angle) {
                        break;
                    }
                } else {
                    if (robot.getLeg(0).tib.angle <= robot.getLeg(i).tib
                            .angle || robot.getLeg(i).tib.angle <= 180
                            -robot.getLeg(0).tib.angle) {
                        break;
                    }
                }
                robot.getLeg(i).tib.angle -= notToSupport;
            }
        }

        try {
            robotPicture.updateStateInfo();
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