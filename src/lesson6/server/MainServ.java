package lesson6.server;

import lesson6.zadanieOsnovnoe.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.Vector;
import java.util.logging.*;

public  class MainServ {

    private Vector<ClientHandler> clients; //синхронизированный арай лист для всех потоков клиентов
     static final Logger logger = Logger.getLogger(lesson6.server.MainServ.class.getName());//


    public MainServ(){
        logger.setLevel(Level.ALL);//
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
                Handler handler = new FileHandler("sereverLog.log", true);//
                handler.setLevel(Level.ALL);
                handler.setFormatter(new SimpleFormatter());
                logger.addHandler(handler);//
                server = new ServerSocket(8189);
                System.out.println("Сервер запущен!");
                logger.log(Level.INFO, "Сервер запущен!");

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
    //для отправки сообщений всем клиентам, кроме клиентов из черного списка
    public void broadcastMsg(ClientHandler from, String msg){

        for (ClientHandler o : clients) {
            //
            //выводим список  Set<Integer> id заблокированных клиентов для текущего клиента "о"
            Set<Integer> blockUserList = AuthService.getListIdBlockUsersByNick(o.getNick());
            if (!isClientBlock(from, blockUserList)){
                o.sendMsg(msg);
            }
        }
    }
//отображение онлайн клиентов в колонке clientslist или blacklist
    public void broadcastClientsList(ClientHandler client) {
        //для формирования онлайн списка незаблокированных клиентов
        StringBuilder sb = new StringBuilder();
        sb.append("/clientslist clientslist: ");
        //для формирования онлайн списка заблокированных клиентов
        StringBuilder blackListClients = new StringBuilder();
        blackListClients.append("/blacklist blacklist: ");

        for (ClientHandler o : clients) {
            String nickThisClient = o.getNick();//ник текущего клиента
            //выводим список  Set<Integer> id заблокированных клиентов для текущего клиента "о"
            Set<Integer> blockUserList = AuthService.getListIdBlockUsersByNick(nickThisClient);

            for (ClientHandler onLineClient : clients) {
                if (isClientBlock(onLineClient, blockUserList)){
                    blackListClients.append(onLineClient.getNick() + " ");
                } else {
                    sb.append(onLineClient.getNick() + " ");
                }
            }
            String outOnlineClient = sb.toString();
            String outBlockClient = blackListClients.toString();

            o.sendMsg(outOnlineClient);
            o.sendMsg(outBlockClient);

            sb.delete(26, sb.length()); //обновляем строку до состояния "/clientslist clientslist: "
            blackListClients.delete(22, blackListClients.length()); //обновляем строку до состояния "/blacklist blacklist: "
        }
    }
    //проверка: онлайнклиент в черном списке?
    public boolean isClientBlock(ClientHandler onLineClient, Set<Integer> blockUserList){

        Integer idOnLineClient = AuthService.getIdByNick(onLineClient.getNick());
        for (Integer idBlockUser : blockUserList) {
            if (idBlockUser == idOnLineClient){
                return true;
            }
        }
        return false;
    }

    public void sendPersonalMsg(ClientHandler from, String nickTo, String msg){
        for (ClientHandler o : clients){
            if (o.getNick().equals(nickTo)){
                //выводим список  Set<Integer> id заблокированных клиентов для текущего клиента "nickTo"
                Set<Integer> blockUserList = AuthService.getListIdBlockUsersByNick(nickTo);
                if (!isClientBlock(from, blockUserList)){
                    o.sendMsg( from.getNick() + " to " + nickTo + ": " + msg);
                    return;
                } else {
                    return;
                }
            }
        }
        from.sendMsg("Клиент с ником " + nickTo + " не найден в чате");
    }

    public void subscribe(ClientHandler client){
        clients.add(client);
        broadcastClientsList(client);
    }

    public void unsubscribe(ClientHandler client){
        clients.remove(client);
        broadcastClientsList(client);
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

