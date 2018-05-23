package sample;

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

    public static class RequestInfo {
        public int message_type = null;
        public int data_size = null;
        public int data_type = null;
        public ByteString data = null;
        public String op_name = null;
        public OpType op_type = null;
    }

    public static class ResponseInfo {
        public int message_type = null;
        public int rc = null;
        public String name = null;
        public String quantity = null;
        public ByteString value = null;
        public String unit = null;
        public int count = null;
    }

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

    public static class MachineFactory {
        private Socket mSocket = null;
        private int mclientNumber = -1;
        private BaseMessage MsgOut = null;
        private BaseMessage MsgIn = null;
        private RequestInfo mReqInfo = null;
        private ResponseInfo mRspInfo = null;
        private int mAllowed = -1; // init to -1, begin is 0; end is 1;

        public MachineFactory(Socket socket, int clientNumber) {
            mSocket = socket;
            mclientNumber = clientNumber;
            mReqInfo = new RequestInfo();
            mRspInfo = new ResponseInfo();
        }

        public void run() {
        }

        private void ReadFromPeer() {
            InputStream in = mSocket.getInputStream();
            while (in.available() == 0) {
            }
            MsgIn = BaseMessage.parseFrom(in);
            // in.close();
            HandlerIn();
        }

        private void WriteToPeer() {
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