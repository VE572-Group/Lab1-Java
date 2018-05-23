
// A Java program for a Client
import java.net.*;
import java.sql.Struct;
import java.io.*;

import com.google.protobuf.*;
import com.proto2.message.L1Message.*;
import com.proto2.message.MyBase.Head;
import com.proto2.message.Mybase.*;

import MySocket.*;

public class DataClient {

    public static class Client extends MachineFactory {

        // private RequestInfo mReqInfo = null;
        // private ResponseInfo mRspInfo = null;

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
                WriteToPeer();
                ReadFromPeer();
            } catch (Exception e) {
                log(e, mclientNumber);
            }
        }

        /*
         * public void ReadFromPeer() { // client receive response from server
         * InputStream in = mSocket.getInputStream(); while (in.available() == 0) { }
         * MsgIn = BaseMessage.parseFrom(in); HandlerIn(); }
         * 
         * public void WriteToPeer() { // client send request to server HandlerOut();
         * OutputStream out = mSocket.getOutputStream(); MsgOut.writeTo(out); //
         * out.close(); }
         */
        private void HandlerIn() {
            int iMessageType = MsgIn.getHead().getMessageType();
            Body body = MsgIn.getBody();
            mRspInfo.message_type = iMessageType;

            switch (iMessageType) {
            case CLIENT_BEGIN_RESPONSE:
                mRspInfo.rc = body.getExtension(client_begin_response).getRc().getRc();
                break;
            case CLIENT_END_RESPONSE:
                mRspInfo.rc = body.getExtension(client_end_response).getRc().getRc();
                break;
            case SEND_DATA_RESPONSE:
                mRspInfo.rc = body.getExtension(send_data_response).getRc().getRc();
                break;
            case QUERY_DATA_RESPONSE:
                mRspInfo.rc = body.getExtension(query_data_response).getRc().getRc();
                QueryResult rst = body.getExtension(query_data_response).getResult();
                mRspInfo.name = rst.getName();
                mRspInfo.quantity = rst.getQuantity();
                mRspInfo.value = rst.getValue();
                mRspInfo.unit = rst.getUnit();
                mRspInfo.count = rst.getCount();
                break;
            }

            log("Response recieved: " + iMessageType, mclientNumber);

        }

        private void HandlerOut() {
            Head.Builder head = Head.newBuilder();
            Head.Builder body = Body.newBuilder();

            head.setMessageType(mReqInfo.message_type);

            if (mReqInfo.message_type == SEND_DATA_REQUEST) {
                SendDataRequest req = SendDataRequest.newBuilder().setSize(mReqInfo.data_size)
                        .setType(mReqInfo.data_type).setData(mReqInfo.data).build();
                body.setExtension(send_data_request, req);
            }

            if (mReqInfo.message_type == QUERY_DATA_REQUEST) {
                QueryDataRequest req = QueryDataRequest.newBuilder().setName(mReqInfo.op_name).setType(mReqInfo.op_type)
                        .build();
                body.setExtension(query_data_request, req);
            }
            MsgOut = BaseMessage.newBuilder().setHead(head).setBody(body).build();
            log("Request sent: " + mReqInfo.message_type, mclientNumber);
        }

    }
}