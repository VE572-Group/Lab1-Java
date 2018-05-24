
package com.sample.base;

// A Java program for a Server
import java.net.*;
import java.io.*;

import com.sample.base.MySocket.*;

public final class MyServer extends MachineFactory {

    /*
     * public static void main(String args[]) {
     * System.out.println("The data server is running."); int clientNumber = 0;
     * ServerSocket listener = new ServerSocket(5000); try { while (true) { new
     * Server(listener.accept(), clientNumber++).run(); } } finally {
     * listener.close(); } }
     */
    // private static class Server extends MachineFactory {
    // private Socket mSocket = null;
    // private BaseMessage MsgOut = null;

    public MyServer(Socket socket, int clientNumber) {
        super(socket, clientNumber);
    }

    public void run() {
        try {
            while (mAllowed <= 0) {
                ReadRequest();
                WriteResponse();
            }
        } catch (Exception e) {
            // log(e, mclientNumber);
            e.printStackTrace();
        } finally {
            try {
                mSocket.close();
            } catch (IOException e) {
                // log(e, mclientNumber);
                e.printStackTrace();
            }
            // log("Connection closed", mclientNumber);
        }
    }

    protected void HandleRequest() {
        MessageType iMessageType = mReqInfo.message_type;
        if (iMessageType == MessageType.SEND_DATA_REQUEST) {
            try {
                FileOutputStream out = new FileOutputStream("validate." + mReqInfo.data_type);
                out.write(mReqInfo.data);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (iMessageType == MessageType.QUERY_DATA_REQUEST) {
            // to do interface
        }
        if (iMessageType == MessageType.CLIENT_BEGIN_REQUEST) {
            mAllowed = 0;
        }
        if (iMessageType == MessageType.CLIENT_END_REQUEST) {
            mAllowed = 1;
        }
    }

    protected void HandleResponse() {
        MessageType iMessageType = mReqInfo.message_type;

        if (iMessageType == MessageType.CLIENT_BEGIN_REQUEST) {
            mRspInfo.rc = 0;
            mRspInfo.message_type = MessageType.CLIENT_BEGIN_RESPONSE;
        }
        if (iMessageType == MessageType.CLIENT_END_REQUEST) {
            mRspInfo.rc = 0;
            mRspInfo.message_type = MessageType.CLIENT_END_RESPONSE;
        }
        if (iMessageType == MessageType.SEND_DATA_REQUEST) {
            mRspInfo.rc = 0;
            mRspInfo.message_type = MessageType.SEND_DATA_RESPONSE;
        }
        if (iMessageType == MessageType.QUERY_DATA_REQUEST) {
            mRspInfo.message_type = MessageType.QUERY_DATA_RESPONSE;
        }
    }

    // }
}