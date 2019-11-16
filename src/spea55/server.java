package spea55;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;

class TCP_server{

    void RunServer() {

        final int PORT = 10000;
        BufferedReader reader = null;
        PrintWriter writer = null;
        Socket socket = null;
        ServerSocket ss_server = null;
        try {
            ss_server = new ServerSocket();
            ss_server.bind(new InetSocketAddress("localhost", PORT));

            System.out.println("クライアントからの入力待ち状態");
            //クライアントからの要求を待ち続ける
            socket = ss_server.accept();

            //クライアントからの受取用
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //サーバーからクライアントへの送信用
            writer = new PrintWriter(socket.getOutputStream(), true);

            String line = null;
            int num;
            while (true) {
                line = reader.readLine();
                if (line.equals("bye")) {
                    break;
                }

                try{
                    num = Integer.parseInt(line);
                    if (num%2==0) {
                        //送信用の文字を送信
                        writer.println("OK");
                    }else {
                        //送信用の文字を送信
                        writer.println("NG");
                    }

                }catch (NumberFormatException nfe){
                        //送信用の文字を送信
                    writer.println("数値を入力してください：");
                }
                System.out.println("クライアントで入力された文字：" + line);
            }

        } catch (Exception err) {
            err.printStackTrace();
        }finally {
            try{
                if (reader!=null){
                    reader.close();
                }
                if (writer!=null) {
                    writer.close();
                }
                if(socket!=null){
                    socket.close();
                }
                if(ss_server!=null){
                    ss_server.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }
}
public class server {
    public static void main(String[] args) {
        TCP_server ts1 = new TCP_server();
        ts1.RunServer();
    }
}

