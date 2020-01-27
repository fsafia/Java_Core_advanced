package lesson6.zadanieOsnovnoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        ServerSocket server = null;
        Socket socket = null;

        DataInputStream in;
        DataOutputStream out;
        try{
            server = new ServerSocket(8188);
            System.out.println("Сервер подключен");

            socket = server.accept();
            System.out.println("Клиент подключился");


            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {

                            String clientMsg = null;

                            clientMsg = in.readUTF();


                            if (clientMsg.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }
                            System.out.println("client: " + clientMsg);
                        }

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

            }).start();

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String servMsg = scanner.nextLine();
                if (servMsg.equals("/end")) {
                    break;
                }
                try {
                    out.writeUTF("Server: " + servMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
