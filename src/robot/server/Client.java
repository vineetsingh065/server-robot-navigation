package robot.server;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOSTNAME, PORT);
        System.out.println("Connected to server: " + socket.getInetAddress());

        // Send a command to the server
        String command = "forward 10";
        socket.getOutputStream().write(command.getBytes());

        // Read the response from the server
        byte[] buffer = new byte[1024];
        int bytesRead = socket.getInputStream().read(buffer);
        String response = new String(buffer, 0, bytesRead);
        System.out.println("Response: " + response);

        // Close the connection
        socket.close();
    }
}
