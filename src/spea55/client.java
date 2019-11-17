package spea55;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class TCP_client{
     void RunClient(){
         final int PORT = 10000;
         BufferedReader csInput = null;
         BufferedReader reader = null;
         PrintWriter writer = null;
         Socket socket = null;
         try{
             socket = new Socket("localhost", PORT);



             //サーバ側からの受取
             reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

             //クライアント側からサーバーへの送信用
             writer = new PrintWriter(socket.getOutputStream(), true);

             //クライアント側での入力用
             csInput = new BufferedReader(new InputStreamReader(System.in));

             //無限ループ　byeの入力でループを抜ける
             String line = null;
             while(true) {
                 line = csInput.readLine();

                 writer.println(line);

                 //byeの入力でループを抜ける
                 if (line.equals("/bye")) {
                     break;
                 }

                 //サーバ側からの受取の結果を表示
                 System.out.println(reader.readLine());
             }
         } catch(Exception e){
             e.printStackTrace();
         }finally {
             try{
                 if(reader != null){
                     reader.close();
                 }
                 if(writer != null){
                     writer.close();
                 }
                 if(socket != null){
                     socket.close();
                 }
                 if(csInput != null){
                     csInput.close();
                 }
             }catch (IOException ioe){
                 ioe.printStackTrace();
             }
         }
         System.out.println("クライアント側終了です");
     }
}

class client {
    public static void main(String[] args) {
        TCP_client tc1 = new TCP_client();
        tc1.RunClient();
    }
}