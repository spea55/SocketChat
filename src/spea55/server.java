package spea55;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

class MusicPlayer {

    private String links = "C:/Users/626ca/IdeaProjects/SocketChat/src/spea55/links.txt";
    private ArrayBlockingQueue<String> music_queue = new ArrayBlockingQueue<>(64);

    void RunServer() {

        final int PORT = 10000;
        ServerSocket ss_server;

        try {
            new Thread(this::PlayMusic_Loop).start();

            ss_server = new ServerSocket();
            ss_server.bind(new InetSocketAddress("192.168.11.3", PORT));

            System.out.println("クライアントからの入力待ち状態");
            //クライアントからの要求を待ち続ける
            while (true){
                Socket socket = ss_server.accept();
                new Thread(() -> {
                    try {
                        ListenClient(socket);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }


        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private String getPath(String url) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(links), StandardCharsets.UTF_8);
            String path;
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

    private void ListenClient(Socket socket) throws IOException, InterruptedException {
        //クライアントからの受取用
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //サーバーからクライアントへの送信用
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        String line;
        while (true) {
            line = reader.readLine();
            System.out.println(line);
            if (line.equals("/bye")) {
                break;
            }

            if (line.startsWith("/play")) {
                String url = line.split(" ")[1];
                System.out.println(url);

                String path = getPath(url);
                if (path != null) {
                    //play music
                    if (!music_queue.offer(path.split("\\.")[0] + ".mp3"))
                        writer.write("Queue is full.");
                    else writer.write("Added to Queue.");
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
                    assert path != null;
                    String command = "ffmpeg -i \"" + path + "\" \"" + path.split("\\.")[0] + ".mp3\"";
                    Process p2 = runtime.exec(command);
                    p2.waitFor();

                    if (!music_queue.offer(path.split("\\.")[0] + ".mp3"))
                        writer.write("Queue is full.");
                    else writer.write("Added to Queue.");
                }
            }


        }
    }

    private void PlayMusic_PyWrapper(String path) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec("python C:/Users/626ca/PycharmProjects/music_player/play_music.py " + path);
        p.waitFor();
//        System.out.println("Play music " + path);
    }

    private void PlayMusic_Loop(){
        while (true){
            try {
                String path = music_queue.take();

                if (!Files.exists(Paths.get(links))) {
                    Files.createFile(Paths.get(links));
                }

                PlayMusic_PyWrapper(path.split("\\.")[0] + ".mp3");

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}

public class server {
    public static void main(String[] args) {
        MusicPlayer ts1 = new MusicPlayer();
        ts1.RunServer();
    }
}

