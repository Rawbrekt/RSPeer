import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 6969;

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);

        System.out.println("[SERVER] Waiting for client connection...");
        Socket client = listener.accept();
        System.out.println("[SERVER] Connected to client!");

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println("Connected");

        //System.out.println("[SERVER] Closing the client connection!");
        //client.close();
        //listener.close();
    }
}
