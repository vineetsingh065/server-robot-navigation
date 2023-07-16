package robot.server;

import java.awt.*;

public class RobotData {
    private String name;
    private int x;
    private int y;
    private Point position;
    private int velocity;
    private Color color;

    public RobotData(String name, int x, int y, int velocity, Color color) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.color = color;
    }

    // Getters and setters for the robot properties

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
    
    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.setLocation(x, y);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
