package spea55;

import java.net.*;
import java.io.*;

public class server {

    private static final int PORT = 10000;
    private static final char END = '.';

    public static void main(String[] args) {

        server sv = new server();
        try {
            ServerSocket ss_server = new ServerSocket(PORT);
            Socket socket = ss_server.accept();

            Reader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Writer out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            int c;
            StringBuilder sb = new StringBuilder(1024);
            String getMSG;
            while ((c = in.read()) != -1){
                if(c == END)break;
                sb.append((char)c);
            }

            getMSG = sb.toString();

            out.write(getMSG.toUpperCase());
            out.flush();
            System.out.println("receive:" + getMSG);

            socket.close();
            ss_server.close();

            System.exit(0);
        }catch (IOException err) {
            err.printStackTrace();
        }
    }
}

