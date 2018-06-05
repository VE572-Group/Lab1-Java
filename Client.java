import com.sample.base.*;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) {
        try {
            // Socket socket = new Socket("35.187.205.210", 5000);
            Socket socket = new Socket("localhost", 5000);
            while (true) {
                Begin(socket);
                TimeUnit.SECONDS.sleep(1);
                Size(socket);
                TimeUnit.SECONDS.sleep(1);
                SizeBIN(socket);
                TimeUnit.SECONDS.sleep(1);
                Query(socket);
                TimeUnit.SECONDS.sleep(1);
                Query2(socket);
                TimeUnit.SECONDS.sleep(1);
                End(socket);
                break;
            }
            TimeUnit.SECONDS.sleep(1);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Query(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("QUERY MAX CHANNEL05;");
            WaitResponse(socket);
            // TimeUnit.SECONDS.sleep(5);
            WaitResponse(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Query2(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("QUERY MAX CHANNEL03;");
            WaitResponse(socket);
            // TimeUnit.SECONDS.sleep(5);
            WaitResponse(socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Size(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("SIZE XML 17055;");
            WaitResponse(socket);
            DataInputStream inFile = new DataInputStream(new FileInputStream("decoding.xml"));
            DataOutputStream outFile = new DataOutputStream(socket.getOutputStream());
            byte[] bytes = new byte[17055];
            inFile.read(bytes, 0, 17055);
            outFile.write(bytes, 0, 17055);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SizeBIN(Socket socket) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("SIZE BIN 252084;");
            WaitResponse(socket);
            DataInputStream inFile = new DataInputStream(new FileInputStream("data_1.bin"));
            DataOutputStream outFile = new DataOutputStream(socket.getOutputStream());
            byte[] bytes = new byte[252084];
            inFile.read(bytes, 0, 252084);
            outFile.write(bytes, 0, 252084);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void WaitResponse(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) == null) {
            }
            // String line = in.readLine();
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