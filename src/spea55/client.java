package spea55;

import java.io.*;
import java.net.Socket;

class client {

    static final int PORT = 10000;

    public static void main(String[] args) {

        try(Socket sc = new Socket("local", PORT)){

            BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));

            String receive = "";

            String sendMSG = args[0];
            out.write(sendMSG);
            out.write('.');
            out.flush();

            System.out.println("receive->");
            char[] buf = new char[1024];
            while (in.read(buf) != -1){
                System.out.println(buf);
            }
            System.out.println("\n");

            sc.close();

            System.exit(0);

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}