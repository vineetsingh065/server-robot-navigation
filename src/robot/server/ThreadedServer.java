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
    
	// public static void main(String args[]) { 
		
//		boolean listening = true;
//        ServerSocket serverSocket = null;
//        
//        // Set up the Server Socket
//        try 
//        {
//            serverSocket = new ServerSocket(portNumber);
//            System.out.println("New Server has started listening on port: " + portNumber );
//        } 
//        catch (IOException e) 
//        {
//            System.out.println("Cannot listen on port: " + portNumber + ", Exception: " + e);
//            System.exit(1);
//        }
//        
//        // Server is now listening for connections or would not get to this point
//        while (listening) // almost infinite loop - loop once for each client request
//        {
//            Socket clientSocket = null;
//            try{
//            	System.out.println("**. Listening for a connection...");
//                clientSocket = serverSocket.accept();
//                System.out.println("00. <- Accepted socket connection from a client: ");
//                System.out.println("    <- with address: " + clientSocket.getInetAddress().toString());
//                System.out.println("    <- and port number: " + clientSocket.getPort());
//            } 
//            catch (IOException e){
//                System.out.println("XX. Accept failed: " + portNumber + e);
//                listening = false;   // end the loop - stop listening for further client requests
//            }	
//            
//            ThreadedConnectionHandler con = new ThreadedConnectionHandler(clientSocket);
//            con.start(); 
//            System.out.println("02. -- Finished communicating with client:" + clientSocket.getInetAddress().toString());
//        }
//        // Server is no longer listening for client connections - time to shut down.
//        try 
//        {
//            System.out.println("04. -- Closing down the server socket gracefully.");
//            serverSocket.close();
//        } 
//        catch (IOException e) 
//        {
//            System.err.println("XX. Could not close server socket. " + e.getMessage());
//        }
//    }
}