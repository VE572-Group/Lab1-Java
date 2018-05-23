
// A Java program for a Server
import java.net.*;
import java.io.*;

import com.google.protobuf.*;
import com.proto2.message.L1Message.*;
import com.proto2.message.Mybase.*;

import MySocket.*;

public class DataServer {

    public static void main(String args[]) {
        System.out.println("The data server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(5000);
        try {
            while (true) {
                new Server(listener.accept(), clientNumber++).run();
            }
        } finally {
            listener.close();
        }
    }

    private static class Server extends MachineFactory {
        private Socket mSocket = null;
        private BaseMessage MsgOut = null;

        public Server(Socket socket, int clientNumber) {
            super(socket, clientNumber);
        }

        public void run() {
            try {
                while (mAllowed <= 0) {
                    ReadFromPeer();
                    WriteToPeer();
                }
            } catch (Exception e) {
                log(e, mclientNumber);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log(e, mclientNumber);
                }
                log("Connection closed", mclientNumber);
            }
        }

        private void HandlerIn() {
            int iMessageType = MsgIn.getHead().getMessageType();
            mReqInfo.message_type = iMessageType;
            Body body = MsgIn.getBody();
            if (iMessageType == SEND_DATA_REQUEST) {
                SendDataRequest req = body.getExtension(send_data_request);
                mReqInfo.data = req.getData();
                mReqInfo.data_size = req.getSize();
                mReqInfo.data_type = req.getType();
            }
            if (iMessageType == QUERY_DATA_REQUEST) {
                SendDataRequest req = body.getExtension(query_data_request);
                mReqInfo.op_name = req.getName();
                mReqInfo.op_type = req.getType();
            }
            if (iMessageType == CLIENT_BEGIN_REQUEST) {
                mAllowed = 0;
            }
            if (iMessageType == CLIENT_END_REQUEST) {
                mAllowed = 1;
            }
        }

        private void HandlerOut() {
            int iMessageType = mReqInfo.message_type;

            Head.Builder Head = Head.newBuilder();
            Head.Builder Body = Body.newBuilder();

            if (iMessageType == SEND_DATA_REQUEST) {
                ResponseCode rc = ResponseCode.newBuilder().setRc(0).build();
                SendDataResponse rsp = SendDataResponse.newBuilder().setRc(rc).build();

                Head.setMessageType(SEND_DATA_RESPONSE);
                Body.setExtension(send_data_response, rsp);
            }
            if (iMessageType == QUERY_DATA_REQUEST) {
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
            if (iMessageType == CLIENT_BEGIN_REQUEST) {
                ResponseCode rc = ResponseCode.newBuilder().setRc(0).build();
                ClientBeginResponse rsp = ClientBeginResponse.newBuilder().setRc(rc).build();

                Head.setMessageType(CLIENT_BEGIN_RESPONSE);
                Body.setExtension(client_begin_response, rsp);
            }
            if (iMessageType == QUERY_DATA_REQUEST) {
                ResponseCode rc = ResponseCode.newBuilder().setRc(0).build();
                ClientEndResponse rsp = ClientEndResponse.newBuilder().setRc(rc).build();

                Head.setMessageType(CLIENT_END_RESPONSE);
                Body.setExtension(client_end_response, rsp);
            }
            MsgOut = BaseMessage.newBuilder().setHead(Head).setBody(Body).build();
        }

    }
}