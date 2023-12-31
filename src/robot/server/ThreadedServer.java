package robot.server;


import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ThreadedServer 
{
	private static int portNumber = 5050;
	private static int MAX_ROBOTS = 3;
    private static int COLLISION_MARGIN = 10;

    private ServerSocket serverSocket;
    private JTextArea logTextArea;
    private JPanel robotsPanel;
    private JLabel selectedRobotLabel;
    private ArrayList<RobotData> connectedRobots;
    
    public ThreadedServer() {
    	connectedRobots = new ArrayList<>();
        initializeGUI();
        startServer();
    }
    
    private void initializeGUI() {
        JFrame frame = new JFrame("Graphical Display Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Log panel
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        frame.add(logScrollPane, BorderLayout.CENTER);

        // Robots panel
        robotsPanel = new JPanel();
        robotsPanel.setLayout(new GridBagLayout());
        frame.add(robotsPanel, BorderLayout.WEST);

        // Selected robot label
        selectedRobotLabel = new JLabel("Selected Robot: ");
        frame.add(selectedRobotLabel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    
    private void startServer() {
        try {
            serverSocket = new ServerSocket(portNumber);
            logMessage("Server started on port: " + portNumber);
            logMessage("Waiting for connections...");
            logMessage("Waiting..."+ serverSocket.getLocalSocketAddress());
            logMessage("Waiting..."+ serverSocket.getLocalPort());
            

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress());
                logMessage("Accepted socket connection from client: " + clientSocket.getInetAddress());

                if (connectedRobots.size() >= MAX_ROBOTS) {
                    sendMaxRobotLimitReachedMessage(clientSocket);
                    clientSocket.close();
                    logMessage("Connection rejected. Maximum robot limit reached.");
                } else {
                    ThreadedConnectionHandler con = new ThreadedConnectionHandler(clientSocket);
                    con.start();
                }
            }
        } catch (IOException e) {
            logMessage("Server error: " + e.getMessage());
        }
    }
    
    private void sendMaxRobotLimitReachedMessage(Socket clientSocket) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
            os.writeObject("Max robot limit reached. Connection rejected.");
            os.flush();
        } catch (IOException e) {
            logMessage("Error sending max robot limit reached message: " + e.getMessage());
        }
    }

    private void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logTextArea.append(message + "\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        });
    }
    
    public synchronized void addRobot(RobotData robotData) {
        SwingUtilities.invokeLater(() -> {
            RobotPanel robotPanel = new RobotPanel(robotData);
            robotsPanel.add(robotPanel);
            robotsPanel.revalidate();
            robotsPanel.repaint();
        });
    }
    
    public synchronized void removeRobot(RobotData robotData) {
        SwingUtilities.invokeLater(() -> {
            Component[] components = robotsPanel.getComponents();
            for (Component component : components) {
                if (component instanceof RobotPanel) {
                    RobotPanel robotPanel = (RobotPanel) component;
                    if (robotPanel.getRobotData().equals(robotData)) {
                        robotsPanel.remove(robotPanel);
                        robotsPanel.revalidate();
                        robotsPanel.repaint();
                        break;
                    }
                }
            }
        });
    }
    
    public synchronized void updateSelectedRobot(RobotData robotData) {
        SwingUtilities.invokeLater(() -> {
            selectedRobotLabel.setText("Selected Robot (selected):" + robotData.getName());
        });
    }
    
    public synchronized void updateRobotPosition(RobotData robotData) {
        SwingUtilities.invokeLater(() -> {
            Component[] components = robotsPanel.getComponents();
            for (Component component : components) {
                if (component instanceof RobotPanel) {
                    RobotPanel robotPanel = (RobotPanel) component;
                    if (robotPanel.getRobotData().equals(robotData)) {
                        robotPanel.updatePosition(robotData.getPosition());
                        break;
                    }
                }
            }
        });
    }

    public synchronized void logCollision(RobotData robot1, RobotData robot2) {
        SwingUtilities.invokeLater(() -> {
            logMessage("Collision detected between Robot " + robot1.getName() + " and Robot " + robot2.getName());
        });
    }
    
    public static void main(String[] args) {
        new ThreadedServer();
    }
    
	
