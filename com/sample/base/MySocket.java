package com.sample.base;

import java.net.*;
import java.io.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

public class MySocket {

    private String logfile = "L1.log";
    private PrintWriter print_writer = null;
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public enum MessageType {
        CLIENT_BEGIN_REQUEST(200), CLIENT_BEGIN_RESPONSE(201), CLIENT_END_REQUEST(202), CLIENT_END_RESPONSE(203),
        SEND_DATA_REQUEST(204), SEND_DATA_RESPONSE(205), QUERY_DATA_REQUEST(206), QUERY_DATA_RESPONSE(207)
    }

    public enum OpType {
        MAX(1), MIN(2), AVG(3), SUM(4), MEDIAN(5)
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

    public void OpenLog() throws IOException {
        print_writer = new PrintWriter(new FileWriter(logfile));
    }

    public void CloseLog() throws IOException {
        print_writer.close();
    }

    public void log(String message, int clientNumber) {
        Date date = new Date();
        System.out.println(sdf.format(date) + " Client#" + clientNumber + ": " + message);
    }

    public void log(Exception e, int clientNumber) {
        Date date = new Date();
        System.out..println(sdf.format(date) + " Error handling client# " + clientNumber + ": " + e);
    }

    public static class MachineFactory {
        protected Socket mSocket;
        protected int mclientNumber = -1;
        // protected BaseMessage MsgOut = null;
        // protected BaseMessage MsgIn = null;
        protected RequestInfo mReqInfo;
        protected ResponseInfo mRspInfo;
        protected int mAllowed = -1; // init to -1, begin is 0; end is 1;

        public MachineFactory(Socket socket, int clientNumber) {
            mSocket = socket;
            mclientNumber = clientNumber;
            mReqInfo = new RequestInfo();
            mRspInfo = new ResponseInfo();
        }

        public void run() {
        }

        protected void ReadRequest() {
            ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
            while (in.available() == 0) {
            }
            mReqInfo = (RequestInfo) in.readObject();
            HandleRequest();
        }

        protected void WriteResquest() {
            HandleRequest();
            ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());
            out.writeObject(mReqInfo);
        }

        protected void ReadResponse() {
            ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
            while (in.available() == 0) {
            }
            mRspInfo = (ResponseInfo) in.readObject();
            HandleResponse();
        }

        protected void WriteResponse() {
            HandleResponse();
            ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());
            out.writeObject(mRspInfo);
        }

        protected void HandleRequest() {
        }

        protected void HandleResponse() {
        }
    }

}