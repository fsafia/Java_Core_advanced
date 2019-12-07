package lesson6.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    MainServ serv;
    DataInputStream in;
    DataOutputStream out;
    String nick;

    public ClientHandler(MainServ serv, Socket socket){
        try {
            this.socket = socket;
            this.serv = serv;

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true){   //этот цикл будет отвечать за авторизацию
                            String msg = in.readUTF();
                            if (msg.startsWith("/auth")){  //ожидаем сообщение в виде auth_login1_pass1
                                String [] tokens = msg.split(" ");
                                String newNick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]); //передаем логин, пароль
                                if (newNick != null){
                                    sendMsg("/authok");
                                    nick = newNick;
                                    serv.subscribe(ClientHandler.this);
                                    break;
                                }
                                 else {
                                     sendMsg("Неверный логин/пароль");
                                }
                            }
                        }

                        while (true){  //отвечает за работу  с пользователем
                            String msg = in.readUTF();
                            if (msg.equals("/end")){
                                out.writeUTF("/serverClosed");
                                break;
                            }
                            serv.broadcastMsg(nick + ": " + msg);
                        }


                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        serv.unsubscribe(ClientHandler.this);
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String str){
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
