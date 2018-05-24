
// A Java program for a Client
import java.net.*;
import java.io.*;

import com.sample.base.MySocket.*;

public class DataClient {

    public static class Client extends MachineFactory {
        public Client(Socket socket, int clientNumber) { // establish a client connection
            super(socket, clientNumber);
        }

        public void Disconnect(Socket socket) { // called before client terminal close
            socket.close();
            log("Connection closed", mclientNumber);
        }

        public void SetMsgOut(RequestInfo req_info) { // set up request
            mReqInfo = req_info;
            log("Resquest set up: " + mReqInfo.message_type, mclientNumber);
        }

        public RequestInfo GetMsgOut() {
            return mReqInfo;
        }

        public ResponseInfo GetMsgIn() { // get response
            return mRspInfo;
        }

        public void run() {
            try {
                WriteResquest();
                ReadResponse();
            } catch (Exception e) {
                log(e, mclientNumber);
            }
        }
    }
}