package com.sample.base;

import java.net.*;
import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

public class MySocket {

    public enum MessageType {
        CLIENT_BEGIN_REQUEST, CLIENT_BEGIN_RESPONSE, CLIENT_END_REQUEST, CLIENT_END_RESPONSE, SEND_DATA_REQUEST,
        SEND_DATA_RESPONSE, QUERY_DATA_REQUEST, QUERY_DATA_RESPONSE, UNKNOWN;
    }

    public enum OpType {
        MAX, MIN, AVG, SUM, MEDIAN;
    }

    public static class RequestInfo {
        public MessageType message_type;
        public int data_size;
        public String data_type;
        public byte[] data;
        public String op_name;
        public OpType op_type;
    }

    public static class ResponseInfo {
        public MessageType message_type;
        public int rc;
        public String name;
        public String quantity;
        public byte[] value;
        public String unit;
        public int count;
    }

    /*
     * public static class MachineFactory { protected Socket mSocket; protected int
     * mclientNumber = -1; // protected BaseMessage MsgOut = null; // protected
     * BaseMessage MsgIn = null; protected RequestInfo mReqInfo; protected
     * ResponseInfo mRspInfo; protected int mAllowed = -1; // init to -1, begin is
     * 0; end is 1;
     * 
     * public MachineFactory(Socket socket, int clientNumber) { mSocket = socket;
     * mclientNumber = clientNumber; mReqInfo = new RequestInfo(); mRspInfo = new
     * ResponseInfo(); }
     * 
     * public void run() { }
     * 
     * protected void ReadRequest() { ObjectInputStream in = new
     * ObjectInputStream(mSocket.getInputStream()); while (in.available() == 0) { }
     * mReqInfo = (RequestInfo) in.readObject(); HandleRequest(); }
     * 
     * protected void WriteResquest() { HandleRequest(); ObjectOutputStream out =
     * new ObjectOutputStream(mSocket.getOutputStream()); out.writeObject(mReqInfo);
     * }
     * 
     * protected void ReadResponse() { ObjectInputStream in = new
     * ObjectInputStream(mSocket.getInputStream()); while (in.available() == 0) { }
     * mRspInfo = (ResponseInfo) in.readObject(); HandleResponse(); }
     * 
     * protected void WriteResponse() { HandleResponse(); ObjectOutputStream out =
     * new ObjectOutputStream(mSocket.getOutputStream()); out.writeObject(mRspInfo);
     * }
     * 
     * protected void HandleRequest() { }
     * 
     * protected void HandleResponse() { } }
     */
}