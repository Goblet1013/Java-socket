package socketProject.socketChat01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    int port;
    List<Socket> clients;
    ServerSocket server;

    public static void main(String[] args) {
        new Server();
    }
    public Server(){
        try {
            port = 8080;
            clients = new ArrayList<>();
            server = new ServerSocket(port);

            while (true){
                Socket socket = server.accept();//阻塞
                clients.add(socket);
                Mythread mythread = new Mythread(socket);
                mythread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Mythread extends Thread{
        Socket serverSocket;
        private BufferedReader br;
        private PrintWriter pw;
        public String content;
        public Mythread(Socket s){
            serverSocket = s;
        }

        @Override
        public void run() {
            String name = null;
            try {
                //通过输入流（程序员为中心）从服务器套接字获取数据----socket连接成功的信息
                br = new BufferedReader(new InputStreamReader(serverSocket.getInputStream(),"UTF-8"));
                name = br.readLine();
                content = name + " join in，now client number is "
                        + clients.size();
                sendMsg();
                while ((content = br.readLine()) != null){
                    content = name + " say: " + content;
                    sendMsg();
                }
            }catch (Exception e){
                System.out.println(name + " leave");
                clients.remove(serverSocket);
            }

        }

        public void sendMsg(){
            try {
                System.out.println(content);
                for (int i=clients.size()-1;i>=0;i--){
                    //通过输出流（程序员为中心）向客户端套接字写数据----连接信息、聊天内容
                    pw = new PrintWriter(clients.get(i).getOutputStream(),true);
                    pw.println(content);
                    pw.flush();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

