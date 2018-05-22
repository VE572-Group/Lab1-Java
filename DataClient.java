
// A Java program for a Client
import java.net.*;
import java.sql.Struct;
import java.io.*;

import com.google.protobuf.*;
import com.proto2.message.L1Message.*;
import com.proto2.message.MyBase.Head;
import com.proto2.message.Mybase.*;

import MySocket.*;
import javafx.util.converter.ByteStringConverter;

public class DataClient {

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

    public static class Client extends MachineFactory {

        private RequestInfo mReqInfo = null;
        private ResponseInfo mRspInfo = null;

        public Client(String ip, int port) {
            mSocket = new Socket(ip, port);
            while (!mSocket.isConnected()) {
            }
        }

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
        }

        public void SetMsgOut(RequestInfo req_info) {
            mReqInfo = req_info;
        }

        public ResponseInfo GetMsgOut() {
            return mReqInfo;
        }

        public ResponseInfo GetMsgIn() {
            return mRspInfo;
        }
    }
}