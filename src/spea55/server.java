package spea55;

import javafx.scene.media.AudioClip;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

class TCP_server {

    private String links = "C:/Users/626ca/IdeaProjects/SocketChat/src/spea55/links.txt";

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
                if (line.equals("/bye")) {
                    break;
                }

                if (line.startsWith("/play")) {
                    String url = line.split(" ")[1];
                    System.out.println(url);

                    try {
                        if (!Files.exists(Paths.get(links))) {
                            Files.createFile(Paths.get(links));
                        }

                        String path = getPath(url);

                        if (path != null) {
                            //play music
                            AudioClip audioClip = new AudioClip(new File(path.split("\\.")[0] + ".mp3")
                                    .toURI().toString());
                            audioClip.setCycleCount(1);
                            audioClip.play();
                            Thread.sleep(3000);
                        } else {
                            //download music
                            Runtime runtime = Runtime.getRuntime();
                            Process p = runtime.exec("python " + "C:/Users/626ca/PycharmProjects/tubedoeloader/download.py "
                                    + url);
                            System.out.println("now downloading...");
                            p.waitFor();
                            System.out.println("download completed");

                            path = getPath(url);
                            System.out.println(path);
                            String command = "ffmpeg -i \"" + path + "\" \"" + path.split("\\.")[0] + ".mp3\"";
                            Process p2 = runtime.exec(command);
                            p2.waitFor();

                            AudioClip audioClip = new AudioClip(new File(path).toURI().toString());
                            audioClip.setCycleCount(1);
                            audioClip.play();
                            Thread.sleep(3000);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    //送信用の文字を送信
                    writer.println("Play \t" + url);
                }
            }

        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (socket != null) {
                    socket.close();
                }
                if (ss_server != null) {
                    ss_server.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private String getPath(String url) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(links), Charset.forName("UTF-8"));
            String path = null;
            for (String s : lines) {
               String tmp =s.split(",")[0];
                if (tmp.equals(url)) {
                    System.out.println("hit.");
                    path = s.split(",")[1];
                    path = "C:/Users/626ca/IdeaProjects/SocketChat/src/spea55/" + path;
                    return path;
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}

public class server {
    public static void main(String[] args) {
        TCP_server ts1 = new TCP_server();
        ts1.RunServer();
    }
}

