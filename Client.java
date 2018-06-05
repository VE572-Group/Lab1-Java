import com.sample.base.*;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("35.187.205.210", 5000);
            while (true) {
                Begin(socket);
                TimeUnit.SECONDS.sleep(1);
                Size(socket);
                TimeUnit.SECONDS.sleep(1);
                Query(socket);
                TimeUnit.SECONDS.sleep(1);
                End(socket);
                break;
            }
            TimeUnit.SECONDS.sleep(10);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Query(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("QUERY MAX Ronald_Drump;");
            WaitResponse(socket);
            WaitResponse(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Size(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("SIZE XML 512;");
            WaitResponse(socket);
            DataInputStream inFile = new DataInputStream(new FileInputStream("data/decoding.xml"));
            DataOutputStream outFile = new DataOutputStream(socket.getOutputStream());
            byte[] bytes = new byte[512];
            inFile.read(bytes, 0, 512);
            outFile.write(bytes, 0, 512);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void WaitResponse(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()), 4096);
            String line = in.readLine();
            System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Begin(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("BEGIN;");
            WaitResponse(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void End(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("END;");
            WaitResponse(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}