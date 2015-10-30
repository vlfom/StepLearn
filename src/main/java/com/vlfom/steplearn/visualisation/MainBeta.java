package com.vlfom.steplearn.visualisation;

import com.vlfom.steplearn.core.learning.QLearningRobotModel;
import com.vlfom.steplearn.core.learning.robot.RobotState;
import com.vlfom.steplearn.spring.config.BasicConfiguration;
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
    private QLearningRobotModel qLearningRobotModel;

    public MainBeta() {
        this.addKeyListener(new MyKeyListener());

        Timer timer = new Timer(50, this);
        timer.setInitialDelay(190);
        timer.start();

        appContext = new AnnotationConfigApplicationContext(
                BasicConfiguration.class);

        qLearningRobotModel = (QLearningRobotModel) appContext.getBean
                ("qLearningRobotModel");
        qLearningRobotModel.iterateLearning(100, 100);
        qLearningRobotModel.calibrateLearning(100, 100);
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

        g2.draw(qLearningRobotModel.getCurrentProjection().getBodyCoords());

        Color colors[] = {Color.DARK_GRAY, Color.GRAY, Color.GREEN};

        Line2D.Double thigh, shin, foot;
        for (int i = 0; i < qLearningRobotModel.getCurrentProjection()
                .getRobot()
                .getLegsCount(); ++i) {
            g2.setColor(colors[i]);
            thigh = qLearningRobotModel.getCurrentProjection()
                    .getThighCoords(i);
            shin = qLearningRobotModel.getCurrentProjection().getShinCoords(i);
            foot = qLearningRobotModel.getCurrentProjection().getFootCoords(i);
            g2.draw(new Ellipse2D.Double(thigh.x1 - 2, thigh.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x1 - 2, shin.y1 - 2, 4, 4));
            g2.draw(new Ellipse2D.Double(shin.x2 - 2, shin.y2 - 2, 4, 4));
            g2.draw(thigh);
            g2.draw(shin);
            g2.draw(foot);
        }
        g2.setColor(Color.BLACK);
        g2.draw(new Line2D.Double(
                qLearningRobotModel.getCurrentProjection().getGround().x1,
                qLearningRobotModel.getCurrentProjection().getGround().y1 - 2,
                qLearningRobotModel.getCurrentProjection().getGround().x2,
                qLearningRobotModel.getCurrentProjection().getGround().y2 - 2));
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

        qLearningRobotModel.makeStep();

        render(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    class MyKeyListener implements KeyListener {
        public void keyPressed(KeyEvent e) {
            qLearningRobotModel.resetModel();
            System.out.println("Restarting.\n");
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}