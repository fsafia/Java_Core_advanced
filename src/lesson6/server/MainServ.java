package lesson6.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public  class MainServ {

    private Vector<ClientHandler> clients; //cсинхронизированный арай лист для всех потоков клиентов
//    public static void main(String[] args) {

    public MainServ(){
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;
            try {
                AuthService.connect(); //подключаемся к базе данных
                //код ДЛЯ тестового подключения

//                try {
//                    String res = AuthService.getNickByLoginAndPass("login1", "pass1" );
//                    System.out.println(res + "соединение с БД");
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
                server = new ServerSocket(8189);
                System.out.println("Сервер запущен!");

                while (true){
                    socket = server.accept();
                    System.out.println("клиент подключился!");
                    new ClientHandler(this, socket);
                }

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
                AuthService.disconnect();
            }
        }
    //для отправки сообщений всем клиентам
    public void broadcastMsg(ClientHandler from, String msg){
        for (ClientHandler o : clients) {
            if (!o.checkBlackList(from.getNick())){
                o.sendMsg(msg);//метоод для отправки сообщения одному клиенту
            }
        }
    }
//отображение онлайн клиентов
    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder();
        sb.append("/clientslist ");
        for (ClientHandler o : clients) {
            sb.append(o.getNick() + " ");
        }
        String out = sb.toString();
        for (ClientHandler o : clients) {
            o.sendMsg(out);
        }
    }

    public void sendPersonalMsg(ClientHandler from, String nickTo, String msg){
        for (ClientHandler o : clients){
            if (o.getNick().equals(nickTo)){
                o.sendMsg("to " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMsg("Клиент с ником " + nickTo + " не найден в чате");
    }

    public void subscribe(ClientHandler client){
        clients.add(client);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler client){
        clients.remove(client);
        broadcastClientsList();
    }

    public boolean isNickBusy(String newNick){
        for (ClientHandler o : clients) {
            if (o.getNick().equals(newNick)){
                return true;
            }
        }
        return false;
    }


}

