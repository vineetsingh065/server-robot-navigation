package robot.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8080;
    private Robot robot;

    public Server(Robot robot) {
        this.robot = robot;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server listening on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            Thread thread = new Thread(() -> {
                try {
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

    private void handleRequest(Socket clientSocket) throws IOException {
        // Read the command from the client
        byte[] buffer = new byte[1024];
        int bytesRead = clientSocket.getInputStream().read(buffer);
        String command = new String(buffer, 0, bytesRead);
        
        // Parse the command
        String[] parts = command.split("\\s+");
        String action = parts[0];
        int distance = Integer.parseInt(parts[1]);
        
        // Perform the appropriate action
        if (action.equals("forward")) {
            robot.moveNorth(distance);
        } else if (action.equals("backward")) {
            robot.moveSouth(distance);
        } else if (action.equals("left")) {
            robot.moveEast(distance);
        } else if (action.equals("right")) {
            robot.moveWest(distance);
        }
        
        // Send a response to the client
        String response = "OK";
        clientSocket.getOutputStream().write(response.getBytes());
        // Close the connection
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException {
        Robot robot = new Robot();
        Server server = new Server(robot);
        server.start();
    }
}
