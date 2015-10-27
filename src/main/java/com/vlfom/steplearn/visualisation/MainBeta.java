package com.vlfom.steplearn.visualisation;

import com.vlfom.steplearn.core.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.core.learning.general.QLearning;
import com.vlfom.steplearn.core.learning.robot.RobotAction;
import com.vlfom.steplearn.core.learning.robot.RobotState;
import com.vlfom.steplearn.core.robot.RobotProjection;
import com.vlfom.steplearn.core.robot.exceptions.HitObjectException;
import com.vlfom.steplearn.core.robot.exceptions.RobotFallException;
import com.vlfom.steplearn.spring.config.RobotConfiguration;
import com.vlfom.steplearn.spring.config.learning.QLearningComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation
        .AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class MainBeta extends JPanel implements ActionListener {
    private ApplicationContext appContext;
    private RobotState curState;
    private RobotProjection curRobotProjection;

    public MainBeta() {
        this.addKeyListener(new MyKeyListener());

        Timer timer = new Timer(50, this);
        timer.setInitialDelay(190);
        timer.start();

//        ApplicationContext context2 = new
//                ClassPathXmlApplicationContext("robot-config.xml");
        appContext = new AnnotationConfigApplicationContext(
                RobotConfiguration.class);

        RobotState startState = (RobotState) appContext.getBean("robotState");

        QLearning qLearning = (QLearning) appContext.getBean("qLearning");
        int repeatsNumber = 100;
        int repeatDepth = 100;
        for (int i = 0; i < repeatsNumber; ++i) {
            if (i % (repeatsNumber / 100) == 0) {
                System.out.print(i / (repeatsNumber / 100) + "%, ");
            }
            curState = (RobotState) startState.copy();
            for (int j = 0; j < repeatDepth; ++j) {
                curState = (RobotState) qLearning.iterateLearning(curState);
                if (curState == null) {
                    break;
                }
            }
        }

        MarkovDecisionProcess markovDecisionProcess = (MarkovDecisionProcess)
                appContext
                .getBean("markovDecisionProcess");
        markovDecisionProcess.observationP = 0;
        for (int i = 0; i < 10; ++i) {
            curState = (RobotState) startState.copy();
            for (int j = 0; j < 1000; ++j) {
                curState = (RobotState) qLearning.iterateLearning(curState);
                if (curState == null) {
                    break;
                }
            }
        }

        curState = (RobotState) startState.copy();
        curRobotProjection = (RobotProjection) ((RobotProjection) appContext
                .getBean("robotProjection")).copy();

        System.out.println("Ended learning.\n");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TimerBasedAnimation");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

        g2.draw(curRobotProjection.getBodyCoords());

        Color colors[] = {Color.DARK_GRAY, Color.GRAY, Color.GREEN};

        Line2D.Double thigh, shin, foot;
        for (int i = 0; i < curRobotProjection.getRobot().getLegsCount(); ++i) {
            g2.setColor(colors[i]);
            thigh = curRobotProjection.getThighCoords(i);
            shin = curRobotProjection.getShinCoords(i);
            foot = curRobotProjection.getFootCoords(i);
            g2.draw(new Ellipse2D.Double(thigh.x1 - 2, thigh.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x1 - 2, shin.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x2 - 2, shin.y2 - 2, 4, 4));
            g2.draw(thigh);
            g2.draw(shin);
            g2.draw(foot);
        }
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(curRobotProjection.getGround().x1,
                curRobotProjection.getGround().y1 - 2,
                curRobotProjection.getGround().x2,
                curRobotProjection.getGround().y2 - 2));
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);

        System.out.println(curState);
        outputAllPossibleActions(curState);

        RobotAction ra = (RobotAction) ((QLearning) appContext.getBean(
                "qLearning")).argMax(curState);

        if (ra == null) {
            curState = (RobotState) ((RobotState) appContext.getBean("robotState"))
                    .copy();
            curRobotProjection = (RobotProjection) ((RobotProjection) appContext
                    .getBean("robotProjection")).copy();
        } else {

            curRobotProjection.getRobot()
                    .setSupportingLeg(ra.getSupportingLegIndex());
            try {
                curRobotProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            curRobotProjection.getRobot()
                    .setSupportingLeg((ra.getSupportingLegIndex() + 1) % 2);
            try {
                curRobotProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            curRobotProjection.getRobot()
                    .setSupportingLeg(ra.getSupportingLegIndex());
            try {
                curRobotProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            try {
                curRobotProjection.rotateSupportingLeg(
                        ra.getThighRotation(ra.getSupportingLegIndex()),
                        ra.getShinRotation(ra.getSupportingLegIndex()),
                        ra.getShinRotation(
                                ra.getSupportingLegIndex()) - ra
                                .getThighRotation(ra.getSupportingLegIndex()));
            } catch (RobotFallException ignored) {
            }

            for (int i = 0; i < curRobotProjection.getRobot()
                    .getLegsCount(); ++i) {
                if (i != ra.getSupportingLegIndex()) {
                    try {
                        curRobotProjection.rotateRegularLeg(i,
                                ra.getThighRotation(i), ra.getShinRotation(i),
                                ra.getShinRotation(i) - ra.getThighRotation(i));
                    } catch (HitObjectException ignored) {
                    }
                }
            }

            curState.setSupportingLegIndex(ra.getSupportingLegIndex());
            for (int i = 0; i < curState.getLegsCount(); ++i) {
                curState.setThighAngle(i,
                        curRobotProjection.getRobot().getLeg(i).thigh.angle);
                curState.setShinAngle(i,
                        curRobotProjection.getRobot().getLeg(i).shin.angle);
            }
        }

        System.out.println("");

        render(g2);
    }

    void outputAllPossibleActions(RobotState state) {
//        System.out.println("Actions list:");
//        for (Utils.Pair pair : mdp.getActionsList(curState)) {
//            try {
//                System.out.println(
//                        pair.first + " " + qLearning.q.get(curState.hash())
//                                .get(((com.vlfom.steplearn.core.learning
//                                        .general.Action) (pair.first))
//                                        .hash()));
//            } catch (Exception ignored) {
//            }
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            curState = (RobotState) ((RobotState) appContext.getBean("robotState"))
                    .copy();
            curRobotProjection = (RobotProjection) ((RobotProjection) appContext
                    .getBean("robotProjection")).copy();
            System.out.println("Restarting.\n");
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}