public class Socket {

    public static class MachineFactory extends Thread {
        private Socket mSocket = null;
        private int mclientNumber;
        private BaseMessage MsgOut = null;

        private void log(Exception e) {
            System.out.println("Error handling client# " + mclientNumber + ": " + e);
            e.printStackTrace();
        }

        private void log(String message) {
            System.out.println(message);
        }

        private void ReadFromPeer() {
            InputStream in = mSocket.getInputStream();
            BaseMessage MsgIn = BaseMessage.parseFrom(in);
            in.close();
            Handler(MsgIn);
        }

        private void WriteToPeer() {
            OutputStream out = mSocket.getOutputStream();
            MsgOut.writeTo(out);
            out.close();
        }

        private void Handler(BaseMessage msg) {
        }
    }

}