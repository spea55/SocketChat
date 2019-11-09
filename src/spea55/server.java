package spea55;

import java.io.IOException;
import java.net.ServerSocket;

public class server {

    private static int PORT = 10000;

    public static void main(String[] args) {
        try{
            //serverSocket
            ServerSocket serverSocket = new ServerSocket(PORT);

            

        }catch (IOException ioe){
            System.out.println("エラーが発生しました");
        }
    }
}
