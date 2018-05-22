
// A Java program for a Server
import java.net.*;
import java.io.*;

import com.google.protobuf.*;
import com.proto2.message.L1Message.*;
import com.proto2.message.Mybase.*;

import Socket.MachineFactory;

public class DataServer {

    public static void main(String args[]) {
        System.out.println("The data server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(5000);
        try {
            while (true) {
                new Server(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Server extends MachineFactory {
        private Socket mSocket = null;
        private int mclientNumber;
        private BaseMessage MsgOut = null;

        public Server(Socket socket, int clientNumber) {
            mSocket = socket;
            mclientNumber = clientNumber;
        }

        /*
         * private void log(Exception e) { System.out.println("Error handling client# "
         * + mclientNumber + ": " + e); e.printStackTrace(); }
         * 
         * private void log(String message) { System.out.println(message); }
         */

        @Override
        private void Handler(BaseMessage msg) {
            Head headIn = msg.getHead();
            int iMessageType = headIn.getMessageType();

            Head.Builder Head = Head.newBuilder();
            Head.Builder Body = Body.newBuilder();

            if (iMessageType == SEND_DATA_REQUEST) {
                ResponseCode rc = ResponseCode.newBuilder().setRc(0).build();
                SendDataResponse rsp = SendDataResponse.newBuilder().setRc(rc).build();

                Head.setMessageType(SEND_DATA_RESPONSE);
                Body.setExtension(send_data_response, rsp);
            } else if (iMessageType == QUERY_DATA_REQUEST) {
                ResponseCode rc = ResponseCode.newBuilder().setRc(0).build();
                QueryResult.Builder rst = QueryResult.newBuilder();
                rst.setName(msg.getBody().getExtension(query_data_request).getName());
                rst.setQuantity("999");
                rst.setValue(1.23);
                rst.setUnit("m");
                rst.setCount(7);
                QueryDataResponse rsp = QueryDataResponse.newBuilder().setRc(rc).setResult(rst.build()).build();
                Head.setMessageType(QUERY_DATA_RESPONSE);
                Body.setExtension(query_data_response, rsp);
            }
            MsgOut = BaseMessage.newBuilder().setHead(Head).setBody(Body).build();
        }

        /*
         * private void ReadFromPeer() { InputStream in = mSocket.getInputStream();
         * BaseMessage MsgIn = BaseMessage.parseFrom(in); in.close(); Handler(MsgIn); }
         * 
         * private void WriteToPeer() { OutputStream out = mSocket.getOutputStream();
         * MsgOut.writeTo(out); out.close(); }
         */

        @Override
        public void run() {
            try {
                ReadFromPeer();
                WriteToPeer();
            } catch (Exception e) {
                log(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

    }
}