import com.sample.base.MySocket.*;
import com.sample.base.MySocket;
import com.sample.base.BinaryHandler;
import com.sample.base.CSVReader.*;
import com.sample.base.CSVReader;

import java.net.*;
import java.io.*;

import java.util.regex.*;
import java.util.stream.DoubleStream;
import java.util.ArrayList;
import java.util.Collections;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(5000);
        try {
            int clientNumber = 0;
            while (true) {
                new Handle(listener.accept(), clientNumber++).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }

    }

    public static class Handle extends Thread {
        private Socket mSocket;
        private int mclientNumber = -1;
        private int mAllowed = -1; // init to -1, begin is 0; end is 1;
        private boolean mDataIncome = false;
        private boolean mFirstQuery = true;
        private String mCSVFile;
        private RequestInfo mReqInfo;
        private ResponseInfo mRspInfo;
        private DataFrame mData;

        private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        public Handle(Socket socket, int clientNumber) {
            mSocket = socket;
            mclientNumber = clientNumber;
            mReqInfo = new RequestInfo();
            mRspInfo = new ResponseInfo();
            log("Client Accepted", mclientNumber);
        }

        private void ReadRequest() {
            try {
                if (!mDataIncome) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()), 4096);
                    String line = in.readLine();
                    log(line, mclientNumber);
                    if (line != null) {
                        ParseMessage(line);
                    }
                } else {
                    // log("Begin recieve data", mclientNumber);
                    BufferedInputStream in = new BufferedInputStream(mSocket.getInputStream());
                    mReqInfo.data = new byte[mReqInfo.data_size + 1];
                    in.read(mReqInfo.data, 0, mReqInfo.data_size);
                    // log(new String(mReqInfo.data, 0, mReqInfo.data_size), mclientNumber);
                }
            } catch (IOException e) {
                // log(e, mclientNumber);
            }
        }

        private void WriteResponse() {

            try {
                PrintWriter output = new PrintWriter(mSocket.getOutputStream(), true);
                output.println("OK");
                output.flush();
                if (mReqInfo.message_type == MessageType.QUERY_DATA_REQUEST) {
                    output.println("RESULT " + mRspInfo.name + " OF " + mRspInfo.quantity + " " + mRspInfo.value + " "
                            + mRspInfo.unit + " FROM " + mRspInfo.count + " POINTS.");
                    output.flush();
                }
                output.flush();
            } catch (Exception e) {
                // log(e, mclientNumber);
            }
            // HandleResponse();
        }

        public void run() {
            try {
                while (mAllowed <= 0) {
                    ReadRequest();
                    Process();
                    WriteResponse();
                }
                mSocket.close();
            } catch (Exception e) {
                // log(e, mclientNumber);
            }

        }

        private void Process() {
            /*
             * if (mReqInfo.message_type == MessageType.CLIENT_BEGIN_REQUEST) { mAllowed =
             * 0; } if (mReqInfo.message_type == MessageType.CLIENT_END_REQUEST) { mAllowed
             * = 1; }
             */
            if (mReqInfo.message_type == MessageType.SEND_DATA_REQUEST)
                if (mReqInfo.message_type == MessageType.SEND_DATA_REQUEST) {
                    if (mDataIncome) {
                        try {
                            DataOutputStream outFile = new DataOutputStream(
                                    new FileOutputStream("client" + "." + mReqInfo.data_type));
                            outFile.write(mReqInfo.data, 0, mReqInfo.data_size);
                            outFile.close();
                        } catch (IOException e) {
                            // log(e, mclientNumber);
                        } finally {
                            mDataIncome = false;
                        }
                    } else {
                        mDataIncome = true;
                    }
                }
            if (mReqInfo.message_type == MessageType.QUERY_DATA_REQUEST) {
                try {
                    if (mFirstQuery) {
                        mCSVFile = BinaryHandler.parseFile("client.BIN", "client.XML");
                        mFirstQuery = false;
                    }
                    mData = CSVReader.read(mCSVFile);
                    QueryData();
                    log("Queryed Data", mclientNumber);
                } catch (Exception e) {
                    // log(e, mclientNumber);
                }

            }
        }

        private void QueryData() {
            if (!mData.name.contains(mReqInfo.op_name)) {
                mRspInfo.unit = null;
                mRspInfo.value = null;
                mRspInfo.name = null;
                mRspInfo.count = -1;
                mRspInfo.quantity = null;
            } else {
                mRspInfo.unit = mData.unit.get(mReqInfo.op_name);
                mRspInfo.name = mReqInfo.op_name;
                mRspInfo.quantity = mData.quantity.get(mReqInfo.op_name);
                mRspInfo.count = mData.value.get(mReqInfo.op_name).size();

                DoubleStream.Builder sBuilder = DoubleStream.builder();
                for (Double e : mData.value.get(mReqInfo.op_name)) {
                    sBuilder.add(e);
                }
                DoubleStream data_piece = sBuilder.build();

                switch (mReqInfo.op_type) {
                case MAX:
                    mRspInfo.value = data_piece.max().getAsDouble();
                    break;
                case MIN:
                    mRspInfo.value = data_piece.min().getAsDouble();
                    break;
                case AVG:
                    mRspInfo.value = data_piece.average().getAsDouble();
                    break;
                case SUM:
                    mRspInfo.value = data_piece.sum();
                    break;
                case MEDIAN:
                    data_piece.sorted();
                    int len = mRspInfo.count;
                    double[] tmp = data_piece.toArray();
                    if (len % 2 == 0)
                        mRspInfo.value = (tmp[len / 2] + tmp[len / 2 + 1]) / 2;
                    else
                        mRspInfo.value = tmp[len / 2];
                    break;
                }
            }
        }

        private void ParseMessage(String line) {

            try {
                Pattern r_begin = Pattern.compile("\\ABEGIN;\\z");
                Pattern r_end = Pattern.compile("\\AEND;\\z");
                Pattern r_size = Pattern.compile("\\ASIZE ([A-Z]{3}) ([0-9]+);\\z");
                Pattern r_query = Pattern.compile("\\AQUERY (MAX|MIN|AVG|SUM|MEDIAN) (.+);\\z");

                Matcher m_begin = r_begin.matcher(line);
                Matcher m_end = r_end.matcher(line);
                Matcher m_size = r_size.matcher(line);
                Matcher m_query = r_query.matcher(line);

                if (m_begin.find()) {
                    log("Message BEGIN parse.", mclientNumber);
                    mReqInfo.message_type = MessageType.CLIENT_BEGIN_REQUEST;
                    mAllowed = 0;
                    // return;
                } else if (m_end.find()) {
                    log("Message END parse.", mclientNumber);
                    mReqInfo.message_type = MessageType.CLIENT_END_REQUEST;
                    mAllowed = 1;
                    // return;
                } else if (m_size.find()) {
                    log("Message SIZE parse.", mclientNumber);
                    mReqInfo.message_type = MessageType.SEND_DATA_REQUEST;
                    mReqInfo.data_type = m_size.group(1);
                    mReqInfo.data_size = Integer.valueOf(m_size.group(2));
                    // return;
                    // mDataIncome = true;

                } else if (m_query.find()) {
                    log("Message QUERY parse.", mclientNumber);
                    mReqInfo.message_type = MessageType.QUERY_DATA_REQUEST;
                    mReqInfo.op_type = OpType.valueOf(m_query.group(1));
                    mReqInfo.op_name = m_query.group(2);
                    // return;
                } else {
                    mReqInfo.message_type = MessageType.UNKNOWN;
                }
            } catch (PatternSyntaxException e) {
                // log(e, mclientNumber);
            }

        }

        public void log(String message, int clientNumber) {
            Date date = new Date();
            System.out.println(sdf.format(date) + " Client#" + clientNumber + ": " + message);
        }

        public void log(Exception e, int clientNumber) {
            Date date = new Date();
            System.out.println(sdf.format(date) + " Error handling client# " + clientNumber + ": " + e);
            e.printStackTrace();
        }
    }

}