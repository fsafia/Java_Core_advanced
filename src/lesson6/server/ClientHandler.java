package lesson6.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ClientHandler {
    private Socket socket;
    MainServ serv;
    DataInputStream in;
    DataOutputStream out;
    List<String> blackList;
    String nick;

    public String getNick() {
        return nick;
    }

    public ClientHandler(MainServ serv, Socket socket){
        try {
            this.socket = socket;
            this.serv = serv;

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.blackList = new ArrayList<>();//пользователь добавил другого пользов в черный список и теперь не получит от него сообщения

            new Thread(()-> {
                   try {
                        while (true){   //этот цикл будет отвечать за авторизацию и регистрацию
                            String msg = in.readUTF();
                            if (msg.startsWith("/auth")){  //ожидаем сообщение в виде auth_login1_pass1
                                String [] tokens = msg.split(" ");
                                String newNick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]); //передаем логин, пароль

                                if (newNick != null) {
                                    if(!serv.isNickBusy(newNick)){
                                        sendMsg("/authok");
                                        nick = newNick;

                                        serv.subscribe(ClientHandler.this);
                                        MainServ.logger.log(Level.INFO, "клиент " + nick + " подключился");
                                        break;
                                    } else {
                                        sendMsg("Учетная запись уже используется");
                                        MainServ.logger.log(Level.INFO, newNick + " Учетная запись уже используется");//
                                    }

                                }
                                else {
                                    sendMsg("Неверный логин/пароль");
                                    MainServ.logger.log(Level.INFO, "Неверный логин " + tokens[1] + " /пароль " + tokens[2]);//
                                }
                            }

                            //регистрация
                            if (msg.startsWith("/signup")){ //ожидаем сообщения в виде /signup login4 pass4 nick4
                                String [] tokens = msg.split(" ");
                                if (AuthService.isLoginUnique(tokens[1])){
                                    AuthService.addUser(tokens[1], tokens[2], tokens[3]);
                                    sendMsg("/authok");
                                    nick = tokens[3];
                                    serv.subscribe(ClientHandler.this);
                                    break;
                                } else {
                                    sendMsg("Логин уже используется");
                                }

                            }
                        }

                        while (true){  //отвечает за работу  с пользователем
                            String str = in.readUTF();
                            if (str.startsWith("/")) {
                                if (str.equals("/end")) {
                                    out.writeUTF("/serverClosed");
                                    break;
                                }

                                //отправка персональных сообщений
                                if (str.startsWith("/w ")) {  //сообщение должно начинаться с /w nick1 textMsg
                                    String[] tokens = str.split(" ", 3);//делим все сообщение на 3 части: до пробела, после 1го пробела до 2го, после 2го
                                    String m = str.substring(tokens[1].length() + 4); //это текст для конкретного nick
                                    serv.sendPersonalMsg(this, tokens[1], tokens[2]);
                                    MainServ.logger.log(Level.INFO, nick + " отправил персональное сообщение для " + tokens[1]);//
                                }
                                if (str.startsWith("/blacklist ")) { //blacklist nick3
                                    String[] tokens = str.split(" ");
                                    blackList.add(tokens[1]);
                                    //
                                    int idThisNick = AuthService.getIdByNick(nick);
                                    int idNickToBlock = AuthService.getIdByNick(tokens[1]);
                                    String msg = AuthService.addUserIntoDBtableBlackList(idThisNick, idNickToBlock);
                                    //
                                    sendMsg(tokens[1] + msg);
                                    serv.broadcastClientsList(this);
                                }
                            } else {
                                serv.broadcastMsg(this, nick + ": " + str);
                                MainServ.logger.log(Level.INFO, nick + " отправил сообщение  для всех клиентов.");//
                            }
                            System.out.println("Client" + str);



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
    public boolean checkBlackList(String nick) {
        return blackList.contains(nick);
    }
}
