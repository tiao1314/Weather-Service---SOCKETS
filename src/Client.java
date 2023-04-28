import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 50007;
  

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Connected to server at " + socket.getRemoteSocketAddress());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // generating number of 1-7 for the requested days to check

            int numDays = new Random().nextInt(7) + 1;
            String request = "GETWEATHER%" + numDays;
            out.println(request);

            System.out.println("===================================================");
            
            Scanner inStream = new Scanner(socket.getInputStream());
            String response = inStream.nextLine();

            if (response.startsWith("FORECAST%")) {
                String[] forecast = response.substring(9).split("%");
                for (String day : forecast) {
                    System.out.println(day);
                }
            } else {
                System.out.println("Error: " + response);
            }
            System.out.println("===================================================");
            System.out.println("Auto Generated Amount of days for Request : " + numDays  );
            out.println("***CLOSE***");
            out.close();
            inStream.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            in.close();
        }
    }
}
