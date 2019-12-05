package lesson6.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public  class MainServ {

    private Vector<ClientHandler> clients; //cсинхронизированный арай лист для всех потоков клиентов
//    public static void main(String[] args) {

    public MainServ(){
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;
            try {
                server = new ServerSocket(8189);
                System.out.println("Сервер запущен!");

//                socket = server.accept();
//                System.out.println("клиент подключился!");

                while (true){
                    socket = server.accept();
                    System.out.println("клиент подключился!");
                    clients.add(new ClientHandler(this, socket));
                }

//                DataInputStream in = new DataInputStream(socket.getInputStream()); //входящий поток
//
//                DataOutputStream out = new DataOutputStream(socket.getOutputStream());//исходящий поток

//                while (true){
//                    String str = in.readUTF();
//                    if (str.equals("/end")){  //для корректного закрытия сервера
//                        break;
//                    }
//                    System.out.println("Client: " + str);
//
//                    out.writeUTF( str);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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
    //для отправки сообщений всем клиентам
    public void broadcastMsg(String msg){
        for (ClientHandler o : clients) {
            //метоод для отправки сообщения одному клиенту
            o.sendMsg(msg);
        }
    }
    }

