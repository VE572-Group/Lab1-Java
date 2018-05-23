import java.net.*;
import java.io.*;

import com.google.protobuf.*;
import com.proto2.message.L1Message.*;
import com.proto2.message.Mybase.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MySocket {

    private String logfile = "L1.log";
    private PrintWriter print_writer = null;
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public void OpenLog() throws IOException {
        print_writer = new PrintWriter(new FileWriter(logfile));
    }

    public void CloseLog() throws IOException {
        print_writer.close();
    }

    public void log(String message, int clientNumber) {
        Date date = new Date();
        print_writer.println(sdf.format(date) + " Client#" + clientNumber + ": " + message);
    }

    public void log(Exception e, int clientNumber) {
        Date date = new Date();
        print_writer.println(sdf.format(date) + " Error handling client# " + clientNumber + ": " + e);
    }

    public class SocketEndException extends Exception {
        static final long serialVersionUID = 7818375838146190175L;
    }

    public static class MachineFactory extends Thread {
        private Socket mSocket = null;
        private int mclientNumber;
        private BaseMessage MsgOut = null;
        private BaseMessage MsgIn = null;

        public void ReadFromPeer() {
            InputStream in = mSocket.getInputStream();
            while (in.available() == 0) {
            }
            MsgIn = BaseMessage.parseFrom(in);
            // in.close();
            HandlerIn();
        }

        public void WriteToPeer() {
            HandlerOut();
            OutputStream out = mSocket.getOutputStream();
            MsgOut.writeTo(out);
            // out.close();
        }

        private void HandlerIn() {
        }

        private void HandlerOut() {
        }
    }

}