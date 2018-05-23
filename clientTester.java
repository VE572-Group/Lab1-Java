
// A program to test the socket client
import java.net.*;
import java.sql.Struct;
import java.io.*;

import com.google.protobuf.*;
import com.proto2.message.L1Message.*;
import com.proto2.message.MyBase.Head;
import com.proto2.message.Mybase.*;

import MySocket.*;

public class clientTester {
    public static void main(Stirng[] args) {
        try {
            System.out.println("The data server is running.");
            int clientNumber = 0;
            Socket socket = new Socket(5000);
            // ServerSocket listener = new ServerSocket(5000);
            // try {
            //     while (true) {
            //         new Server(listener.accept(), clientNumber++).run();
            //     }
            // } finally {
            //     listener.close();
            // }
            DataClient.Client dc = new DataClient.Client(socket, clientNumber);
            dc.run();
            dc.Disconnect(socket);
        }   catch (UnknownHostException e) {
			e.printStackTrace();
        }
    }
}
