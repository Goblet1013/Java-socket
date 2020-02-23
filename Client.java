package socketProject.socketChat01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public int port = 8080;
    Socket socket = null;
    public String name;

    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("please input your name：");
        new Client(sc.next());
    }

    public Client(String name){
        this.name = name;
        try {
            socket = new Socket("127.0.0.1",port);
            new oneClient().start();
            //通过输入流（程序员为中心）从客户端套接字读取数据
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String msg1;
            while ((msg1 = br.readLine()) != null){
                System.out.println(msg1);
            }
        } catch (IOException e) {
            System.out.println("can not connect to server");
            System.exit(1);
        }

    }

    class oneClient extends Thread{
        @Override
        public void run() {
            try {
                //从键盘获取数据
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
                PrintWriter pw = null;
                //通过输出流（程序员为中心）向客户端套接字写数据
                pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println(name);
                String msg2;
                while (true){
                    msg2 = br.readLine();
                    pw.println(msg2);
                }
            } catch (IOException e) {
                System.out.println("disconnect to server");
                System.exit(1);
            }

        }
    }
}
