package robot.server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Robot {
    private int x; // X-coordinate of the robot's position
    private int y; // Y-coordinate of the robot's position

    public Robot() {
        this.x = 0;
        this.y = 0;
    }

    public boolean move(String direction, int distance) {
        switch (direction) {
            case "north":
                return moveNorth(distance);
            case "south":
                return moveSouth(distance);
            case "east":
                return moveEast(distance);
            case "west":
                return moveWest(distance);
            default:
                return false; // Invalid direction
        }
    }
    
    boolean checkObstacles(int newX, int newY) {
        // List of obstacle positions
        List<Point> obstaclePositions = new ArrayList<>();
        obstaclePositions.add(new Point(2, 3)); // Example obstacle position
        obstaclePositions.add(new Point(5, 7)); // Example obstacle position

        // Check if the new coordinates match any obstacle position
        for (Point obstaclePosition : obstaclePositions) {
            if (obstaclePosition.x == newX && obstaclePosition.y == newY) {
                // Obstacle present at the new coordinates
                return true;
            }
        }

        // No obstacle found at the new coordinates
        return false;
    }

    
    boolean isValidMove(int newX, int newY) {
        // Check if the new coordinates are within the boundaries of the grid
        int gridWidth = 10; // Width of the grid
        int gridHeight = 10; // Height of the grid

        if (newX < 0 || newX >= gridWidth || newY < 0 || newY >= gridHeight) {
            // The new coordinates are outside the boundaries of the grid
            return false;
        }

        // Check if there are any obstacles at the new coordinates
        boolean obstaclePresent = checkObstacles(newX, newY); // Implement the logic to check obstacles

        if (obstaclePresent) {
            // An obstacle is present at the new coordinates
            return false;
        }

        // The move is valid if no boundary or obstacle is violated
        return true;
    }


    boolean moveNorth(int distance) {
    	int newX = x; // Store the current X-coordinate
        int newY = y + distance;
        // Check if the movement will go out of bounds or collide with an obstacle
        if (isValidMove(newX, newY)) {
            y = newY; // Update the robot's position
            return true;
        }
        return false;
    }

    boolean moveSouth(int distance) {
    	int newX = x;
    	int newY = y - distance;
        if (isValidMove(newX, newY)) {
            y = newY;
            return true;
        }
        return false;
    }

    boolean moveEast(int distance) {
        int newX = x + distance;
        int newY = y;
        if (isValidMove(newX, newY)) {
            x = newX;
            return true;
        }
        return false;
    }

    boolean moveWest(int distance) {
        int newX = x - distance;
        int newY = y;
        if (isValidMove(newX, newY)) {
            x = newX;
            return true;
        }
        return false;
    }

}
