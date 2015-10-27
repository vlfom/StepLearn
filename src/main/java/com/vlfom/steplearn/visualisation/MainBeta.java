package com.vlfom.steplearn.visualisation;

import com.vlfom.steplearn.core.robot.draw.RobotProjection;
import com.vlfom.steplearn.core.robot.exceptions.HitObjectException;
import com.vlfom.steplearn.core.robot.exceptions.RobotFallException;
import com.vlfom.steplearn.core.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.core.learning.general.QLearning;
import com.vlfom.steplearn.core.learning.robot.RobotAction;
import com.vlfom.steplearn.core.learning.robot.RobotMarkovDecisionProcess;
import com.vlfom.steplearn.core.learning.robot.RobotState;
import com.vlfom.steplearn.core.robot.Robot;
import com.vlfom.steplearn.core.util.Utils;
import com.vlfom.steplearn.spring.config.RobotConfiguration;
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
import java.util.ArrayList;

public class MainBeta extends JPanel implements ActionListener {
    public RobotState start, state;
    Timer timer;
    MarkovDecisionProcess mdp;
    QLearning qLearning;
    RobotProjection robotProjection, startRobotProjection;

    public MainBeta() {
        this.addKeyListener(new MyKeyListener());

        timer = new Timer(50, this);
        timer.setInitialDelay(190);
        timer.start();

        //ClassPathXmlApplicationContext context = new
        //        ClassPathXmlApplicationContext("robot-config.xml");

        AnnotationConfigApplicationContext context = new
                AnnotationConfigApplicationContext(
                RobotConfiguration.class);

        Robot robot = (Robot) context.getBean("robot");

        startRobotProjection = new RobotProjection(robot,
                new Line2D.Double(0, -1, 700, -1));
        try {
            startRobotProjection.reInitialize();
        } catch (HitObjectException ignored) {
        }

        mdp = new RobotMarkovDecisionProcess(
                (RobotProjection) startRobotProjection.copy());
        qLearning = new QLearning(mdp);

        ArrayList<Integer> thighAngle = new ArrayList<>(robot.getLegsCount());
        ArrayList<Integer> shinAngle = new ArrayList<>(robot.getLegsCount());
        for (int i = 0; i < robot.getLegsCount(); ++i) {
            thighAngle.add(robot.getLeg(i).thigh.angle);
            shinAngle.add(robot.getLeg(i).shin.angle);
        }
        start = new RobotState(robot.getLegsCount(), robot.supportingLegIndex,
                thighAngle, shinAngle);

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
        mdp.observationP = 0;
        for (int i = 0; i < 10; ++i) {
            state = (RobotState) start.copy();
            for (int j = 0; j < 1000; ++j) {
                state = (RobotState) qLearning.iterateLearning(state);
                if (state == null) {
                    break;
                }
            }
        }

        state = (RobotState) start.copy();
        robotProjection = (RobotProjection) startRobotProjection.copy();

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

        g2.draw(robotProjection.getBodyCoords());

        Color colors[] = {Color.DARK_GRAY, Color.GRAY, Color.GREEN};

        Line2D.Double thigh, shin, foot;
        for (int i = 0; i < robotProjection.getRobot().getLegsCount(); ++i) {
            g2.setColor(colors[i]);
            thigh = robotProjection.getThighCoords(i);
            shin = robotProjection.getShinCoords(i);
            foot = robotProjection.getFootCoords(i);
            g2.draw(new Ellipse2D.Double(thigh.x1 - 2, thigh.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x1 - 2, shin.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x2 - 2, shin.y2 - 2, 4, 4));
            g2.draw(thigh);
            g2.draw(shin);
            g2.draw(foot);
        }
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(robotProjection.getGround().x1,
                robotProjection.getGround().y1 - 2,
                robotProjection.getGround().x2,
                robotProjection.getGround().y2 - 2));
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

        System.out.println(state);
        outputAllPossibleActions(state);

        RobotAction ra = (RobotAction) qLearning.argMax(state);

        if (ra == null) {
            state = (RobotState) start.copy();
            robotProjection = (RobotProjection) startRobotProjection.copy();
        } else {

            robotProjection.getRobot()
                    .setSupportingLeg(ra.getSupportingLegIndex());
            try {
                robotProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            robotProjection.getRobot()
                    .setSupportingLeg((ra.getSupportingLegIndex() + 1) % 2);
            try {
                robotProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            robotProjection.getRobot()
                    .setSupportingLeg(ra.getSupportingLegIndex());
            try {
                robotProjection.reInitialize();
            } catch (HitObjectException ignored) {
            }

            try {
                robotProjection.rotateSupportingLeg(
                        ra.getThighRotation(ra.getSupportingLegIndex()),
                        ra.getShinRotation(ra.getSupportingLegIndex()),
                        ra.getShinRotation(
                                ra.getSupportingLegIndex()) - ra
                                .getThighRotation(ra.getSupportingLegIndex()));
            } catch (RobotFallException ignored) {
            }

            for (int i = 0; i < robotProjection.getRobot()
                    .getLegsCount(); ++i) {
                if (i != ra.getSupportingLegIndex()) {
                    try {
                        robotProjection.rotateRegularLeg(i,
                                ra.getThighRotation(i), ra.getShinRotation(i),
                                ra.getShinRotation(i) - ra.getThighRotation(i));
                    } catch (HitObjectException ignored) {
                    }
                }
            }

            state.setSupportingLegIndex(ra.getSupportingLegIndex());
            for (int i = 0; i < state.getLegsCount(); ++i) {
                state.setThighAngle(i,
                        robotProjection.getRobot().getLeg(i).thigh.angle);
                state.setShinAngle(i,
                        robotProjection.getRobot().getLeg(i).shin.angle);
            }
        }

        System.out.println("");

        render(g2);
    }

    void outputAllPossibleActions(RobotState state) {
        System.out.println("Actions list:");
        for (Utils.Pair pair : mdp.getActionsList(state)) {
            try {
                System.out.println(
                        pair.first + " " + qLearning.q.get(state.hash())
                                .get(((com.vlfom.steplearn.core.learning
                                        .general.Action) (pair.first))
                                        .hash()));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            state = (RobotState) start.copy();
            robotProjection = (RobotProjection) startRobotProjection.copy();
            System.out.println("Restarting.\n");
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}