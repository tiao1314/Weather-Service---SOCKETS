import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private ServerSocket serverSocket;
    private List<String> forecastData;

    public Server() {
        forecastData = new ArrayList<>();
        forecastData.add("Sunny");
        forecastData.add("Cloudy");
        forecastData.add("Rainy");
        forecastData.add("Stormy");
        forecastData.add("Snowy");
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                handleClient(clientSocket);

                clientSocket.close();
                System.out.println("Client disconnected");
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            Scanner in = new Scanner(clientSocket.getInputStream());
            OutputStream os = clientSocket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true);

            while (in.hasNextLine()) {
                String request = in.nextLine();
                System.out.println("Received request: " + request);

                if (request.equals("***CLOSE***")) {
                    System.out.println("Closing client connection");
                    break;
                }

                if (request.startsWith("GETWEATHER%")) {
                    String[] parts = request.split("%");
                    int numDays = Integer.parseInt(parts[1]);

                    LocalDate date = LocalDate.now();
                    out.write("FORECAST%");
                    for (int i = 0; i < numDays; i++) {
                        String forecast = forecastData.get(date.getDayOfMonth() % forecastData.size());
                        out.write(forecast + "%");
                        date = date.plusDays(1);
                    }
                    out.write("\n");
                    out.flush();
                }
            }

            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(50007);
    }
}
