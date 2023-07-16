package robot.server;

import javax.swing.*;
import java.awt.*;

public class RobotPanel extends JPanel {
    private RobotData robotData;

    public RobotPanel(RobotData robotData) {
        this.robotData = robotData;
        setPreferredSize(new Dimension(50, 50));
        setBackground(robotData.getColor());
    }

    public RobotData getRobotData() {
        return robotData;
    }

    public void updatePosition(Point position) {
        robotData.setX(position.x);
        robotData.setY(position.y);
        setLocation(position);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillOval(10, 10, getWidth() - 20, getHeight() - 20);
        g.setColor(Color.BLACK);
        g.drawString(robotData.getName(), 10, getHeight() / 2);
    }
}

