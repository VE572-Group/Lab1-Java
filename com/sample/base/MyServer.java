
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
    private Socket mSocket = null;
    private BaseMessage MsgOut = null;

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
            log(e, mclientNumber);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log(e, mclientNumber);
            }
            // log("Connection closed", mclientNumber);
        }
    }

    protected void HandleRequest() {
        int iMessageType = mReqInfo.message_type;
        if (iMessageType == SEND_DATA_REQUEST) {
            try {
                FileOutputStream out = new FileOutputStream("validate." + mReqInfo.data_type);
                out.write(mReqInfo.data);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (iMessageType == QUERY_DATA_REQUEST) {
            // to do interface
        }
        if (iMessageType == CLIENT_BEGIN_REQUEST) {
            mAllowed = 0;
        }
        if (iMessageType == CLIENT_END_REQUEST) {
            mAllowed = 1;
        }
    }

    protected void HandleResponse() {
        int iMessageType = mReqInfo.message_type;

        if (iMessageType == CLIENT_BEGIN_REQUEST) {
            mRspInfo.rc = 0;
            mRspInfo.message_type = MessageType.CLIENT_BEGIN_RESPONSE;
        }
        if (iMessageType == CLIENT_END_REQUEST) {
            mRspInfo.rc = 0;
            mRspInfo.message_type = MessageType.CLIENT_END_RESPONSE;
        }
        if (iMessageType == SEND_DATA_REQUEST) {
            mRspInfo.rc = 0;
            mRspInfo.message_type = MessageType.SEND_DATA_RESPONSE;
        }
        if (iMessageType == QUERY_DATA_REQUEST) {
            mRspInfo.message_type = MessageType.QUERY_DATA_RESPONSE;
        }
    }

    // }
}