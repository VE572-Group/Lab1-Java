import java.net.*;
import java.io.*;

import com.google.protobuf.*;
import com.proto2.message.L1Message.*;
import com.proto2.message.Mybase.*;

public class MySocket {

    public void log(String message) {
        System.out.println(message);
    }

    public class SocketEndException extends Exception {
        static final long serialVersionUID = 7818375838146190175L;
    }

    public static class MachineFactory extends Thread {
        private Socket mSocket = null;
        private int mclientNumber;
        private BaseMessage MsgOut = null;
        private BaseMessage MsgIn = null;

        private void log(Exception e) {
            System.out.println("Error handling client# " + mclientNumber + ": " + e);
            e.printStackTrace();
        }

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